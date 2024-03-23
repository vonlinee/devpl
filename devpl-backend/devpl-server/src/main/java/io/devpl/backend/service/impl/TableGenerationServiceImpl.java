package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import io.devpl.backend.common.exception.BusinessException;
import io.devpl.backend.common.mvc.MyBatisPlusServiceImpl;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.dao.TableGenerationMapper;
import io.devpl.backend.domain.TemplateFillStrategy;
import io.devpl.backend.domain.bo.TableImportInfo;
import io.devpl.backend.domain.enums.FormLayoutEnum;
import io.devpl.backend.domain.enums.GeneratorTypeEnum;
import io.devpl.backend.domain.param.GenTableListParam;
import io.devpl.backend.entity.*;
import io.devpl.backend.service.*;
import io.devpl.backend.utils.DateTimeUtils;
import io.devpl.backend.utils.PathUtils;
import io.devpl.codegen.core.CaseFormat;
import io.devpl.codegen.db.DBType;
import io.devpl.codegen.jdbc.RuntimeSQLException;
import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import io.devpl.codegen.jdbc.meta.DatabaseMetadataLoader;
import io.devpl.codegen.jdbc.meta.PrimaryKeyMetadata;
import io.devpl.codegen.jdbc.meta.TableMetadata;
import io.devpl.codegen.template.TemplateArgumentsMap;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.common.utils.ProjectUtils;
import io.devpl.sdk.annotations.NotNull;
import io.devpl.sdk.annotations.Readonly;
import io.devpl.sdk.collection.Maps;
import io.devpl.sdk.util.ArrayUtils;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
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
    @Resource
    DataSourceService dataSourceService;
    @Resource
    TemplateService templateService;
    @Resource
    DomainModelService domainModelService;
    @Resource
    TemplateArgumentService templateArgumentService;

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

    /**
     * 查询已被导入的表信息
     *
     * @param dataSourceId 数据源 ID，精确匹配
     * @param databaseName 数据库名称，模糊匹配
     * @param tableName    表名，模糊匹配
     * @return {@link List}<{@link TableImportInfo}>
     */
    @Override
    public List<TableImportInfo> listImportedTables(@NotNull Long dataSourceId, @Nullable String databaseName, @Nullable String tableName) {
        return baseMapper.selectImportedTableList(dataSourceId, databaseName, tableName);
    }

    /**
     * 分页列表查询
     *
     * @param query 列表查询参数
     * @return 分页列表
     */
    @Override
    public ListResult<TableGeneration> pageByCondition(GenTableListParam query) {
        return ListResult.ok(baseMapper.selectListByCondition(query));
    }

    /**
     * 获取生成表
     *
     * @param dataSourceId 数据源 ID
     * @param databaseName 数据库名称
     * @param tableName    表名
     * @return {@link TableGeneration}
     */
    @Override
    public TableGeneration getGenerationTable(Long dataSourceId, String databaseName, String tableName) {
        return baseMapper.selectOne(dataSourceId, databaseName, tableName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchRemoveTablesById(Long[] ids) {
        // 删除表
        baseMapper.deleteBatchIds(Arrays.asList(ids));
        // 删除列
        tableFieldService.deleteBatchByTableIds(ids);
        // 删除生成的文件信息
        return tableFileGenerationService.removeByTableIds(ids, false);
    }

    /**
     * 导入单个表
     *
     * @param param TableImportParam
     */
    @Override
    public boolean importSingleTable(TableImportInfo param) {
        String tableName = param.getTableName();
        Long datasourceId = param.getDataSourceId();
        // 根据项目信息，生成表生成的相关信息，可选
        ProjectInfo projectInfo = null;
        if (param.getProjectId() != null) {
            projectInfo = projectService.getById(param.getProjectId());
        }
        try (Connection connection = rdbmsConnectionInfoService.getConnection(datasourceId, null)) {
            // 从数据库获取表信息
            DatabaseMetadataLoader loader = dataSourceService.getDatabaseMetadataLoader(connection, param.getDbType());
            List<TableGeneration> tables = prepareTables(loader.getTables(null, null, tableName, null));
            // 查询表是否存在
            TableGeneration table = null;
            for (TableGeneration tg : tables) {
                if (Objects.equals(tg.getTableName(), tableName)) {
                    table = tg;
                    break;
                }
            }
            if (table == null) {
                return false;
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

            // 加载所有字段信息
            List<TableGenerationField> tableFieldList = this.loadTableGenerationFields(loader, param.getDbType(), connection, table);
            if (!CollectionUtils.isEmpty(tableFieldList)) {
                // 初始化字段数据并保存列数据
                tableFieldService.saveBatch(tableFieldList);
                table.setFieldList(tableFieldList);
            }

            Map<String, Object> dataModel = this.initTableTemplateArguments(table);

            table.setTemplateArguments(dataModel);

            updateById(table);
        } catch (SQLException exception) {
            throw new RuntimeSQLException(exception);
        }
        return true;
    }

    /**
     * 根据数据表的元数据初始化表生成配置数据
     *
     * @param tablesMetadata 数据表元数据
     * @return 表生成配置数据
     */
    private List<TableGeneration> prepareTables(List<TableMetadata> tablesMetadata) {
        List<TableGeneration> tableList = new ArrayList<>();
        for (TableMetadata tm : tablesMetadata) {
            TableGeneration tableGeneration = new TableGeneration();
            tableGeneration.setTableName(tm.getTableName());
            tableGeneration.setTableComment(tm.getRemarks());
            tableGeneration.setDatabaseName(tm.getTableSchema());
            tableGeneration.setTableType(tm.getTableType());
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

        List<TemplateFileGeneration> templateFileGenerations = new ArrayList<>();

        IdentityHashMap<TableFileGeneration, TemplateFileGeneration> map = new IdentityHashMap<>();

        Map<Long, String> templateIdNameMap = templateService.listIdAndNameMapByIds(CollectionUtils.toSet(targetGenFiles, TargetGenerationFile::getTemplateId));

        TableInfo tableInfo = TableInfoHelper.getTableInfo(TableFileGeneration.class);

        for (TargetGenerationFile targetGenFile : targetGenFiles) {
            TableFileGeneration tableFileGen = new TableFileGeneration();
            tableFileGen.setTableId(table.getId());
            // 需替换参数变量
            if (StringUtils.hasText(targetGenFile.getFileName())) {
                tableFileGen.setFileName(targetGenFile.getFileName());
            }
            if (StringUtils.hasText(targetGenFile.getSavePath())) {
                tableFileGen.setSavePath(targetGenFile.getSavePath());
            }
            list.add(tableFileGen);

            // 模板文件生成信息
            TemplateFileGeneration templateFileGen = new TemplateFileGeneration();
            templateFileGen.setDataFillStrategy(TemplateFillStrategy.DB_TABLE.getId());
            templateFileGen.setBuiltin(false);
            templateFileGen.setTemplateId(targetGenFile.getTemplateId());
            templateFileGen.setTemplateName(templateIdNameMap.get(targetGenFile.getTemplateId()));
            templateFileGen.setConfigTableName(tableInfo.getTableName());

            if (StringUtils.hasText(targetGenFile.getFileName())) {
                templateFileGen.setFileName(templateEngine.evaluate(targetGenFile.getFileName(), params));
            }
            if (StringUtils.hasText(targetGenFile.getSavePath())) {
                templateFileGen.setSavePath(PathUtils.toRelative(templateEngine.evaluate(targetGenFile.getSavePath(), params)));
            }

            map.put(tableFileGen, templateFileGen);
            templateFileGenerations.add(templateFileGen);
        }

        templateFileGenerationService.saveBatch(templateFileGenerations);
        // 关联ID
        for (TableFileGeneration tfg : list) {
            TemplateFileGeneration templateFileGeneration = map.get(tfg);
            if (templateFileGeneration != null) {
                tfg.setGenerationId(templateFileGeneration.getId());
            }
        }
        tableFileGenerationService.saveBatch(list);
        table.setGenerationFiles(list);

        for (TableFileGeneration tfg : list) {
            TemplateFileGeneration templateFileGeneration = map.get(tfg);
            if (templateFileGeneration != null) {
                templateFileGeneration.setConfigTableId(tfg.getId());
            }
        }
        templateFileGenerationService.updateBatchById(templateFileGenerations);
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
     * 获取单个表渲染的数据模型
     *
     * @param table 表信息
     */
    @Override
    @NotNull
    public Map<String, Object> prepareDataModel(@Readonly TableGeneration table) {
        // 数据模型
        final Map<String, Object> dataModel = new HashMap<>();
        // 获取数据库类型
        dataModel.put("entity", table.getClassName());
        // 包名配置
        Map<String, Object> packageConfig = new HashMap<>();
        packageConfig.put("Controller", table.getPackageName() + ".controller");
        packageConfig.put("Mapper", table.getPackageName() + ".mapper");
        packageConfig.put("Service", table.getPackageName() + ".service");
        packageConfig.put("Entity", table.getPackageName() + ".entity");
        packageConfig.put("ServiceImpl", table.getPackageName() + ".service.impl");

        dataModel.put("package", packageConfig);
        // 表配置信息
        Map<String, Object> tableConfig = new HashMap<>();
        tableConfig.put("entityPath", table.getTableName());
        tableConfig.put("comment", table.getTableComment());
        tableConfig.put("name", table.getTableName());

        tableConfig.put("controllerName", table.getClassName() + "Controller");
        tableConfig.put("serviceName", table.getClassName() + "Service");
        tableConfig.put("serviceImplName", table.getClassName() + "ServiceImpl");
        tableConfig.put("mapperName", table.getClassName() + "Mapper");
        dataModel.put("table", tableConfig);

        dataModel.put("superServiceClass", "IService");
        dataModel.put("superMapperClass", "BaseMapper");
        dataModel.put("superMapperClassPackage", "com.baomidou.mybatisplus.core.mapper.BaseMapper");
        dataModel.put("superServiceImplClassPackage", "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
        dataModel.put("superServiceImplClass", "ServiceImpl");
        dataModel.put("superServiceClassPackage", "com.baomidou.mybatisplus.extension.service.IService");

        // 项目信息
        // 包路径
        dataModel.put("packagePath", table.getPackageName().replace(".", File.separator));
        dataModel.put("version", table.getVersion());
        dataModel.put("moduleName", table.getModuleName());
        dataModel.put("ModuleName", StringUtils.upperFirst(ProjectUtils.toSimpleIdentifier(table.getModuleName())));
        dataModel.put("functionName", table.getFunctionName());
        dataModel.put("FunctionName", StringUtils.upperFirst(table.getFunctionName()));
        dataModel.put("formLayout", table.getFormLayout());

        // 开发者信息
        dataModel.put("author", table.getAuthor());
        dataModel.put("email", table.getEmail());
        dataModel.put("datetime", DateTimeUtils.stringOfNow());
        dataModel.put("date", DateTimeUtils.nowDateString());

        // 设置字段分类
        setFieldTypeList(dataModel, table);
        // 设置基类信息
        setBaseClass(dataModel, table);
        // 导入的包列表
        Set<String> importList = new HashSet<>();
        dataModel.put("importList", importList);

        // 表信息
        dataModel.put("tableName", table.getTableName());
        dataModel.put("tableComment", table.getTableComment());
        dataModel.put("className", StringUtils.lowerFirst(table.getClassName()));
        dataModel.put("ClassName", table.getClassName());
        dataModel.put("fieldList", table.getFieldList());

        // 前后端生成路径
        dataModel.put("backendPath", table.getBackendPath());
        dataModel.put("frontendPath", table.getFrontendPath());
        return dataModel;
    }

    /**
     * 初始化表的模板参数
     *
     * @param table 表生成配置
     * @return 参数列表
     */
    @Override
    public Map<String, Object> initTableTemplateArguments(@Readonly TableGeneration table) {
        // 单个表数据模型
        Map<String, Object> dataModel = this.prepareDataModel(table);
        // 全局模板参数
        for (TemplateParam param : templateParamService.getGlobalTemplateParams()) {
            Object val = dataModel.get(param.getParamKey());
            if (val != null) {
                // 覆盖
            } else {
                dataModel.put(param.getParamKey(), param.getParamValue());
            }
        }
        return dataModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sync(Long id) {
        TableGeneration table = this.getById(id);

        RdbmsConnectionInfo connInfo = rdbmsConnectionInfoService.getConnectionInfo(table.getDatasourceId());
        DBType dbType = DBType.getValue(connInfo.getDbType());

        List<TableGenerationField> tableFields;
        try (Connection connection = rdbmsConnectionInfoService.getRdbmsConnection(connInfo)) {
            tableFields = this.loadTableGenerationFields(null, dbType, connection, table);
        } catch (Exception exception) {
            throw BusinessException.create("同步失败，请检查数据库表：%s", table.getTableName());
        }
        // 从数据库获取表字段列表
        if (tableFields.isEmpty()) {
            throw BusinessException.create("同步失败，请检查数据库表：%s", table.getTableName());
        }
        // 表字段列表
        List<TableGenerationField> tableFieldList = tableFieldService.listByTableId(id);
        Map<String, TableGenerationField> tableFieldMap = CollectionUtils.toMap(tableFieldList, TableGenerationField::getFieldName);
        // 初始化字段数据 同步表结构字段
        tableFieldService.initTableFields(table, tableFields).forEach(field -> {
            // 新增字段
            if (!tableFieldMap.containsKey(field.getFieldName())) {
                tableFieldService.save(field);
                return;
            }
            // 修改字段
            TableGenerationField tgf = tableFieldMap.get(field.getFieldName());
            tgf.setPrimaryKey(field.isPrimaryKey());
            tgf.setFieldComment(field.getFieldComment());
            tgf.setFieldType(field.getFieldType());
            tgf.setAttrType(field.getAttrType());
            tableFieldService.updateById(tgf);
        });

        // 删除数据库表中没有的字段
        List<String> dbTableFieldNameList = CollectionUtils.toList(tableFields, TableGenerationField::getFieldName);
        List<TableGenerationField> delFieldList = CollectionUtils.filter(tableFieldList, field -> !dbTableFieldNameList.contains(field.getFieldName()));

        if (!delFieldList.isEmpty()) {
            tableFieldService.removeBatchByIds(CollectionUtils.toList(delFieldList, TableGenerationField::getId));
        }
        return true;
    }

    /**
     * 读取数据源元数据，获取表的所有字段
     *
     * @param dbType     数据库类型
     * @param table      表信息
     * @param loader     元数据加载类
     * @param connection 连接
     * @return 表的所有字段信息
     */
    private List<TableGenerationField> loadTableGenerationFields(DatabaseMetadataLoader loader, DBType dbType, Connection connection, TableGeneration table) {
        List<TableGenerationField> tableFieldList = new ArrayList<>();
        try {
            if (loader == null) {
                loader = dataSourceService.getDatabaseMetadataLoader(connection, dbType);
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
    public List<TableGeneration> listGenerationTargetTables(Long datasourceId, String databaseName, String tableNamePattern) {
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
            try (DatabaseMetadataLoader loader = dataSourceService.getDatabaseMetadataLoader(connection, dbType)) {
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


    /**
     * 设置字段分类信息
     *
     * @param dataModel 数据模型
     * @param table     表
     */
    private void setFieldTypeList(Map<String, Object> dataModel, TableGeneration table) {
        // 主键列表 (支持多主键)
        List<TableGenerationField> primaryList = new ArrayList<>();
        // 表单列表
        List<TableGenerationField> formList = new ArrayList<>();
        // 网格列表
        List<TableGenerationField> gridList = new ArrayList<>();
        // 查询列表
        List<TableGenerationField> queryList = new ArrayList<>();
        for (TableGenerationField field : table.getFieldList()) {
            if (field.isPrimaryKey()) {
                primaryList.add(field);
            }
            if (field.isFormItem()) {
                formList.add(field);
            }
            if (field.isGridItem()) {
                gridList.add(field);
            }
            if (field.isQueryItem()) {
                queryList.add(field);
            }
        }
        dataModel.put("primaryList", primaryList);
        dataModel.put("formList", formList);
        dataModel.put("gridList", gridList);
        dataModel.put("queryList", queryList);
    }


    /**
     * 设置基类信息
     *
     * @param dataModel 数据模型
     * @param table     表
     */
    private void setBaseClass(Map<String, Object> dataModel, TableGeneration table) {
        if (table.getBaseclassId() == null) {
            return;
        }
        // 基类
        ModelInfo baseClass = domainModelService.getById(table.getBaseclassId());
        baseClass.setPackageName(baseClass.getPackageName());
        dataModel.put("baseClass", baseClass);
        // 基类字段
        String[] fields = baseClass.getFieldsName().split(",");
        // 标注为基类字段
        for (TableGenerationField field : table.getFieldList()) {
            if (ArrayUtils.contains(fields, field.getFieldName())) {
                field.setBaseField(true);
            }
        }
    }
}
