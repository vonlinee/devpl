package org.apache.ddlutils;

import org.apache.ddlutils.jdbc.JdbcUtils;
import org.apache.ddlutils.platform.DBType;
import org.apache.ddlutils.platform.BuiltinDBType;
import org.apache.ddlutils.platform.JDBCDriver;
import org.apache.ddlutils.platform.BuiltinJDBCDriver;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility functions for dealing with database platforms.
 */
public final class PlatformUtils {

    /**
     * Maps the sub-protocol part of a jdbc connection url to a OJB platform name.
     */
    private static final Map<String, String> jdbcSubProtocolToPlatform = new HashMap<>();
    /**
     * Maps the jdbc driver name to a OJB platform name.
     */
    private static final Map<String, String> jdbcDriverToPlatform = new HashMap<>();

    static {
        init();
    }

    /**
     * Creates a new instance.
     */
    public static void init() {
        // Note that currently Sapdb and MaxDB have equal sub-protocols and
        // drivers, so we have no means to distinguish them
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.AXION, BuiltinDBType.AXION);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.DB2_NETWORK, BuiltinDBType.CLOUDSCAPE);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.CLOUDSCAPE_NET, BuiltinDBType.CLOUDSCAPE);
        // db2
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.DB2, BuiltinDBType.DB2);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.DB2_OS390, BuiltinDBType.DB2);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.DB2_OS390_SQLJ, BuiltinDBType.DB2);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.JTOPEN_DB2, BuiltinDBType.DB2);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.DATA_DIRECT_DB2, BuiltinDBType.DB2);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_DB2, BuiltinDBType.DB2);
        // derby
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.DERBY, BuiltinDBType.DERBY);
        // Firebird
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.FIREBIRD, BuiltinDBType.FIREBIRD);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.HSQLDB, BuiltinDBType.HSQLDB);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INTERBASE, BuiltinDBType.INTERBASE);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.SAPDB, BuiltinDBType.SAPDB);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.MCKOI, BuiltinDBType.MCKOI);

        // mssql
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.SQL_SERVER, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.SQL_SERVER2005_NEW, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.SQL_SERVER2005_INTERNAL, BuiltinDBType.MSSQL);

        // sqlserver
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.DATA_DIRECT_SQLSERVER, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SQLSERVER, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SQLSERVER6, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SQLSERVER7, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SQLSERVER7A, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SQLSERVER_POOLED, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SQLSERVER6_POOLED, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SQLSERVER7_POOLED, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SQLSERVER7A_POOLED, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SQLSERVER_JDBC_POOLED, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SQLSERVER6_JDBC_POOLED, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SQLSERVER7_JDBC_POOLED, BuiltinDBType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SQLSERVER7A_JDBC_POOLED, BuiltinDBType.MSSQL);

        // jsqlconnect
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.JSQLCONNECT_SQLSERVER, BuiltinDBType.MSSQL);
        // jtds
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.JTDS_SQLSERVER, BuiltinDBType.MSSQL);
        // mysql
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.MYSQL, BuiltinDBType.MYSQL);

        // oracle
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.ORACLE_THIN, BuiltinDBType.ORACLE8);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.ORACLE_OCI8, BuiltinDBType.ORACLE8);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.ORACLE_THIN_OLD, BuiltinDBType.ORACLE8);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.DATADIRECT_ORACLE, BuiltinDBType.ORACLE8);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_ORACLE, BuiltinDBType.ORACLE8);

        // pg
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.POSTGRE_SQL, BuiltinDBType.POSTGRE_SQL);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.SYBASE, BuiltinDBType.SYBASE);

        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.DATADIRECT_SYBASE, BuiltinDBType.SYBASE);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SYBASE, BuiltinDBType.SYBASE);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SYBASE_POOLED, BuiltinDBType.SYBASE);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.INET_SYBASE_JDBC_POOLED, BuiltinDBType.SYBASE);
        addSubProtocolToPlatformMapping(BuiltinJDBCDriver.JTDS_SYBASE, BuiltinDBType.SYBASE);

        // 驱动映射
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.AXION, BuiltinDBType.AXION);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.DB2, BuiltinDBType.DB2);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.DB2_OLD1, BuiltinDBType.DB2);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.DB2_OLD2, BuiltinDBType.DB2);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.JTOPEN_DB2, BuiltinDBType.DB2);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.DATA_DIRECT_DB2, BuiltinDBType.DB2);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.INET_DB2, BuiltinDBType.DB2);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.DERBY_EMBED, BuiltinDBType.DERBY);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.DERBY, BuiltinDBType.DERBY);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.FIREBIRD, BuiltinDBType.FIREBIRD);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.HSQLDB, BuiltinDBType.HSQLDB);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.INTERBASE, BuiltinDBType.INTERBASE);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.SAPDB, BuiltinDBType.SAPDB);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.MCKOI, BuiltinDBType.MCKOI);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.SQL_SERVER, BuiltinDBType.MSSQL);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.SQL_SERVER2005_NEW, BuiltinDBType.MSSQL);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.DATADIRECT_SQLSERVER, BuiltinDBType.MSSQL);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.INET_SQL_SERVER, BuiltinDBType.MSSQL);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.JNET_DIRECT_SQLSERVER, BuiltinDBType.MSSQL);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.MYSQL, BuiltinDBType.MYSQL);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.MYSQL_OLD, BuiltinDBType.MYSQL);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.ORACLE_THIN, BuiltinDBType.ORACLE8);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.ORACLE_THIN_OLD, BuiltinDBType.ORACLE8);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.DATADIRECT_ORACLE, BuiltinDBType.ORACLE8);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.INET_ORACLE, BuiltinDBType.ORACLE8);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.POSTGRE_SQL, BuiltinDBType.POSTGRE_SQL);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.SYBASE, BuiltinDBType.SYBASE);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.SYBASE_OLD, BuiltinDBType.SYBASE);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.DATADIRECT_SYBASE, BuiltinDBType.SYBASE);
        addJdbcDriverToPlatformMapping(BuiltinJDBCDriver.INET_SYBASE, BuiltinDBType.SYBASE);
    }

    static void addSubProtocolToPlatformMapping(JDBCDriver driverType, DBType dbType) {
        jdbcSubProtocolToPlatform.put(driverType.getSubProtocol(), dbType.getName());
    }

    static void addJdbcDriverToPlatformMapping(JDBCDriver driverType, DBType dbType) {
        jdbcDriverToPlatform.put(driverType.getSubProtocol(), dbType.getName());
    }

    /**
     * Tries to determine the database type for the given data source. Note that this will establish
     * a connection to the database.
     *
     * @param dataSource The data source
     * @return The database type or <code>null</code> if the database type couldn't be determined
     */
    public static String determineDatabaseType(DataSource dataSource) throws DatabaseOperationException {
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
    public static String determineDatabaseType(DataSource dataSource, String username, String password) throws DatabaseOperationException {
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
    public static String determineDatabaseType(String driverName, String jdbcConnectionUrl) {
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
