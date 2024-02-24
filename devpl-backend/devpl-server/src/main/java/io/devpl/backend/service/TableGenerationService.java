package io.devpl.backend.service;

import io.devpl.backend.common.mvc.BaseService;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.domain.param.GenTableListParam;
import io.devpl.backend.domain.param.TableImportParam;
import io.devpl.backend.entity.TableGeneration;
import io.devpl.codegen.template.TemplateArgumentsMap;

import java.util.Collection;
import java.util.List;

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

    ListResult<TableGeneration> selectPage(GenTableListParam param);

    TableGeneration getByTableName(String tableName);

    /**
     * 删除表
     *
     * @param ids 主键ID列表
     * @return 是否成功
     */
    boolean batchRemoveTablesById(Long[] ids);

    /**
     * 导入表
     */
    void importTable(TableImportParam param);

    /**
     * 导入单个表
     */
    void importSingleTable(TableImportParam param);

    /**
     * 初始化表要生成的文件
     *
     * @param table  表信息
     * @param params 模板参数
     */
    void initTargetGenerationFiles(TableGeneration table, TemplateArgumentsMap params);

    /**
     * 同步数据库表
     *
     * @param id 表ID
     */
    void sync(Long id);

    /**
     * 根据数据源，获取指定数据表
     *
     * @param datasourceId     数据源ID
     * @param databaseName     数据库名称，如果为空，则获取数据源下的所有数据库的表信息
     * @param tableNamePattern 表名，模糊匹配
     */
    List<TableGeneration> getGenerationTargetTables(Long datasourceId, String databaseName, String tableNamePattern);
}
