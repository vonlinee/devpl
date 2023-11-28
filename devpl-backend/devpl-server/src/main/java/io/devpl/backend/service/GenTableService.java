package io.devpl.backend.service;

import com.baomidou.mybatisplus.generator.engine.TemplateArgumentsMap;
import io.devpl.backend.common.mvc.BaseService;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.config.query.AbstractQuery;
import io.devpl.backend.domain.param.Query;
import io.devpl.backend.domain.param.TableImportParam;
import io.devpl.backend.entity.GenTable;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

/**
 * 数据表
 */
public interface GenTableService extends BaseService<GenTable> {

    List<GenTable> listGenTables(Collection<String> tableNames);

    List<String> listTableNames(Long dataSourceId);

    ListResult<GenTable> page(Query query);

    GenTable getByTableName(String tableName);

    boolean deleteBatchIds(Long[] ids);

    /**
     * 导入单个表
     */
    void importSingleTable(TableImportParam param);

    void initTargetGenerationFiles(GenTable table, TemplateArgumentsMap params);

    /**
     * 根据数据源，获取指定数据表
     *
     * @param connection   数据源连接
     * @param query        查询策略
     * @param dataSourceId 数据源ID
     * @param tableName    表名
     */
    GenTable getTable(Connection connection, AbstractQuery query, Long dataSourceId, String tableName);

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
     * @param tableNamePattern 表名，模糊匹配
     */
    List<GenTable> getTableList(Long datasourceId, String tableNamePattern);
}
