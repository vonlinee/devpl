package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.devpl.backend.common.exception.BusinessException;
import io.devpl.backend.common.mvc.MyBatisPlusServiceImpl;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.dao.TableGenerationMapper;
import io.devpl.backend.domain.TemplateFillStrategy;
import io.devpl.backend.domain.enums.FormLayoutEnum;
import io.devpl.backend.domain.enums.GeneratorTypeEnum;
import io.devpl.backend.domain.param.GenTableListParam;
import io.devpl.backend.domain.param.TableImportParam;
import io.devpl.backend.entity.*;
import io.devpl.backend.service.*;
import io.devpl.backend.utils.PathUtils;
import io.devpl.common.utils.ProjectUtils;
import io.devpl.codegen.core.CaseFormat;
import io.devpl.codegen.db.DBType;
import io.devpl.codegen.db.query.AbstractQueryDatabaseMetadataLoader;
import io.devpl.codegen.jdbc.JdbcDatabaseMetadataLoader;
import io.devpl.codegen.jdbc.RuntimeSQLException;
import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import io.devpl.codegen.jdbc.meta.DatabaseMetadataLoader;
import io.devpl.codegen.jdbc.meta.PrimaryKeyMetadata;
import io.devpl.codegen.jdbc.meta.TableMetadata;
import io.devpl.codegen.template.TemplateArgumentsMap;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.sdk.collection.Lists;
import io.devpl.sdk.collection.Maps;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 表生成 Service
 */
@Slf4j
@Service
public class TableGenerationServiceImpl extends MyBatisPlusServiceImpl<TableGenerationMapper, TableGeneration> implements TableGenerationService {
    @Resource
    TableGenerationFieldService tableFieldService;
    @Resource
    RdbmsConnectionInfoService rdbmsConnectionInfoService;
    @Resource
    TargetGenerationFileService targetGenFileService;
    @Resource
    TableFileGenerationService tableFileGenerationService;
    @Resource
    ProjectService projectService;
    @Resource
    TemplateFileGenerationService templateFileGenerationService;
    @Resource
    TemplateParamService templateParamService;
    @Resource
    TemplateEngine templateEngine;

    @Override
    public List<TableGeneration> listGenTables(Collection<String> tableNames) {
        if (tableNames == null || tableNames.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TableGeneration> qw = new LambdaQueryWrapper<>();
        qw.in(TableGeneration::getTableName, tableNames);
        return list(qw);
    }

    @Override
    public List<String> listTableNames(Long dataSourceId) {
        return baseMapper.selectTableNames(dataSourceId);
    }

    @Override
    public ListResult<TableGeneration> selectPage(GenTableListParam query) {
        return ListResult.ok(baseMapper.selectPage(query,
            new LambdaQueryWrapper<TableGeneration>().eq(StringUtils.hasText(query.getTableName()), TableGeneration::getTableName, query.getTableName())));
    }

    @Override
    public TableGeneration getByTableName(String tableName) {
        return baseMapper.selectOneByTableName(tableName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchRemoveTablesById(Long[] ids) {
        // 删除表
        baseMapper.deleteBatchIds(Arrays.asList(ids));
        // 删除列
        tableFieldService.deleteBatchTableIds(ids);
        // 删除生成的文件信息
        return tableFileGenerationService.removeByTableIds(ids, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importTable(TableImportParam param) {
        if (!CollectionUtils.isEmpty(param.getTableNameList())) {
            // 已经导入的表名
            List<String> tableNamesImported = this.listTableNames(param.getDataSourceId());
            param.getTableNameList().removeAll(tableNamesImported);
            for (String tableName : param.getTableNameList()) {
                param.setTableName(tableName);
                this.importSingleTable(param);
            }
        } else {
            this.importSingleTable(param);
        }
    }

    /**
     * 导入单个表
     *
     * @param param TableImportParam
     */
    @Override
    public void importSingleTable(TableImportParam param) {
        String tableName = param.getTableName();
        Long datasourceId = param.getDataSourceId();
        // 查询表是否存在
        TableGeneration table = this.getByTableName(tableName);
        // 表存在
        if (table != null) {
            return;
        }

        // 根据项目信息，生成表生成的相关信息，可选
        ProjectInfo projectInfo = null;
        if (param.getProjectId() != null) {
            projectInfo = projectService.getById(param.getProjectId());
        }

        try (Connection connection = rdbmsConnectionInfoService.getConnection(datasourceId, null)) {
            // 从数据库获取表信息

            DatabaseMetadataLoader loader = getDatabaseMetadataLoader(connection, param.getDbType());
            List<TableGeneration> tables = prepareTables(loader.getTables(null, null, tableName, null));
            table = Lists.first(tables, t -> Objects.equals(t.getTableName(), tableName));
            if (table == null) {
                return;
            }

            table.setDatasourceId(datasourceId);

            // 保存表信息
            // 项目信息
            Map<String, String> globalTemplateParamsMap = templateParamService.getGlobalTemplateParamValuesMap();

            table.setAuthor(Maps.get(globalTemplateParamsMap, "author"));
            table.setEmail(Maps.get(globalTemplateParamsMap, "email"));

            table.setClassName(CaseFormat.toPascalCase(tableName));
            // 获取模块名
            table.setModuleName(StringUtils.subAfter(table.getPackageName(), ".", true));
            table.setFunctionName(getFunctionName(tableName));

            // 默认初始值
            table.setFormLayout(FormLayoutEnum.ONE.getValue());
            table.setGeneratorType(GeneratorTypeEnum.CUSTOM_DIRECTORY.ordinal());

            table.setCreateTime(LocalDateTime.now());
            table.setUpdateTime(table.getCreateTime());

            TemplateArgumentsMap params = new TemplateArgumentsMap();
            if (projectInfo != null) {
                table.setPackageName(projectInfo.getProjectPackage());
                table.setVersion(projectInfo.getVersion());
                table.setBackendPath(projectInfo.getBackendPath());
                table.setFrontendPath(projectInfo.getFrontendPath());
                table.setModuleName(projectInfo.getProjectName());
                table.setVersion(projectInfo.getVersion());
                params.setValue("moduleName", ProjectUtils.toSimpleIdentifier(projectInfo.getProjectName()));
            }

            // 尝试填充全局模板参数
            if (!StringUtils.hasText(table.getPackageName())) {
                table.setPackageName(Maps.get(globalTemplateParamsMap, "packageName"));
            }
            if (!StringUtils.hasText(table.getBackendPath())) {
                table.setBackendPath(Maps.get(globalTemplateParamsMap, "backendPath"));
            }
            if (!StringUtils.hasText(table.getFrontendPath())) {
                table.setFrontendPath(Maps.get(globalTemplateParamsMap, "frontendPath"));
            }

            params.setValue("backendPath", table.getBackendPath());
            params.setValue("frontendPath", table.getFrontendPath());
            params.setValue("tableName", table.getTableName()); // 必填
            params.setValue("packagePath", StringUtils.replace(table.getPackageName(), ".", "/"));
            params.setValue("ClassName", table.getClassName());

            this.save(table);

            // 初始化该表需要生成的文件列表
            this.initTargetGenerationFiles(table, params);

            List<TableGenerationField> tableFieldList = this.loadTableGenerationFields(loader, param.getDbType(), connection, table);
            // 初始化字段数据并保存列数据
            tableFieldService.saveBatch(tableFieldList);
        } catch (SQLException exception) {
            log.error("导入表失败", exception);
            throw new RuntimeSQLException(exception);
        }
    }

    private List<TableGeneration> prepareTables(List<TableMetadata> tablesMetadata) {
        List<TableGeneration> tableList = new ArrayList<>();
        for (TableMetadata tm : tablesMetadata) {
            TableGeneration tableGeneration = new TableGeneration();
            tableGeneration.setTableName(tm.getTableName());
            tableGeneration.setTableComment(tm.getRemarks());
            tableGeneration.setDatabaseName(tm.getTableSchem());
            tableList.add(tableGeneration);
        }
        return tableList;
    }

    /**
     * 初始化要生成的文件信息
     *
     * @param table 要生成的数据库表
     */
    @Override
    public void initTargetGenerationFiles(TableGeneration table, TemplateArgumentsMap params) {
        List<TargetGenerationFile> targetGenFiles = targetGenFileService.listDefaultGeneratedFileTypes();
        List<TableFileGeneration> list = new ArrayList<>();

        for (TargetGenerationFile targetGenFile : targetGenFiles) {

            TemplateFileGeneration templateFileGen = new TemplateFileGeneration();
            templateFileGen.setTemplateId(targetGenFile.getTemplateId());
            templateFileGen.setDataFillStrategy(TemplateFillStrategy.DB_TABLE.getId());
            templateFileGen.setBuiltin(false);
            templateFileGen.setTemplateName(targetGenFile.getTemplateName());
            templateFileGenerationService.save(templateFileGen);

            TableFileGeneration tableFileGen = new TableFileGeneration();
            tableFileGen.setTableId(table.getId());
            tableFileGen.setGenerationId(templateFileGen.getId());

            // 需替换参数变量
            if (StringUtils.hasText(targetGenFile.getFileName())) {
                tableFileGen.setFileName(templateEngine.evaluate(targetGenFile.getFileName(), params));
            }
            if (StringUtils.hasText(targetGenFile.getSavePath())) {
                tableFileGen.setSavePath(PathUtils.toRelative(templateEngine.evaluate(targetGenFile.getSavePath(), params)));
            }
            list.add(tableFileGen);
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


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sync(Long id) {
        TableGeneration table = this.getById(id);

        RdbmsConnectionInfo connInfo = rdbmsConnectionInfoService.getConnectionInfo(table.getDatasourceId());
        DBType dbType = DBType.getValue(connInfo.getDbType());

        List<TableGenerationField> dbTableFieldList;
        try (Connection connection = rdbmsConnectionInfoService.getRdbmsConnection(connInfo)) {
            dbTableFieldList = this.loadTableGenerationFields(null, dbType, connection, table);
        } catch (Exception exception) {
            log.error("同步数据库表失败{}", table.getTableName());
            throw new BusinessException("同步失败，请检查数据库表：" + table.getTableName());
        }
        // 从数据库获取表字段列表
        if (dbTableFieldList.isEmpty()) {
            throw new BusinessException("同步失败，请检查数据库表：" + table.getTableName());
        }

        // 表字段列表
        List<TableGenerationField> tableFieldList = tableFieldService.listByTableId(id);

        Map<String, TableGenerationField> tableFieldMap = CollectionUtils.toMap(tableFieldList, TableGenerationField::getFieldName);

        // 初始化字段数据 同步表结构字段
        tableFieldService.initTableFields(table, dbTableFieldList).forEach(field -> {
            // 新增字段
            if (!tableFieldMap.containsKey(field.getFieldName())) {
                tableFieldService.save(field);
                return;
            }
            // 修改字段
            TableGenerationField updateField = tableFieldMap.get(field.getFieldName());
            updateField.setPrimaryKey(field.isPrimaryKey());
            updateField.setFieldComment(field.getFieldComment());
            updateField.setFieldType(field.getFieldType());
            updateField.setAttrType(field.getAttrType());

            tableFieldService.updateById(updateField);
        });

        // 删除数据库表中没有的字段
        List<String> dbTableFieldNameList = CollectionUtils.toList(dbTableFieldList, TableGenerationField::getFieldName);
        List<TableGenerationField> delFieldList = CollectionUtils.filter(tableFieldList, field -> !dbTableFieldNameList.contains(field.getFieldName()));

        if (!delFieldList.isEmpty()) {
            tableFieldService.removeBatchByIds(CollectionUtils.toList(delFieldList, TableGenerationField::getId));
        }
    }


    /**
     * 获取表的所有字段
     *
     * @param dbType 数据库类型
     * @param table  表信息
     * @return 表的所有字段信息
     */
    private List<TableGenerationField> loadTableGenerationFields(DatabaseMetadataLoader loader, DBType dbType, Connection connection, TableGeneration table) {
        List<TableGenerationField> tableFieldList = new ArrayList<>();
        try {
            if (loader == null) {
                loader = getDatabaseMetadataLoader(connection, dbType);
            }
            List<ColumnMetadata> columns = loader.getColumns(null, table.getDatabaseName(), table.getTableName(), null);
            // 获取主键数据
            List<PrimaryKeyMetadata> primaryKeys = loader.getPrimaryKeys(null, null, table.getTableName());
            Map<String, PrimaryKeyMetadata> primaryKeyMetadataMap = CollectionUtils.toMap(primaryKeys, PrimaryKeyMetadata::getColumnName);
            for (ColumnMetadata column : columns) {
                TableGenerationField tgf = new TableGenerationField();
                tgf.setFieldName(column.getColumnName());
                tgf.setFieldType(column.getPlatformDataType());
                tgf.setFieldComment(column.getRemarks());
                tgf.setPrimaryKey(primaryKeyMetadataMap.containsKey(column.getColumnName()));

                tableFieldList.add(tgf);
            }

            tableFieldList = tableFieldService.initTableFields(table, tableFieldList);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return tableFieldList;
    }

    /**
     * 获取数据源的表信息列表
     *
     * @param datasourceId     数据源ID
     * @param databaseName     数据库名称，如果为空，则获取数据源下的所有数据库的表信息
     * @param tableNamePattern 表名，模糊匹配
     * @return 表信息列表
     */
    @Override
    public List<TableGeneration> getGenerationTargetTables(Long datasourceId, String databaseName, String tableNamePattern) {
        List<TableGeneration> tableList = new ArrayList<>();
        DBType dbType = DBType.MYSQL;
        if (!rdbmsConnectionInfoService.isSystemDataSource(datasourceId)) {
            RdbmsConnectionInfo connInfo = rdbmsConnectionInfoService.getById(datasourceId);
            if (connInfo != null) {
                dbType = DBType.getValue(connInfo.getDbType());
            }
        }

        try (Connection connection = rdbmsConnectionInfoService.getConnection(datasourceId, databaseName, StringUtils.hasLength(databaseName))) {
            // 根据数据源，获取全部数据表
            try (DatabaseMetadataLoader loader = getDatabaseMetadataLoader(connection, dbType)) {
                List<TableMetadata> tables = loader.getTables(null, databaseName, tableNamePattern, null);
                List<TableGeneration> tgs = this.prepareTables(tables);
                for (TableGeneration tg : tgs) {
                    tg.setDatasourceId(datasourceId);
                    tableList.add(tg);
                }
            }
        } catch (Exception e) {
            throw RuntimeSQLException.wrap(e);
        }
        return tableList;
    }

    private DatabaseMetadataLoader getDatabaseMetadataLoader(Connection connection, DBType dbType) {
        if (dbType == null) {
            return new JdbcDatabaseMetadataLoader(connection);
        }
        return new AbstractQueryDatabaseMetadataLoader(connection, dbType);
    }
}
