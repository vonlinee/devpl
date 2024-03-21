package io.devpl.backend.service;

import io.devpl.backend.common.mvc.BaseService;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.domain.bo.TableImportInfo;
import io.devpl.backend.domain.param.GenTableListParam;
import io.devpl.backend.entity.TableGeneration;
import io.devpl.codegen.template.TemplateArgumentsMap;
import io.devpl.sdk.annotations.NotNull;
import io.devpl.sdk.annotations.Readonly;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据表
 */
public interface TableGenerationService extends BaseService<TableGeneration> {

    List<TableGeneration> listGenTables(Collection<String> tableNames);

    /**
     * 查询已导入的表名称
     *
     * @param dataSourceId 数据源ID
     * @return 已导入的表名称
     */
    List<String> listTableNames(Long dataSourceId);

    List<TableImportInfo> listImportedTables(Long dataSourceId, String databaseName, String tableName);

    /**
     * 分页列表查询
     *
     * @param param 列表查询参数
     * @return 分页列表
     */
    ListResult<TableGeneration> pageByCondition(GenTableListParam param);

    /**
     * 获取生成表信息
     *
     * @param dataSourceId 数据源 ID
     * @param databaseName 数据库名称
     * @param tableName    表名
     * @return {@link TableGeneration}
     */
    TableGeneration getGenerationTable(Long dataSourceId, String databaseName, String tableName);

    /**
     * 删除表
     *
     * @param ids 主键ID列表
     * @return 是否成功
     */
    boolean batchRemoveTablesById(Long[] ids);

    /**
     * 导入单个表
     */
    boolean importSingleTable(TableImportInfo param);

    /**
     * 初始化表要生成的文件
     *
     * @param table  表信息
     * @param params 模板参数
     */
    void initTargetGenerationFiles(TableGeneration table, TemplateArgumentsMap params);

    /**
     * 获取渲染的数据模型
     *
     * @param table 表信息
     */
    Map<String, Object> prepareDataModel(TableGeneration table);

    /**
     * 初始化表生成的模板参数
     *
     * @param table 表生成信息
     */
    @NotNull
    Map<String, Object> initTableTemplateArguments(@Readonly TableGeneration table);

    /**
     * 同步数据库表
     *
     * @param id 表ID
     */
    boolean sync(Long id);

    /**
     * 根据数据源，获取指定数据表
     *
     * @param datasourceId     数据源ID
     * @param databaseName     数据库名称，如果为空，则获取数据源下的所有数据库的表信息
     * @param tableNamePattern 表名，模糊匹配
     */
    List<TableGeneration> getGenerationTargetTables(Long datasourceId, String databaseName, String tableNamePattern);
}
