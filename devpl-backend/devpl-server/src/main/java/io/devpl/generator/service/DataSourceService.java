package io.devpl.generator.service;

import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.mvc.BaseService;
import io.devpl.generator.config.ConnectionInfo;
import io.devpl.generator.entity.DataSourceInfo;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.List;

/**
 * 数据源管理
 */
public interface DataSourceService extends BaseService<DataSourceInfo> {

    PageResult<DataSourceInfo> page(Query query);

    List<DataSourceInfo> getList();

    /**
     * 获取数据库产品名，如：MySQL
     * @param datasourceId 数据源ID
     * @return 返回产品名
     */
    String getDatabaseProductName(Long datasourceId);

    /**
     * 根据数据源ID，获取数据源
     * @param datasourceId 数据源ID
     */
    @Nullable
    ConnectionInfo findById(Long datasourceId);

    /**
     * 获取数据库连接
     * @param dataSourceId 数据源ID
     * @return 数据库连接
     */
    @Nullable
    Connection getConnection(Long dataSourceId);

    List<String> getDbNames(DataSourceInfo entity);
}
