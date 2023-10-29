package io.devpl.generator.service;

import io.devpl.generator.common.mvc.BaseService;
import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Query;
import com.baomidou.mybatisplus.generator.jdbc.DBType;
import io.devpl.generator.config.query.AbstractQuery;
import io.devpl.generator.domain.vo.DataSourceVO;
import io.devpl.generator.entity.DbConnInfo;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.List;

/**
 * 数据源管理
 */
public interface DataSourceService extends BaseService<DbConnInfo> {

    DbConnInfo getOne(long id);

    PageResult<DbConnInfo> listPage(Query query);

    List<DbConnInfo> listAll();

    List<DataSourceVO> listIdAndNames();

    /**
     * 获取数据库产品名，如：MySQL
     *
     * @param datasourceId 数据源ID
     * @return 返回产品名
     */
    String getDatabaseProductName(Long datasourceId);

    boolean addOne(DbConnInfo connInfo);

    Connection getConnection(DbConnInfo connInfo);

    /**
     * 获取数据库连接
     *
     * @param dataSourceId 数据源ID
     * @return 数据库连接
     */
    @Nullable
    Connection getConnection(Long dataSourceId);

    AbstractQuery getQuery(DBType dbType);

    String getConnectionUrl(DbConnInfo entity);

    /**
     * 获取数据库名称
     *
     * @param entity 数据库连接信息
     * @return 数据库名称
     */
    List<String> getDbNames(DbConnInfo entity);

    List<String> getTableNames(DbConnInfo connInfo, String databaseName);

    String testJdbcConnection(Long id);

    DbConnInfo updateOne(DbConnInfo entity);
}
