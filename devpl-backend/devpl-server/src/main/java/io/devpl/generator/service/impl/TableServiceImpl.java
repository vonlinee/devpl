package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.devpl.generator.common.exception.ServerException;
import io.devpl.generator.common.mvc.BaseServiceImpl;
import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.config.ConnectionInfo;
import io.devpl.generator.config.DbType;
import io.devpl.generator.config.query.AbstractQuery;
import io.devpl.generator.config.template.DeveloperInfo;
import io.devpl.generator.config.template.GeneratorInfo;
import io.devpl.generator.config.template.ProjectInfo;
import io.devpl.generator.dao.TableMapper;
import io.devpl.generator.entity.GenTable;
import io.devpl.generator.entity.GenTableField;
import io.devpl.generator.enums.FormLayoutEnum;
import io.devpl.generator.enums.GeneratorTypeEnum;
import io.devpl.generator.service.DataSourceService;
import io.devpl.generator.service.GeneratorConfigService;
import io.devpl.generator.service.TableFieldService;
import io.devpl.generator.service.TableService;
import io.devpl.generator.utils.CollectionUtils;
import io.devpl.generator.utils.NamingUtils;
import io.devpl.sdk.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.Date;
import java.util.*;

/**
 * 数据表
 */
@Slf4j
@Service
@AllArgsConstructor
public class TableServiceImpl extends BaseServiceImpl<TableMapper, GenTable> implements TableService {
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
    public boolean deleteBatchIds(Long[] ids) {
        // 删除表
        baseMapper.deleteBatchIds(Arrays.asList(ids));
        // 删除列
        return tableFieldService.deleteBatchTableIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importTable(Long datasourceId, String tableName) {
        // 初始化配置信息
        ConnectionInfo dataSource = dataSourceService.findById(datasourceId);
        // 查询表是否存在
        GenTable table = this.getByTableName(tableName);
        // 表存在
        if (table != null) {
            throw new ServerException(tableName + "已存在");
        }

        // 从数据库获取表信息
        table = getTable(dataSource, tableName);
        // 代码生成器信息
        GeneratorInfo generator = generatorConfigService.getGeneratorInfo(true);

        ProjectInfo project = generator.getProject();

        DeveloperInfo developer = generator.getDeveloper();

        // 保存表信息
        table.setPackageName(project.getPackageName());
        table.setVersion(project.getVersion());
        table.setBackendPath(project.getBackendPath());
        table.setFrontendPath(project.getFrontendPath());

        table.setAuthor(developer.getAuthor());
        table.setEmail(developer.getEmail());

        table.setFormLayout(FormLayoutEnum.ONE.getValue());
        table.setGeneratorType(GeneratorTypeEnum.ZIP_DOWNLOAD.ordinal());
        table.setClassName(NamingUtils.toPascalCase(tableName));
        table.setModuleName(getModuleName(table.getPackageName()));
        table.setFunctionName(getFunctionName(tableName));
        table.setCreateTime(new Date());
        this.save(table);

        // 获取原生字段数据
        List<GenTableField> tableFieldList = getTableFieldList(dataSource, table.getId(), table.getTableName());
        // 初始化字段数据
        tableFieldService.initFieldList(tableFieldList);
        // 保存列数据
        tableFieldService.saveBatch(tableFieldList);

        dataSource.closeConnection();
    }

    /**
     * 获取功能名
     * @param tableName 表名
     * @return 功能名
     */
    public static String getFunctionName(String tableName) {
        String functionName = StringUtils.subAfter(tableName, "_", true);
        if (StringUtils.isBlank(functionName)) {
            functionName = tableName;
        }
        return functionName;
    }

    /**
     * 获取模块名
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        return StringUtils.subAfter(packageName, ".", true);
    }

    /**
     * 根据数据源，获取指定数据表 GenTable 实体
     * @param datasource 数据源
     * @param tableName  表名
     */
    @Override
    public GenTable getTable(ConnectionInfo datasource, String tableName) {
        AbstractQuery query = datasource.getDbQuery();
        String tableQuerySql = query.getTableQuerySql(tableName);
        // 查询数据
        try (Connection connection = datasource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(tableQuerySql)) {
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        GenTable table = new GenTable();
                        table.setTableName(rs.getString(query.tableName()));
                        table.setTableComment(rs.getString(query.tableComment()));
                        table.setDatasourceId(datasource.getId());
                        return table;
                    }
                }
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
        ConnectionInfo datasource = dataSourceService.findById(table.getDatasourceId());

        // 从数据库获取表字段列表
        List<GenTableField> dbTableFieldList = getTableFieldList(datasource, table.getId(), table.getTableName());
        if (dbTableFieldList.isEmpty()) {
            throw new ServerException("同步失败，请检查数据库表：" + table.getTableName());
        }

        // 表字段列表
        List<GenTableField> tableFieldList = tableFieldService.listByTableId(id);

        Map<String, GenTableField> tableFieldMap = CollectionUtils.toMap(tableFieldList, GenTableField::getFieldName);

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
        List<String> dbTableFieldNameList = CollectionUtils.toList(dbTableFieldList, GenTableField::getFieldName);
        List<GenTableField> delFieldList = CollectionUtils.filter(tableFieldList, field -> !dbTableFieldNameList.contains(field.getFieldName()));

        if (!delFieldList.isEmpty()) {
            tableFieldService.removeBatchByIds(CollectionUtils.toList(delFieldList, GenTableField::getId));
        }
    }

    /**
     * 获取表字段列表
     * @param datasource 数据源
     * @param tableId    表ID
     * @param tableName  表名
     */
    @Override
    public List<GenTableField> getTableFieldList(ConnectionInfo datasource, Long tableId, String tableName) {
        List<GenTableField> tableFieldList = new ArrayList<>();

        AbstractQuery query = datasource.getDbQuery();
        String tableFieldsSql = query.getTableFieldsQuerySql();
        try {
            if (datasource.getDbType() == DbType.Oracle) {
                DatabaseMetaData md = datasource.getConnection().getMetaData();
                tableFieldsSql = String.format(tableFieldsSql.replace("#schema", md.getUserName()), tableName);
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        try (Connection connection = dataSourceService.getConnection(datasource.getId())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(tableFieldsSql)) {
                try (ResultSet rs = preparedStatement.executeQuery()) {
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
                        field.setPrimaryKey(StringUtils.hasText(key) && "PRI".equalsIgnoreCase(key));

                        tableFieldList.add(field);
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw ServerException.create("异常", e);
        }
        return tableFieldList;
    }

    /**
     * 根据数据源，获取全部数据表
     * @param datasource 数据源
     */
    @Override
    public List<GenTable> getTableList(ConnectionInfo datasource) {
        List<GenTable> tableList = new ArrayList<>();
        AbstractQuery query = datasource.getDbQuery();
        String tableQuerySql = query.getTableQuerySql(null);
        try (Connection connection = datasource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(tableQuerySql)) {
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    // 查询数据
                    while (rs.next()) {
                        GenTable table = new GenTable();
                        table.setTableName(rs.getString(query.tableName()));
                        table.setTableComment(rs.getString(query.tableComment()));
                        table.setDatasourceId(datasource.getId());
                        tableList.add(table);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return tableList;
    }
}
