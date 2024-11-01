package org.apache.ddlutils;

import org.apache.ddlutils.jdbc.JdbcUtils;
import org.apache.ddlutils.platform.BuiltinDatabaseType;
import org.apache.ddlutils.platform.DatabaseType;
import org.apache.ddlutils.platform.DriverType;
import org.apache.ddlutils.platform.BuiltinDriverType;

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
        addSubProtocolToPlatformMapping(BuiltinDriverType.AXION, BuiltinDatabaseType.AXION);
        addSubProtocolToPlatformMapping(BuiltinDriverType.DB2_NETWORK, BuiltinDatabaseType.CLOUDSCAPE);
        addSubProtocolToPlatformMapping(BuiltinDriverType.CLOUDSCAPE_NET, BuiltinDatabaseType.CLOUDSCAPE);
        // db2
        addSubProtocolToPlatformMapping(BuiltinDriverType.DB2, BuiltinDatabaseType.DB2);
        addSubProtocolToPlatformMapping(BuiltinDriverType.DB2_OS390, BuiltinDatabaseType.DB2);
        addSubProtocolToPlatformMapping(BuiltinDriverType.DB2_OS390_SQLJ, BuiltinDatabaseType.DB2);
        addSubProtocolToPlatformMapping(BuiltinDriverType.JTOPEN_DB2, BuiltinDatabaseType.DB2);
        addSubProtocolToPlatformMapping(BuiltinDriverType.DATA_DIRECT_DB2, BuiltinDatabaseType.DB2);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_DB2, BuiltinDatabaseType.DB2);
        // derby
        addSubProtocolToPlatformMapping(BuiltinDriverType.DERBY, BuiltinDatabaseType.DERBY);
        // Firebird
        addSubProtocolToPlatformMapping(BuiltinDriverType.FIREBIRD, BuiltinDatabaseType.FIREBIRD);
        addSubProtocolToPlatformMapping(BuiltinDriverType.HSQLDB, BuiltinDatabaseType.HSQLDB);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INTERBASE, BuiltinDatabaseType.INTERBASE);
        addSubProtocolToPlatformMapping(BuiltinDriverType.SAPDB, BuiltinDatabaseType.SAPDB);
        addSubProtocolToPlatformMapping(BuiltinDriverType.MCKOI, BuiltinDatabaseType.MCKOI);

        // mssql
        addSubProtocolToPlatformMapping(BuiltinDriverType.SQL_SERVER, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.SQL_SERVER2005_NEW, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.SQL_SERVER2005_INTERNAL, BuiltinDatabaseType.MSSQL);

        // sqlserver
        addSubProtocolToPlatformMapping(BuiltinDriverType.DATA_DIRECT_SQLSERVER, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SQLSERVER, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SQLSERVER6, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SQLSERVER7, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SQLSERVER7A, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SQLSERVER_POOLED, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SQLSERVER6_POOLED, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SQLSERVER7_POOLED, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SQLSERVER7A_POOLED, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SQLSERVER_JDBC_POOLED, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SQLSERVER6_JDBC_POOLED, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SQLSERVER7_JDBC_POOLED, BuiltinDatabaseType.MSSQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SQLSERVER7A_JDBC_POOLED, BuiltinDatabaseType.MSSQL);

        // jsqlconnect
        addSubProtocolToPlatformMapping(BuiltinDriverType.JSQLCONNECT_SQLSERVER, BuiltinDatabaseType.MSSQL);
        // jtds
        addSubProtocolToPlatformMapping(BuiltinDriverType.JTDS_SQLSERVER, BuiltinDatabaseType.MSSQL);
        // mysql
        addSubProtocolToPlatformMapping(BuiltinDriverType.MYSQL, BuiltinDatabaseType.MYSQL);

        // oracle
        addSubProtocolToPlatformMapping(BuiltinDriverType.ORACLE_THIN, BuiltinDatabaseType.ORACLE8);
        addSubProtocolToPlatformMapping(BuiltinDriverType.ORACLE_OCI8, BuiltinDatabaseType.ORACLE8);
        addSubProtocolToPlatformMapping(BuiltinDriverType.ORACLE_THIN_OLD, BuiltinDatabaseType.ORACLE8);
        addSubProtocolToPlatformMapping(BuiltinDriverType.DATADIRECT_ORACLE, BuiltinDatabaseType.ORACLE8);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_ORACLE, BuiltinDatabaseType.ORACLE8);

        // pg
        addSubProtocolToPlatformMapping(BuiltinDriverType.POSTGRE_SQL, BuiltinDatabaseType.POSTGRE_SQL);
        addSubProtocolToPlatformMapping(BuiltinDriverType.SYBASE, BuiltinDatabaseType.SYBASE);

        addSubProtocolToPlatformMapping(BuiltinDriverType.DATADIRECT_SYBASE, BuiltinDatabaseType.SYBASE);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SYBASE, BuiltinDatabaseType.SYBASE);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SYBASE_POOLED, BuiltinDatabaseType.SYBASE);
        addSubProtocolToPlatformMapping(BuiltinDriverType.INET_SYBASE_JDBC_POOLED, BuiltinDatabaseType.SYBASE);
        addSubProtocolToPlatformMapping(BuiltinDriverType.JTDS_SYBASE, BuiltinDatabaseType.SYBASE);

        // 驱动映射
        addJdbcDriverToPlatformMapping(BuiltinDriverType.AXION, BuiltinDatabaseType.AXION);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.DB2, BuiltinDatabaseType.DB2);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.DB2_OLD1, BuiltinDatabaseType.DB2);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.DB2_OLD2, BuiltinDatabaseType.DB2);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.JTOPEN_DB2, BuiltinDatabaseType.DB2);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.DATA_DIRECT_DB2, BuiltinDatabaseType.DB2);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.INET_DB2, BuiltinDatabaseType.DB2);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.DERBY_EMBED, BuiltinDatabaseType.DERBY);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.DERBY, BuiltinDatabaseType.DERBY);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.FIREBIRD, BuiltinDatabaseType.FIREBIRD);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.HSQLDB, BuiltinDatabaseType.HSQLDB);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.INTERBASE, BuiltinDatabaseType.INTERBASE);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.SAPDB, BuiltinDatabaseType.SAPDB);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.MCKOI, BuiltinDatabaseType.MCKOI);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.SQL_SERVER, BuiltinDatabaseType.MSSQL);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.SQL_SERVER2005_NEW, BuiltinDatabaseType.MSSQL);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.DATADIRECT_SQLSERVER, BuiltinDatabaseType.MSSQL);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.INET_SQL_SERVER, BuiltinDatabaseType.MSSQL);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.JNET_DIRECT_SQLSERVER, BuiltinDatabaseType.MSSQL);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.MYSQL, BuiltinDatabaseType.MYSQL);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.MYSQL_OLD, BuiltinDatabaseType.MYSQL);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.ORACLE_THIN, BuiltinDatabaseType.ORACLE8);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.ORACLE_THIN_OLD, BuiltinDatabaseType.ORACLE8);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.DATADIRECT_ORACLE, BuiltinDatabaseType.ORACLE8);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.INET_ORACLE, BuiltinDatabaseType.ORACLE8);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.POSTGRE_SQL, BuiltinDatabaseType.POSTGRE_SQL);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.SYBASE, BuiltinDatabaseType.SYBASE);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.SYBASE_OLD, BuiltinDatabaseType.SYBASE);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.DATADIRECT_SYBASE, BuiltinDatabaseType.SYBASE);
        addJdbcDriverToPlatformMapping(BuiltinDriverType.INET_SYBASE, BuiltinDatabaseType.SYBASE);
    }

    static void addSubProtocolToPlatformMapping(DriverType driverType, DatabaseType databaseType) {
        jdbcSubProtocolToPlatform.put(driverType.getSubProtocol(), databaseType.getName());
    }

    static void addJdbcDriverToPlatformMapping(DriverType driverType, DatabaseType databaseType) {
        jdbcDriverToPlatform.put(driverType.getSubProtocol(), databaseType.getName());
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
