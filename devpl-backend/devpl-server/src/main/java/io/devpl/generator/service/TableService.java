package io.devpl.generator.service;

import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.mvc.BaseService;
import io.devpl.generator.config.ConnectionInfo;
import io.devpl.generator.entity.GenTable;
import io.devpl.generator.entity.GenTableField;

import java.util.List;

/**
 * 数据表
 */
public interface TableService extends BaseService<GenTable> {

    PageResult<GenTable> page(Query query);

    GenTable getByTableName(String tableName);

    boolean deleteBatchIds(Long[] ids);

    /**
     * 导入表
     * @param datasourceId 数据源ID
     * @param tableName    表名
     */
    void importTable(Long datasourceId, String tableName);

    /**
     * 根据数据源，获取指定数据表
     * @param datasource 数据源
     * @param tableName  表名
     */
    GenTable getTable(ConnectionInfo datasource, String tableName);

    /**
     * 同步数据库表
     * @param id 表ID
     */
    void sync(Long id);

    /**
     * 获取表的所有字段
     * @param datasource 数据源信息
     * @param tableId    表ID
     * @param tableName  表名
     * @return 表的所有字段信息
     */
    List<GenTableField> getTableFieldList(ConnectionInfo datasource, Long tableId, String tableName);

    /**
     * 根据数据源，获取指定数据表
     * @param datasource 数据源
     */
    List<GenTable> getTableList(ConnectionInfo datasource);
}
