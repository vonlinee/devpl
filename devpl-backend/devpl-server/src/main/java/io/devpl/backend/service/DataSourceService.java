package io.devpl.backend.service;

import io.devpl.backend.common.mvc.BaseService;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.config.query.AbstractQuery;
import io.devpl.backend.domain.param.DBTableDataParam;
import io.devpl.backend.domain.param.DbConnInfoListParam;
import io.devpl.backend.domain.vo.DBTableDataVO;
import io.devpl.backend.domain.vo.DataSourceVO;
import io.devpl.backend.domain.vo.TestConnVO;
import io.devpl.backend.entity.RdbmsConnectionInfo;
import io.devpl.codegen.db.DBType;
import io.devpl.codegen.jdbc.meta.ColumnMetadata;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据源管理
 */
public interface DataSourceService extends BaseService<RdbmsConnectionInfo> {

    /**
     * 数据源ID是否是系统自身数据源
     *
     * @param id 数据源ID
     * @return 是否是系统数据源
     */
    boolean isSystemDataSource(Long id);

    /**
     * 获取数据库连接信息
     * 对一些加密的参数进行解密
     *
     * @param id 数据源ID
     * @return 数据库连接信息
     */
    RdbmsConnectionInfo getConnectionInfo(long id);

    ListResult<RdbmsConnectionInfo> listPage(DbConnInfoListParam param);

    List<RdbmsConnectionInfo> listAll();

    List<DataSourceVO> listIdAndNames();

    /**
     * 获取数据库产品名，如：MySQL
     *
     * @param datasourceId 数据源ID
     * @return 返回产品名
     */
    String getDatabaseProductName(Long datasourceId);

    boolean addOne(RdbmsConnectionInfo connInfo);

    /**
     * 获取数据库连接
     *
     * @param connInfo 连接信息
     * @return 数据库连接
     * @throws SQLException SQLException
     */
    Connection getConnection(RdbmsConnectionInfo connInfo) throws SQLException;

    /**
     * 获取数据库连接
     *
     * @param dataSourceId 数据源ID
     * @return 数据库连接
     */
    Connection getConnection(Long dataSourceId) throws SQLException;

    AbstractQuery getQuery(DBType dbType);

    String getConnectionUrl(RdbmsConnectionInfo entity);

    /**
     * 获取数据库名称
     *
     * @param entity 数据库连接信息
     * @return 数据库名称
     */
    List<String> getDbNames(RdbmsConnectionInfo entity);

    /**
     * 获取数据库名称
     *
     * @param dataSourceId 数据源ID
     * @return 数据库名称
     */
    List<String> getDatabaseNames(Long dataSourceId);

    List<String> getTableNames(RdbmsConnectionInfo connInfo, String databaseName);

    List<ColumnMetadata> getColumns(RdbmsConnectionInfo connInfo, String databaseName, String tableName);

    /**
     * 测试数据源连接
     *
     * @param id 数据源ID
     * @return 数据源连接信息
     */
    TestConnVO testJdbcConnection(Long id);

    TestConnVO testJdbcConnection(RdbmsConnectionInfo connInfo);

    RdbmsConnectionInfo updateOne(RdbmsConnectionInfo entity);

    DBTableDataVO getTableData(DBTableDataParam param);
}
