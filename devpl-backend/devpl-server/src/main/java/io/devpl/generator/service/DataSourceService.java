package io.devpl.generator.service;

import io.devpl.generator.common.mvc.BaseService;
import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.config.DbType;
import io.devpl.generator.config.query.AbstractQuery;
import io.devpl.generator.domain.vo.DataSourceVO;
import io.devpl.generator.entity.JdbcConnInfo;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.List;

/**
 * 数据源管理
 */
public interface DataSourceService extends BaseService<JdbcConnInfo> {

    PageResult<JdbcConnInfo> page(Query query);

    List<JdbcConnInfo> getList();

    List<DataSourceVO> listIdAndNames();

    /**
     * 获取数据库产品名，如：MySQL
     *
     * @param datasourceId 数据源ID
     * @return 返回产品名
     */
    String getDatabaseProductName(Long datasourceId);

    Connection getConnection(JdbcConnInfo connInfo);

    /**
     * 获取数据库连接
     *
     * @param dataSourceId 数据源ID
     * @return 数据库连接
     */
    @Nullable
    Connection getConnection(Long dataSourceId);

    AbstractQuery getQuery(DbType dbType);

    /**
     * 获取数据库名称
     *
     * @param entity 数据库连接信息
     * @return 数据库名称
     */
    List<String> getDbNames(JdbcConnInfo entity);

    String testJdbcConnection(Long id);
}
