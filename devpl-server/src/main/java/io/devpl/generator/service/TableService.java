package io.devpl.generator.service;

import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.service.BaseService;
import io.devpl.generator.entity.GenTable;

/**
 * 数据表
 */
public interface TableService extends BaseService<GenTable> {

    PageResult<GenTable> page(Query query);

    GenTable getByTableName(String tableName);

    void deleteBatchIds(Long[] ids);

    /**
     * 导入表
     * @param datasourceId 数据源ID
     * @param tableName    表名
     */
    void importTable(Long datasourceId, String tableName);

    /**
     * 同步数据库表
     * @param id 表ID
     */
    void sync(Long id);
}
