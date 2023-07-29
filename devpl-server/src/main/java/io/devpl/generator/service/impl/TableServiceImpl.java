package io.devpl.generator.service.impl;

import cn.hutool.core.text.NamingCase;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.devpl.codegen.utils.JdbcUtils;
import io.devpl.generator.common.exception.ServerException;
import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.service.impl.BaseServiceImpl;
import io.devpl.generator.config.DataSourceInfo;
import io.devpl.generator.config.DbType;
import io.devpl.generator.config.query.AbstractQuery;
import io.devpl.generator.config.template.GeneratorConfig;
import io.devpl.generator.config.template.GeneratorInfo;
import io.devpl.generator.dao.TableDao;
import io.devpl.generator.entity.GenTable;
import io.devpl.generator.entity.GenTableField;
import io.devpl.generator.enums.FormLayoutEnum;
import io.devpl.generator.enums.GeneratorTypeEnum;
import io.devpl.generator.service.DataSourceService;
import io.devpl.generator.service.GeneratorConfigService;
import io.devpl.generator.service.TableFieldService;
import io.devpl.generator.service.TableService;
import io.devpl.generator.utils.DbUtils;
import io.devpl.generator.utils.GenUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据表
 */
@Slf4j
@Service
@AllArgsConstructor
public class TableServiceImpl extends BaseServiceImpl<TableDao, GenTable> implements TableService {
    private final TableFieldService tableFieldService;
    private final DataSourceService dataSourceService;
    private final GeneratorConfigService generatorConfigService;

    @Override
    public PageResult<GenTable> page(Query query) {
        IPage<GenTable> page = baseMapper.selectPage(getPage(query), getWrapper(query));
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public GenTable getByTableName(String tableName) {
        LambdaQueryWrapper<GenTable> queryWrapper = Wrappers.lambdaQuery();
        return baseMapper.selectOne(queryWrapper.eq(GenTable::getTableName, tableName));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchIds(Long[] ids) {
        // 删除表
        baseMapper.deleteBatchIds(Arrays.asList(ids));
        // 删除列
        tableFieldService.deleteBatchTableIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importTable(Long datasourceId, String tableName) {
        // 初始化配置信息
        DataSourceInfo dataSource = dataSourceService.findById(datasourceId);
        // 查询表是否存在
        GenTable table = this.getByTableName(tableName);
        // 表存在
        if (table != null) {
            throw new ServerException(tableName + "已存在");
        }

        // 从数据库获取表信息
        table = getTable(dataSource, tableName);
        // 代码生成器信息
        GeneratorInfo generator = generatorConfigService.getGeneratorInfo();

        // 保存表信息
        table.setPackageName(generator.getProject().getPackageName());
        table.setVersion(generator.getProject().getVersion());
        table.setBackendPath(generator.getProject().getBackendPath());
        table.setFrontendPath(generator.getProject().getFrontendPath());
        table.setAuthor(generator.getDeveloper().getAuthor());
        table.setEmail(generator.getDeveloper().getEmail());
        table.setFormLayout(FormLayoutEnum.ONE.getValue());
        table.setGeneratorType(GeneratorTypeEnum.ZIP_DOWNLOAD.ordinal());
        table.setClassName(NamingCase.toPascalCase(tableName));
        table.setModuleName(GenUtils.getModuleName(table.getPackageName()));
        table.setFunctionName(GenUtils.getFunctionName(tableName));
        table.setCreateTime(new Date());
        this.save(table);

        // 获取原生字段数据
        List<GenTableField> tableFieldList = getTableFieldList(dataSource, table.getId(), table.getTableName());
        // 初始化字段数据
        tableFieldService.initFieldList(tableFieldList);

        // 保存列数据
        tableFieldService.saveBatch(tableFieldList);

        try {
            // 释放数据源
            dataSource.getConnection().close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 根据数据源，获取指定数据表
     * @param datasource 数据源
     * @param tableName  表名
     */
    @Override
    public GenTable getTable(DataSourceInfo datasource, String tableName) {
        AbstractQuery query = datasource.getDbQuery();
        String tableQuerySql = query.getTableQuerySql(tableName);
        // 查询数据
        try (Connection connection = datasource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(tableQuerySql); ResultSet rs = preparedStatement.executeQuery()) {
            if (rs.next()) {
                GenTable table = new GenTable();
                table.setTableName(rs.getString(query.tableName()));
                table.setTableComment(rs.getString(query.tableComment()));
                table.setDatasourceId(datasource.getId());
                return table;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        throw new ServerException("数据表不存在：" + tableName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sync(Long id) {
        GenTable table = this.getById(id);

        // 初始化配置信息
        DataSourceInfo datasource = dataSourceService.findById(table.getDatasourceId());

        // 从数据库获取表字段列表
        List<GenTableField> dbTableFieldList = getTableFieldList(datasource, table.getId(), table.getTableName());
        if (dbTableFieldList.size() == 0) {
            throw new ServerException("同步失败，请检查数据库表：" + table.getTableName());
        }

        List<String> dbTableFieldNameList = dbTableFieldList.stream().map(GenTableField::getFieldName).toList();

        // 表字段列表
        List<GenTableField> tableFieldList = tableFieldService.listByTableId(id);

        Map<String, GenTableField> tableFieldMap = tableFieldList.stream()
            .collect(Collectors.toMap(GenTableField::getFieldName, Function.identity()));

        // 初始化字段数据
        tableFieldService.initFieldList(dbTableFieldList);

        // 同步表结构字段
        dbTableFieldList.forEach(field -> {
            // 新增字段
            if (!tableFieldMap.containsKey(field.getFieldName())) {
                tableFieldService.save(field);
                return;
            }

            // 修改字段
            GenTableField updateField = tableFieldMap.get(field.getFieldName());
            updateField.setPrimaryKey(field.isPrimaryKey());
            updateField.setFieldComment(field.getFieldComment());
            updateField.setFieldType(field.getFieldType());
            updateField.setAttrType(field.getAttrType());

            tableFieldService.updateById(updateField);
        });

        // 删除数据库表中没有的字段
        List<GenTableField> delFieldList = tableFieldList.stream()
            .filter(field -> !dbTableFieldNameList.contains(field.getFieldName())).toList();
        if (delFieldList.size() > 0) {
            List<Long> fieldIds = delFieldList.stream().map(GenTableField::getId).collect(Collectors.toList());
            tableFieldService.removeBatchByIds(fieldIds);
        }
    }

    /**
     * 获取表字段列表
     * @param datasource 数据源
     * @param tableId    表ID
     * @param tableName  表名
     */
    @Override
    public List<GenTableField> getTableFieldList(DataSourceInfo datasource, Long tableId, String tableName) {
        List<GenTableField> tableFieldList = new ArrayList<>();
        try {
            AbstractQuery query = datasource.getDbQuery();
            String tableFieldsSql = query.getTableFieldsQuerySql();
            if (datasource.getDbType() == DbType.Oracle) {
                DatabaseMetaData md = datasource.getConnection().getMetaData();
                tableFieldsSql = String.format(tableFieldsSql.replace("#schema", md.getUserName()), tableName);
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            }
            try (Connection connection = datasource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(tableFieldsSql);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    GenTableField field = new GenTableField();
                    field.setTableId(tableId);
                    field.setFieldName(rs.getString(query.fieldName()));
                    String fieldType = rs.getString(query.fieldType());
                    if (fieldType.contains(" ")) {
                        fieldType = fieldType.substring(0, fieldType.indexOf(" "));
                    }
                    field.setFieldType(fieldType);
                    field.setFieldComment(rs.getString(query.fieldComment()));
                    String key = rs.getString(query.fieldKey());
                    field.setPrimaryKey(StringUtils.isNotBlank(key) && "PRI".equalsIgnoreCase(key));

                    tableFieldList.add(field);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return tableFieldList;
    }


    /**
     * 根据数据源，获取全部数据表
     * @param datasource 数据源
     */
    @Override
    public List<GenTable> getTableList(DataSourceInfo datasource) {
        List<GenTable> tableList = new ArrayList<>();
        AbstractQuery query = datasource.getDbQuery();
        String tableQuerySql = query.getTableQuerySql(null);
        try (Connection connection = datasource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(tableQuerySql); ResultSet rs = preparedStatement.executeQuery();) {
            // 查询数据
            while (rs.next()) {
                GenTable table = new GenTable();
                table.setTableName(rs.getString(query.tableName()));
                table.setTableComment(rs.getString(query.tableComment()));
                table.setDatasourceId(datasource.getId());
                tableList.add(table);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return tableList;
    }
}
