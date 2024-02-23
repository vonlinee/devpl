package io.devpl.backend.service;

import io.devpl.backend.common.mvc.BaseService;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.domain.param.DBTableDataParam;
import io.devpl.backend.domain.param.DbConnInfoListParam;
import io.devpl.backend.domain.vo.DBTableDataVO;
import io.devpl.backend.domain.vo.DataSourceVO;
import io.devpl.backend.domain.vo.TestConnVO;
import io.devpl.backend.entity.RdbmsConnectionInfo;
import io.devpl.codegen.jdbc.RuntimeSQLException;
import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
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
     * @param dataSourceId 数据源ID
     * @return 数据库连接信息
     */
    RdbmsConnectionInfo getConnectionInfo(Long dataSourceId);

    long getSystemDataSourceId();

    RdbmsConnectionInfo getInternalConnectionInfo();

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
     * @throws RuntimeSQLException RuntimeSQLException
     */
    Connection getRdbmsConnection(RdbmsConnectionInfo connInfo) throws RuntimeSQLException;

    /**
     * 获取数据库连接
     *
     * @param dataSourceId 数据源ID
     * @return 数据库连接
     */
    default Connection getConnection(Long dataSourceId) throws RuntimeSQLException {
        return getConnection(dataSourceId, null, true);
    }

    /**
     * 获取数据库连接
     *
     * @param dataSourceId 数据源ID
     * @param databaseName 数据库名称，如果不为空，则获取该数据源下指定数据库名称的连接
     * @return 数据库连接
     */
    default Connection getConnection(Long dataSourceId, @Nullable String databaseName) throws RuntimeSQLException {
        return getConnection(dataSourceId, databaseName, true);
    }

    /**
     * 获取数据库连接
     *
     * @param dataSourceId    数据源ID
     * @param databaseName    数据库名称，如果不为空，则获取该数据源下指定数据库名称的连接
     * @param useDatabaseName 是否使用数据库名称获取指定数据库的链接，默认true
     *                        如果连接信息的dbName {@link RdbmsConnectionInfo#getDbName()}}不为空，如果useDatabaseName为true则使用dbName进行连接，否则连接URL上不包含dbName
     * @return 数据库连接
     * @see RdbmsConnectionInfo#getDbName()
     */
    Connection getConnection(Long dataSourceId, @Nullable String databaseName, boolean useDatabaseName) throws RuntimeSQLException;

    /**
     * 根据RdbmsConnectionInfo内容拼接连接地址
     *
     * @param entity RdbmsConnectionInfo
     * @return JDBC连接地址
     */
    String getConnectionUrl(RdbmsConnectionInfo entity);

    /**
     * 获取数据库名称
     *
     * @param entity 数据库连接信息
     * @return 数据库名称
     */
    List<String> getDbNames(RdbmsConnectionInfo entity);

    /**
     * 获取数据库名称，如果时程序自身的数据源，则仅返回单个数据库
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
