package io.devpl.generator.service;

import com.baomidou.mybatisplus.generator.jdbc.DBType;
import com.baomidou.mybatisplus.generator.jdbc.meta.ColumnMetadata;
import io.devpl.generator.common.mvc.BaseService;
import io.devpl.generator.common.query.ListResult;
import io.devpl.generator.config.query.AbstractQuery;
import io.devpl.generator.domain.param.DBTableDataParam;
import io.devpl.generator.domain.param.DbConnInfoListParam;
import io.devpl.generator.domain.vo.DBTableDataVO;
import io.devpl.generator.domain.vo.DataSourceVO;
import io.devpl.generator.domain.vo.TestConnVO;
import io.devpl.generator.entity.DbConnInfo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据源管理
 */
public interface DataSourceService extends BaseService<DbConnInfo> {

    boolean isSystemDataSource(Long id);

    DbConnInfo getOne(long id);

    ListResult<DbConnInfo> listPage(DbConnInfoListParam param);

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

    /**
     * 获取数据库连接
     *
     * @param connInfo 连接信息
     * @return 数据库连接
     * @throws SQLException SQLException
     */
    Connection getConnection(DbConnInfo connInfo) throws SQLException;

    /**
     * 获取数据库连接
     *
     * @param dataSourceId 数据源ID
     * @return 数据库连接
     */
    Connection getConnection(Long dataSourceId) throws SQLException;

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

    List<ColumnMetadata> getColumns(DbConnInfo connInfo, String databaseName, String tableName);

    TestConnVO testJdbcConnection(Long id);

    TestConnVO testJdbcConnection(DbConnInfo connInfo);

    DbConnInfo updateOne(DbConnInfo entity);

    DBTableDataVO getTableData(DBTableDataParam param);
}
