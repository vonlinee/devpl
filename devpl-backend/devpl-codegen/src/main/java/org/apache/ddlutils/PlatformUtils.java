package org.apache.ddlutils;

import org.apache.ddlutils.platform.DBType;
import org.apache.ddlutils.platform.DBTypeEnum;
import org.apache.ddlutils.platform.JDBCDriverType;
import org.apache.ddlutils.platform.JDBCDriverTypeEnum;
import org.apache.ddlutils.util.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility functions for dealing with database platforms.
 */
public class PlatformUtils {

    /**
     * Maps the sub-protocol part of a jdbc connection url to a OJB platform name.
     */
    private final Map<String, String> jdbcSubProtocolToPlatform = new HashMap<>();
    /**
     * Maps the jdbc driver name to a OJB platform name.
     */
    private final Map<String, String> jdbcDriverToPlatform = new HashMap<>();

    void addSubProtocolToPlatformMapping(JDBCDriverType driverType, DBType dbType) {
        jdbcSubProtocolToPlatform.put(driverType.getSubProtocol(), dbType.getName());
    }

    void addJdbcDriverToPlatformMapping(JDBCDriverType driverType, DBType dbType) {
        jdbcDriverToPlatform.put(driverType.getSubProtocol(), dbType.getName());
    }

    /**
     * Creates a new instance.
     */
    public PlatformUtils() {
        // Note that currently Sapdb and MaxDB have equal sub-protocols and
        // drivers, so we have no means to distinguish them
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.AXION, DBTypeEnum.AXION);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.DB2_NETWORK, DBTypeEnum.CLOUDSCAPE);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.CLOUDSCAPE_NET, DBTypeEnum.CLOUDSCAPE);
        // db2
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.DB2, DBTypeEnum.DB2);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.DB2_OS390, DBTypeEnum.DB2);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.DB2_OS390_SQLJ, DBTypeEnum.DB2);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.JTOPEN_DB2, DBTypeEnum.DB2);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.DATA_DIRECT_DB2, DBTypeEnum.DB2);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_DB2, DBTypeEnum.DB2);
        // derby
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.DERBY, DBTypeEnum.DERBY);
        // Firebird
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.FIREBIRD, DBTypeEnum.FIREBIRD);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.HSQLDB, DBTypeEnum.HSQLDB);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INTERBASE, DBTypeEnum.INTERBASE);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.SAPDB, DBTypeEnum.SAPDB);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.MCKOI, DBTypeEnum.MCKOI);

        // mssql
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.SQL_SERVER, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.SQL_SERVER2005_NEW, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.SQL_SERVER2005_INTERNAL, DBTypeEnum.MSSQL);

        // sqlserver
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.DATA_DIRECT_SQLSERVER, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SQLSERVER, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SQLSERVER6, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SQLSERVER7, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SQLSERVER7A, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SQLSERVER_POOLED, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SQLSERVER6_POOLED, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SQLSERVER7_POOLED, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SQLSERVER7A_POOLED, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SQLSERVER_JDBC_POOLED, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SQLSERVER6_JDBC_POOLED, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SQLSERVER7_JDBC_POOLED, DBTypeEnum.MSSQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SQLSERVER7A_JDBC_POOLED, DBTypeEnum.MSSQL);

        // jsqlconnect
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.JSQLCONNECT_SQLSERVER, DBTypeEnum.MSSQL);
        // jtds
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.JTDS_SQLSERVER, DBTypeEnum.MSSQL);
        // mysql
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.MYSQL, DBTypeEnum.MYSQL);

        // oracle
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.ORACLE_THIN, DBTypeEnum.ORACLE8);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.ORACLE_OCI8, DBTypeEnum.ORACLE8);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.ORACLE_THIN_OLD, DBTypeEnum.ORACLE8);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.DATADIRECT_ORACLE, DBTypeEnum.ORACLE8);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_ORACLE, DBTypeEnum.ORACLE8);

        // pg
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.POSTGRE_SQL, DBTypeEnum.POSTGRE_SQL);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.SYBASE, DBTypeEnum.SYBASE);

        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.DATADIRECT_SYBASE, DBTypeEnum.SYBASE);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SYBASE, DBTypeEnum.SYBASE);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SYBASE_POOLED, DBTypeEnum.SYBASE);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.INET_SYBASE_JDBC_POOLED, DBTypeEnum.SYBASE);
        addSubProtocolToPlatformMapping(JDBCDriverTypeEnum.JTDS_SYBASE, DBTypeEnum.SYBASE);

        // 驱动映射
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.AXION, DBTypeEnum.AXION);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.DB2, DBTypeEnum.DB2);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.DB2_OLD1, DBTypeEnum.DB2);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.DB2_OLD2, DBTypeEnum.DB2);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.JTOPEN_DB2, DBTypeEnum.DB2);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.DATA_DIRECT_DB2, DBTypeEnum.DB2);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.INET_DB2, DBTypeEnum.DB2);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.DERBY_EMBED, DBTypeEnum.DERBY);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.DERBY, DBTypeEnum.DERBY);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.FIREBIRD, DBTypeEnum.FIREBIRD);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.HSQLDB, DBTypeEnum.HSQLDB);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.INTERBASE, DBTypeEnum.INTERBASE);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.SAPDB, DBTypeEnum.SAPDB);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.MCKOI, DBTypeEnum.MCKOI);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.SQL_SERVER, DBTypeEnum.MSSQL);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.SQL_SERVER2005_NEW, DBTypeEnum.MSSQL);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.DATADIRECT_SQLSERVER, DBTypeEnum.MSSQL);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.INET_SQL_SERVER, DBTypeEnum.MSSQL);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.JNET_DIRECT_SQLSERVER, DBTypeEnum.MSSQL);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.MYSQL, DBTypeEnum.MYSQL);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.MYSQL_OLD, DBTypeEnum.MYSQL);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.ORACLE_THIN, DBTypeEnum.ORACLE8);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.ORACLE_THIN_OLD, DBTypeEnum.ORACLE8);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.DATADIRECT_ORACLE, DBTypeEnum.ORACLE8);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.INET_ORACLE, DBTypeEnum.ORACLE8);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.POSTGRE_SQL, DBTypeEnum.POSTGRE_SQL);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.SYBASE, DBTypeEnum.SYBASE);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.SYBASE_OLD, DBTypeEnum.SYBASE);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.DATADIRECT_SYBASE, DBTypeEnum.SYBASE);
        addJdbcDriverToPlatformMapping(JDBCDriverTypeEnum.INET_SYBASE, DBTypeEnum.SYBASE);
    }

    /**
     * Tries to determine the database type for the given data source. Note that this will establish
     * a connection to the database.
     *
     * @param dataSource The data source
     * @return The database type or <code>null</code> if the database type couldn't be determined
     */
    public String determineDatabaseType(DataSource dataSource) throws DatabaseOperationException {
        return determineDatabaseType(dataSource, null, null);
    }

    /**
     * Tries to determine the database type for the given data source. Note that this will establish
     * a connection to the database.
     *
     * @param dataSource The data source
     * @param username   The username to use for connecting to the database
     * @param password   The password to use for connecting to the database
     * @return The database type or <code>null</code> if the database type couldn't be determined
     */
    public String determineDatabaseType(DataSource dataSource, String username, String password) throws DatabaseOperationException {
        try (Connection conn = JdbcUtils.getConnection(dataSource, username, password)) {
            DatabaseMetaData metaData = conn.getMetaData();
            return determineDatabaseType(metaData.getDriverName(), metaData.getURL());
        } catch (SQLException ex) {
            throw new DatabaseOperationException("Error while reading the database metadata: " + ex.getMessage(), ex);
        }
    }

    /**
     * Tries to determine the database type for the given jdbc driver and connection url.
     *
     * @param driverName        The fully qualified name of the JDBC driver
     * @param jdbcConnectionUrl The connection url
     * @return The database type or <code>null</code> if the database type couldn't be determined
     */
    public String determineDatabaseType(String driverName, String jdbcConnectionUrl) {
        if (jdbcDriverToPlatform.containsKey(driverName)) {
            return jdbcDriverToPlatform.get(driverName);
        }
        if (jdbcConnectionUrl == null) {
            return null;
        }
        for (Map.Entry<String, String> entry : jdbcSubProtocolToPlatform.entrySet()) {
            String curSubProtocol = "jdbc:" + entry.getKey() + ":";
            if (jdbcConnectionUrl.startsWith(curSubProtocol)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
