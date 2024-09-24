package org.apache.ddlutils.platform;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public enum BuiltinJDBCDriver implements JDBCDriver {

    /**
     * The axion jdbc driver.
     */
    AXION("axiondb", "org.axiondb.jdbc.AxionDriver"),

    //=======================  db2 =============================================
    /**
     * standard db2 database
     */
    DB2("db2", "com.ibm.db2.jcc.DB2Driver"),
    /**
     * An alternative subprotocol used by the standard DB2 driver on OS/390.
     */
    DB2_OS390("db2os390"),
    /**
     * An alternative subprotocol used by the standard DB2 driver on OS/390.
     */
    DB2_OS390_SQLJ("db2os390sqlj"),
    /**
     * The sub-protocol used by the DataDirect DB2 driver.
     * The DataDirect Connect DB2 jdbc driver.
     */
    DATA_DIRECT_DB2("datadirect:db2", "com.ddtek.jdbc.db2.DB2Driver"),
    /**
     * Older name for the jdbc driver.
     */
    DB2_OLD1("", "COM.ibm.db2.jdbc.app.DB2Driver"),
    /**
     * A sub protocol used by the DB2 network driver.
     */
    DB2_NETWORK("db2j:net"),
    /**
     * Older name for the jdbc driver.
     */
    DB2_OLD2("", "COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver"),
    /**
     * The sub-protocol used by the i-net DB2 driver.
     */
    INET_DB2("inetdb2", "com.inet.drda.DRDADriver"),
    /**
     * An alternative subprotocol used by the JTOpen driver on OS/400.
     */
    JTOPEN_DB2("as400", "com.ibm.as400.access.AS400JDBCDriver"),
    /**
     * The sub-protocol used by the JNetDirect SQLServer driver.
     */
    JSQLCONNECT_SQLSERVER("JSQLConnect"),
    // Derby
    /**
     * The subprotocol used by the derby drivers.
     * The derby jdbc driver for use as a client for a normal server.
     */
    DERBY("derby", "org.apache.derby.jdbc.ClientDriver"),
    /**
     * The subprotocol used by the derby drivers.
     * The derby jdbc driver for use as an embedded database.
     */
    DERBY_EMBED("derby", "org.apache.derby.jdbc.EmbeddedDriver"),
    /**
     * The subprotocol used by the standard Firebird driver.
     */
    FIREBIRD("firebirdsql", "org.firebirdsql.jdbc.FBDriver"),
    /**
     * The subprotocol used by the standard Hsqldb driver.
     */
    HSQLDB("hsqldb", "org.hsqldb.jdbcDriver"),
    /**
     * The subprotocol used by the interbase driver.
     */
    INTERBASE("interbase", "interbase.interclient.Driver"),
    /**
     * The subprotocol used by the standard SapDB/MaxDB driver.
     */
    SAPDB("sapdb", "com.sap.dbtech.jdbc.DriverSapDB"),
    /**
     * The subprotocol used by the standard McKoi driver.
     */
    MCKOI("mckoi", "com.mckoi.JDBCDriver"),
    /**
     * The subprotocol used by the standard SQL Server driver.
     */
    SQL_SERVER("microsoft:sqlserver", "com.microsoft.jdbc.sqlserver.SQLServerDriver"),
    /**
     * The subprotocol recommended for the newer SQL Server 2005 driver.
     * The new SQLServer 2005 jdbc driver which can also be used for SQL Server 2000.
     */
    SQL_SERVER2005_NEW("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    /**
     * The subprotocol internally returned by the newer SQL Server 2005 driver.
     */
    SQL_SERVER2005_INTERNAL("sqljdbc"),
    /**
     * The sub-protocol used by the DataDirect SQLServer driver.
     */
    DATA_DIRECT_SQLSERVER("datadirect:sqlserver"),
    /**
     * A sub-protocol used by the i-net SQLServer driver.
     */
    INET_SQLSERVER("inetdae"),
    /**
     * A sub-protocol used by the i-net SQLServer driver.
     */
    INET_SQLSERVER6("inetdae6"),
    /**
     * A sub-protocol used by the i-net SQLServer driver.
     */
    INET_SQLSERVER7("inetdae7"),
    /**
     * A sub-protocol used by the i-net SQLServer driver.
     */
    INET_SQLSERVER7A("inetdae7a"),
    /**
     * A sub-protocol used by the pooled i-net SQLServer driver.
     */
    INET_SQLSERVER_POOLED("inetpool:inetdae"),
    /**
     * A sub-protocol used by the pooled i-net SQLServer driver.
     */
    INET_SQLSERVER6_POOLED("inetpool:inetdae6"),
    /**
     * A sub-protocol used by the pooled i-net SQLServer driver.
     */
    INET_SQLSERVER7_POOLED("inetpool:inetdae7"),
    /**
     * A sub-protocol used by the pooled i-net SQLServer driver.
     */
    INET_SQLSERVER7A_POOLED("inetpool:inetdae7a"),
    /**
     * A sub-protocol used by the pooled i-net SQLServer driver.
     */
    INET_SQLSERVER_JDBC_POOLED("inetpool:jdbc:inetdae"),
    /**
     * A sub-protocol used by the pooled i-net SQLServer driver.
     */
    INET_SQLSERVER6_JDBC_POOLED("inetpool:jdbc:inetdae6"),
    /**
     * A sub-protocol used by the pooled i-net SQLServer driver.
     */
    INET_SQLSERVER7_JDBC_POOLED("inetpool:jdbc:inetdae7"),
    /**
     * A sub-protocol used by the pooled i-net SQLServer driver.
     */
    INET_SQLSERVER7A_JDBC_POOLED("inetpool:jdbc:inetdae7a"),
    /**
     * The sub-protocol used by the DataDirect DB2 driver.
     */
    DATADIRECT_DB2("datadirect:db2"),
    /**
     * The sub-protocol used by the DataDirect Oracle driver.
     * The DataDirect Connect Oracle jdbc driver.
     */
    DATADIRECT_ORACLE("datadirect:oracle", "com.ddtek.jdbc.oracle.OracleDriver"),
    /**
     * The sub-protocol used by the DataDirect Sybase driver.
     */
    DATADIRECT_SYBASE("datadirect:sybase", "com.ddtek.jdbc.sybase.SybaseDriver"),
    /**
     * The sub-protocol used by the i-net Oracle driver.
     * The i-net Oracle jdbc driver.
     */
    INET_ORACLE("inetora", "com.inet.ora.OraDriver"),
    /**
     * The sub-protocol used by the i-net Sybase driver.
     * The i-net Sybase jdbc driver.
     */
    INET_SYBASE("inetsyb", "com.inet.syb.SybDriver"),
    /**
     * The sub-protocol used by the pooled i-net Sybase driver.
     */
    INET_SYBASE_POOLED("inetpool:inetsyb"),
    /**
     * The sub-protocol used by the pooled i-net Sybase driver.
     */
    INET_SYBASE_JDBC_POOLED("inetpool:jdbc:inetsyb"),
    /**
     * The i-net pooled jdbc driver for SQLServer and Sybase.
     */
    INET_POOLED_SQL_SERVER("", "com.inet.pool.PoolDriver"),
    /**
     * The sub-protocol used by the jTDS SQLServer driver.
     */
    JTDS_SQLSERVER("jtds:sqlserver"),
    /**
     * The sub-protocol used by the jTDS Sybase driver.
     */
    JTDS_SYBASE("jtds:sybase"),
    /**
     * The subprotocol used by the standard Sybase driver.
     */
    SYBASE("sybase:Tds", "com.sybase.jdbc2.jdbc.SybDriver"),
    /**
     * The old Sybase jdbc driver.
     */
    SYBASE_OLD("sybase:Tds", "com.sybase.jdbc.SybDriver"),
    /**
     * The sub protocol used by the standard PostgreSQL driver.
     */
    POSTGRE_SQL("postgresql", "org.postgresql.Driver"),
    /**
     * The thin subprotocol used by the standard Oracle driver.
     */
    ORACLE_THIN("oracle:thin", "oracle.jdbc.driver.OracleDriver"),
    /**
     * The thin subprotocol used by the standard Oracle driver.
     */
    ORACLE_OCI8("oracle:oci8"),
    /**
     * The old thin subprotocol used by the standard Oracle driver.
     * The old Oracle jdbc driver.
     */
    ORACLE_THIN_OLD("oracle:dnldthin", "oracle.jdbc.dnlddriver.OracleDriver"),
    /**
     * The jTDS jdbc driver for SQLServer and Sybase.
     */
    JTDS("", "net.sourceforge.jtds.jdbc.Driver"),
    /**
     * The JNetDirect SQLServer jdbc driver.
     */
    JNET_DIRECT_SQLSERVER("", "com.jnetdirect.jsql.JSQLDriver"),
    /**
     * The i-net SQLServer jdbc driver.
     */
    INET_SQL_SERVER("", "com.inet.tds.TdsDriver"),
    /**
     * The DataDirect Connect SQLServer jdbc driver.
     */
    DATADIRECT_SQLSERVER("", "com.ddtek.jdbc.sqlserver.SQLServerDriver"),
    /**
     * The standard MySQL jdbc driver.
     * The sub protocol used by the standard MySQL driver.
     */
    MYSQL("mysql", "com.mysql.jdbc.Driver"),
    /**
     * The old MySQL jdbc driver.
     */
    MYSQL_OLD("mysql", "org.gjt.mm.mysql.Driver"),
    /**
     * cloudscape net sub protocol
     */
    CLOUDSCAPE_NET("cloudscape:net", ""),

    ;

    private final String subProtocol;
    private final String driverClassName;

    BuiltinJDBCDriver(String subProtocol) {
        this(subProtocol, null);
    }

    BuiltinJDBCDriver(String subProtocol, String driverClassName) {
        this.subProtocol = subProtocol;
        this.driverClassName = driverClassName;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getSubProtocol() {
        return subProtocol;
    }

    @Override
    public String getConnectionUrl(String hostname, int port, String databaseName, Properties props) {
        return null;
    }

    @Override
    public @NotNull String getDriverClassName() {
        return driverClassName;
    }
}
