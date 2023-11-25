package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.generator.jdbc.DBType;
import io.devpl.generator.common.ServerException;
import io.devpl.generator.common.mvc.BaseServiceImpl;
import io.devpl.generator.common.query.ListResult;
import io.devpl.generator.config.query.AbstractQuery;
import io.devpl.generator.config.template.DeveloperInfo;
import io.devpl.generator.config.template.GeneratorInfo;
import io.devpl.generator.config.template.ProjectInfo;
import io.devpl.generator.dao.GenTableMapper;
import io.devpl.generator.domain.param.Query;
import io.devpl.generator.entity.*;
import io.devpl.generator.enums.FormLayoutEnum;
import io.devpl.generator.enums.GeneratorTypeEnum;
import io.devpl.generator.service.*;
import io.devpl.generator.utils.EncryptUtils;
import io.devpl.generator.utils.NamingUtils;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据表
 */
@Slf4j
@Service
@AllArgsConstructor
public class GenTableServiceImpl extends BaseServiceImpl<GenTableMapper, GenTable> implements GenTableService {
    private final GenTableFieldService tableFieldService;
    private final DataSourceService dataSourceService;
    private final GeneratorConfigService generatorConfigService;
    private final TargetGenFileService targetGenFileService;
    private final TableFileGenerationService tableFileGenerationService;

    @Override
    public ListResult<GenTable> page(Query query) {
        return ListResult.ok(baseMapper.selectPage(getPage(query), getWrapper(query)));
    }

    @Override
    public GenTable getByTableName(String tableName) {
        return baseMapper.selectOneByTableName(tableName);
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
        // 查询表是否存在
        GenTable table = this.getByTableName(tableName);
        // 表存在
        if (table != null) {
            throw new ServerException(tableName + "已存在");
        }

        DBType dbType = DBType.MYSQL;
        if (datasourceId != 0) {
            DbConnInfo connInfo = dataSourceService.getById(datasourceId);
            connInfo.setPassword(EncryptUtils.decrypt(connInfo.getPassword()));
        }

        // 代码生成器信息
        GeneratorInfo generator = generatorConfigService.getGeneratorInfo(true);

        try (Connection connection = dataSourceService.getConnection(datasourceId)) {
            AbstractQuery query = dataSourceService.getQuery(dbType);
            // 从数据库获取表信息
            table = this.getTable(connection, query, datasourceId, tableName);

            // 保存表信息
            // 项目信息
            ProjectInfo project = generator.getProject();
            table.setPackageName(project.getPackageName());
            table.setVersion(project.getVersion());
            table.setBackendPath(project.getBackendPath());
            table.setFrontendPath(project.getFrontendPath());

            DeveloperInfo developer = generator.getDeveloper();
            table.setAuthor(developer.getAuthor());
            table.setEmail(developer.getEmail());

            table.setClassName(NamingUtils.toPascalCase(tableName));
            table.setModuleName(getModuleName(table.getPackageName()));
            table.setFunctionName(getFunctionName(tableName));

            // 默认初始值
            table.setFormLayout(FormLayoutEnum.ONE.getValue());
            table.setGeneratorType(GeneratorTypeEnum.CUSTOM_DIRECTORY.ordinal());

            table.setCreateTime(LocalDateTime.now());
            this.save(table);

            initGenerationFiles(table);

            // 获取原生字段数据
            List<GenTableField> tableFieldList = getTableFieldList(dbType, connection, query, datasourceId, table.getId(), table.getTableName());
            // 初始化字段数据
            tableFieldService.initFieldList(tableFieldList);
            // 保存列数据
            tableFieldService.saveBatch(tableFieldList);
        } catch (SQLException exception) {
            log.error("导入表失败", exception);
        }
    }

    /**
     * 初始化要生成的文件信息
     *
     * @param table 要生成的数据库表
     */
    public void initGenerationFiles(GenTable table) {
        List<TargetGenFile> targetGenFiles = targetGenFileService.listGeneratedFileTypes();
        List<TableFileGeneration> list = new ArrayList<>();
        for (TargetGenFile targetGenFile : targetGenFiles) {
            TableFileGeneration tfg = new TableFileGeneration();
            tfg.setTableId(table.getId());
            // 可能为空
            tfg.setTemplateId(targetGenFile.getTemplateId());
            tfg.setFileName(targetGenFile.getFileName());
            list.add(tfg);
        }
        tableFileGenerationService.saveBatch(list);
    }

    /**
     * 获取功能名
     *
     * @param tableName 表名
     * @return 功能名
     */
    public String getFunctionName(String tableName) {
        String functionName = StringUtils.subAfter(tableName, "_", true);
        if (StringUtils.isBlank(functionName)) {
            functionName = tableName;
        }
        return functionName;
    }

    /**
     * 获取模块名
     *
     * @param packageName 包名
     * @return 模块名
     */
    public String getModuleName(String packageName) {
        return StringUtils.subAfter(packageName, ".", true);
    }

    /**
     * 根据数据源，获取指定数据表 GenTable 实体
     */
    @Override
    public GenTable getTable(Connection connection, AbstractQuery query, Long dataSourceId, String tableName) {
        String tableQuerySql = query.getTableQuerySql(tableName);
        // 查询数据
        try (PreparedStatement preparedStatement = connection.prepareStatement(tableQuerySql)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    GenTable table = new GenTable();
                    table.setTableName(rs.getString(query.getTableNameResultSetColumnName()));
                    table.setTableComment(rs.getString(query.tableComment()));
                    table.setDatasourceId(dataSourceId);
                    return table;
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

        DbConnInfo connInfo = dataSourceService.getById(table.getDatasourceId());

        DBType dbType = DBType.getValue(connInfo.getDbType());
        AbstractQuery query = dataSourceService.getQuery(dbType);

        List<GenTableField> dbTableFieldList;
        try (Connection connection = dataSourceService.getConnection(connInfo)) {
            dbTableFieldList = getTableFieldList(dbType, connection, query, table.getDatasourceId(), table.getId(), table.getTableName());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        // 从数据库获取表字段列表
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
     * 获取表的所有字段
     *
     * @param dbType    数据库类型
     * @param tableId   表ID
     * @param tableName 表名
     * @return 表的所有字段信息
     */
    public List<GenTableField> getTableFieldList(DBType dbType, Connection connection, AbstractQuery query, Long dataSourceId, Long tableId, String tableName) {
        List<GenTableField> tableFieldList = new ArrayList<>();
        String tableFieldsSql = query.getTableFieldsQuerySql();
        try {
            if (dbType == DBType.ORACLE) {
                DatabaseMetaData md = connection.getMetaData();
                tableFieldsSql = String.format(tableFieldsSql.replace("#schema", md.getUserName()), tableName);
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
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
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw ServerException.create("异常", e);
        }
        return tableFieldList;
    }

    @Override
    public List<GenTable> getTableList(Long datasourceId, String tableNamePattern) {
        try (Connection connection = dataSourceService.getConnection(datasourceId)) {
            DBType dbType = DBType.MYSQL;
            if (datasourceId != 0) {
                DbConnInfo connInfo = dataSourceService.getById(datasourceId);
                if (connInfo != null) {
                    dbType = DBType.getValue(connInfo.getDbType());
                }
            }
            AbstractQuery query = dataSourceService.getQuery(dbType);
            // 根据数据源，获取全部数据表
            return getTableList(connection, datasourceId, query, tableNamePattern);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据数据源，获取全部数据表
     */
    private List<GenTable> getTableList(Connection connection, Long datasourceId, AbstractQuery query, String tableNamePattern) {
        List<GenTable> tableList = new ArrayList<>();
        String tableQuerySql = query.getTableQuerySql(null);
        try (PreparedStatement preparedStatement = connection.prepareStatement(tableQuerySql)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                // 查询数据
                while (rs.next()) {
                    String tableName = rs.getString(query.getTableNameResultSetColumnName());
                    if (StringUtils.hasText(tableNamePattern) && !tableName.contains(tableNamePattern)) {
                        continue;
                    }
                    GenTable table = new GenTable();
                    table.setTableName(tableName);
                    table.setTableComment(rs.getString(query.tableComment()));
                    table.setDatasourceId(datasourceId);
                    tableList.add(table);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return tableList;
    }
}