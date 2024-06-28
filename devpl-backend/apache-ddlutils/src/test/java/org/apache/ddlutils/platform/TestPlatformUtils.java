package org.apache.ddlutils.platform;

import org.apache.ddlutils.PlatformUtils;
import org.apache.ddlutils.platform.axion.AxionPlatform;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the {@link org.apache.ddlutils.PlatformUtils} class.
 *
 * @version $Revision: 279421 $
 */
public class TestPlatformUtils {

    /**
     * Tests the determination of the Axion platform via its JDBC driver.
     */
    @Test
    public void testAxionDriver() {
        assertEquals(AxionPlatform.DATABASENAME, PlatformUtils.determineDatabaseType("org.axiondb.jdbc.AxionDriver", null));
    }

    /**
     * Tests the determination of the Axion platform via JDBC connection urls.
     */
    @Test
    public void testAxionUrl() {
        assertEquals(AxionPlatform.DATABASENAME, PlatformUtils.determineDatabaseType(null, "jdbc:axiondb:testdb"));
        assertEquals(AxionPlatform.DATABASENAME, PlatformUtils.determineDatabaseType(null, "jdbc:axiondb:testdb:/tmp/testdbdir"));
    }

    /**
     * Tests the determination of the Db2 platform via its JDBC drivers.
     */
    @Test
    public void testDb2Driver() {
        assertEquals(DBTypeEnum.DB2.getName(), PlatformUtils.determineDatabaseType("com.ibm.db2.jcc.DB2Driver", null));
        assertEquals(DBTypeEnum.DB2.getName(), PlatformUtils.determineDatabaseType("COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver", null));
        assertEquals(DBTypeEnum.DB2.getName(), PlatformUtils.determineDatabaseType("COM.ibm.db2.jdbc.app.DB2Driver", null));
        // DataDirect Connect
        assertEquals(DBTypeEnum.DB2.getName(), PlatformUtils.determineDatabaseType("com.ddtek.jdbc.db2.DB2Driver", null));
        // i-net
        assertEquals(DBTypeEnum.DB2.getName(), PlatformUtils.determineDatabaseType("com.inet.drda.DRDADriver", null));
    }

    /**
     * Tests the determination of the Db2 platform via JDBC connection urls.
     */
    @Test
    public void testDb2Url() {
        assertEquals(DBTypeEnum.DB2.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:db2://sysmvs1.stl.ibm.com:5021/san_jose"));
        assertEquals(DBTypeEnum.DB2.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:db2os390://sysmvs1.stl.ibm.com:5021/san_jose"));
        assertEquals(DBTypeEnum.DB2.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:db2os390sqlj://sysmvs1.stl.ibm.com:5021/san_jose"));
        // DataDirect Connect
        assertEquals(DBTypeEnum.DB2.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:datadirect:db2://server1:50000;DatabaseName=jdbc;User=test;Password=secret"));
        // i-net
        assertEquals(DBTypeEnum.DB2.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetdb2://server1:50000"));
    }

    /**
     * Tests the determination of the Cloudscape platform via JDBC connection urls.
     */
    @Test
    public void testCloudscapeUrl() {
        assertEquals(DBTypeEnum.CLOUDSCAPE.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:db2j:net:database"));
        assertEquals(DBTypeEnum.CLOUDSCAPE.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:cloudscape:net:database"));
    }

    /**
     * Tests the determination of the Derby platform via its JDBC drivers.
     */
    @Test
    public void testDerbyDriver() {
        assertEquals(DBTypeEnum.DERBY.getName(), PlatformUtils.determineDatabaseType("org.apache.derby.jdbc.ClientDriver", null));
        assertEquals(DBTypeEnum.DERBY.getName(), PlatformUtils.determineDatabaseType("org.apache.derby.jdbc.EmbeddedDriver", null));
    }

    /**
     * Tests the determination of the Derby platform via JDBC connection urls.
     */
    @Test
    public void testDerbyUrl() {
        assertEquals(DBTypeEnum.DERBY.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:derby:sample"));
    }

    /**
     * Tests the determination of the Firebird platform via its JDBC driver.
     */
    @Test
    public void testFirebirdDriver() {
        assertEquals(DBTypeEnum.FIREBIRD.getName(), PlatformUtils.determineDatabaseType("org.firebirdsql.jdbc.FBDriver", null));
    }

    /**
     * Tests the determination of the Firebird platform via JDBC connection urls.
     */
    @Test
    public void testFirebirdUrl() {
        assertEquals(DBTypeEnum.FIREBIRD.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:firebirdsql://localhost:8080/path/to/db.fdb"));
        assertEquals(DBTypeEnum.FIREBIRD.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:firebirdsql:native:localhost/8080:/path/to/db.fdb"));
        assertEquals(DBTypeEnum.FIREBIRD.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:firebirdsql:local://localhost:8080:/path/to/db.fdb"));
        assertEquals(DBTypeEnum.FIREBIRD.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:firebirdsql:embedded:localhost/8080:/path/to/db.fdb"));
    }

    /**
     * Tests the determination of the HsqlDb platform via its JDBC driver.
     */
    @Test
    public void testHsqldbDriver() {
        assertEquals(DBTypeEnum.HSQLDB.getName(), PlatformUtils.determineDatabaseType("org.hsqldb.jdbcDriver", null));
    }

    /**
     * Tests the determination of the HsqlDb platform via JDBC connection urls.
     */
    @Test
    public void testHsqldbUrl() {
        assertEquals(DBTypeEnum.HSQLDB.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:hsqldb:/opt/db/testdb"));
    }

    /**
     * Tests the determination of the Interbase platform via its JDBC driver.
     */
    @Test
    public void testInterbaseDriver() {
        assertEquals(DBTypeEnum.INTERBASE.getName(), PlatformUtils.determineDatabaseType("interbase.interclient.Driver", null));
    }

    /**
     * Tests the determination of the Interbase platform via JDBC connection urls.
     */
    @Test
    public void testInterbaseUrl() {
        assertEquals(DBTypeEnum.INTERBASE.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:interbase://localhost/e:/testbed/database/employee.gdb"));
    }

    /**
     * Tests the determination of the McKoi platform via its JDBC driver.
     */
    @Test
    public void testMckoiDriver() {
        assertEquals(DBTypeEnum.MCKOI.getName(), PlatformUtils.determineDatabaseType("com.mckoi.JDBCDriver", null));
    }

    /**
     * Tests the determination of the McKoi platform via JDBC connection urls.
     */
    @Test
    public void testMckoiUrl() {
        assertEquals(DBTypeEnum.MCKOI.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:mckoi:local://./db.conf"));
        assertEquals(DBTypeEnum.MCKOI.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:mckoi://db.myhost.org/"));
    }

    /**
     * Tests the determination of the Microsoft Sql Server platform via its JDBC drivers.
     */
    @Test
    public void testMsSqlDriver() {
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType("com.microsoft.jdbc.sqlserver.SQLServerDriver", null));
        // DataDirect Connect
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType("com.ddtek.jdbc.sqlserver.SQLServerDriver", null));
        // JNetDirect JSQLConnect
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType("com.jnetdirect.jsql.JSQLDriver", null));
        // i-net
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType("com.inet.tds.TdsDriver", null));
    }

    /**
     * Tests the determination of the Microsoft Sql Server platform via JDBC connection urls.
     */
    @Test
    public void testMsSqlUrl() {
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:microsoft:sqlserver://localhost:1433"));
        // DataDirect Connect
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:datadirect:sqlserver://server1:1433;User=test;Password=secret"));
        // JNetDirect JSQLConnect
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:JSQLConnect://localhost/database=master/user=sa/sqlVersion=6"));
        // i-net
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetdae:210.1.164.19:1433"));
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetdae6:[2002:d201:a413::d201:a413]:1433"));
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetdae7:localHost:1433"));
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetdae7a://MyServer/pipe/sql/query"));
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetpool:inetdae:210.1.164.19:1433"));
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetpool:inetdae6:[2002:d201:a413::d201:a413]:1433"));
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetpool:inetdae7:localHost:1433"));
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetpool:inetdae7a://MyServer/pipe/sql/query"));
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetpool:jdbc:inetdae:210.1.164.19:1433"));
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetpool:jdbc:inetdae6:[2002:d201:a413::d201:a413]:1433"));
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetpool:jdbc:inetdae7:localHost:1433"));
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetpool:jdbc:inetdae7a://MyServer/pipe/sql/query"));
        // jTDS
        assertEquals(DBTypeEnum.MSSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:jtds:sqlserver://localhost:8080/test"));
    }

    /**
     * Tests the determination of the MySql platform via its JDBC drivers.
     */
    @Test
    public void testMySqlDriver() {
        assertEquals(DBTypeEnum.MYSQL.getName(), PlatformUtils.determineDatabaseType("com.mysql.jdbc.Driver", null));
        assertEquals(DBTypeEnum.MYSQL.getName(), PlatformUtils.determineDatabaseType("org.gjt.mm.mysql.Driver", null));
    }

    /**
     * Tests the determination of the MySql platform via JDBC connection urls.
     */
    @Test
    public void testMySqlUrl() {
        assertEquals(DBTypeEnum.MYSQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:mysql://localhost:1234/test"));
    }

    /**
     * Tests the determination of the Oracle 8 platform via its JDBC drivers.
     */
    @Test
    public void testOracleDriver() {
        assertEquals(DBTypeEnum.ORACLE8.getName(), PlatformUtils.determineDatabaseType("oracle.jdbc.driver.OracleDriver", null));
        assertEquals(DBTypeEnum.ORACLE8.getName(), PlatformUtils.determineDatabaseType("oracle.jdbc.dnlddriver.OracleDriver", null));
        // DataDirect Connect
        assertEquals(DBTypeEnum.ORACLE8.getName(), PlatformUtils.determineDatabaseType("com.ddtek.jdbc.oracle.OracleDriver", null));
        // i-net
        assertEquals(DBTypeEnum.ORACLE8.getName(), PlatformUtils.determineDatabaseType("com.inet.ora.OraDriver", null));
    }

    /**
     * Tests the determination of the Oracle 8 platform via JDBC connection urls.
     */
    @Test
    public void testOracleUrl() {
        assertEquals(DBTypeEnum.ORACLE8.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:oracle:thin:@myhost:1521:orcl"));
        assertEquals(DBTypeEnum.ORACLE8.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:oracle:oci8:@(description=(address=(host=myhost)(protocol=tcp)(port=1521))(connect_data=(sid=orcl)))"));
        assertEquals(DBTypeEnum.ORACLE8.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:oracle:dnldthin:@myhost:1521:orcl"));
        assertEquals(DBTypeEnum.ORACLE8.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:oracle:dnldthin:@myhost:1521:orcl"));
        // DataDirect Connect
        assertEquals(DBTypeEnum.ORACLE8.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:datadirect:oracle://server3:1521;ServiceName=ORCL;User=test;Password=secret"));
        // i-net
        assertEquals(DBTypeEnum.ORACLE8.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetora:www.inetsoftware.de:1521:orcl?traceLevel=2"));
    }

    /**
     * Tests the determination of the PostgreSql platform via its JDBC driver.
     */
    @Test
    public void testPostgreSqlDriver() {
        assertEquals(DBTypeEnum.POSTGRE_SQL.getName(), PlatformUtils.determineDatabaseType("org.postgresql.Driver", null));
    }

    /**
     * Tests the determination of the PostgreSql platform via JDBC connection urls.
     */
    @Test
    public void testPostgreSqlUrl() {
        assertEquals(DBTypeEnum.POSTGRE_SQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:postgresql://localhost:1234/test"));
        assertEquals(DBTypeEnum.POSTGRE_SQL.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:postgresql://[::1]:5740/accounting"));
    }

    /**
     * Tests the determination of the SapDb platform via its JDBC driver.
     */
    @Test
    public void testSapDbDriver() {
        assertEquals(DBTypeEnum.SAPDB.getName(), PlatformUtils.determineDatabaseType("com.sap.dbtech.jdbc.DriverSapDB", null));
    }

    /**
     * Tests the determination of the SapDb platform via JDBC connection urls.
     */
    @Test
    public void testSapDbUrl() {
        assertEquals(DBTypeEnum.SAPDB.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:sapdb://servermachine:9876/TST"));
    }

    /**
     * Tests the determination of the Sybase platform via its JDBC drivers.
     */
    @Test
    public void testSybaseDriver() {
        assertEquals(DBTypeEnum.SYBASE.getName(), PlatformUtils.determineDatabaseType("com.sybase.jdbc.SybDriver", null));
        assertEquals(DBTypeEnum.SYBASE.getName(), PlatformUtils.determineDatabaseType("com.sybase.jdbc2.jdbc.SybDriver", null));
        // DataDirect Connect
        assertEquals(DBTypeEnum.SYBASE.getName(), PlatformUtils.determineDatabaseType("com.ddtek.jdbc.sybase.SybaseDriver", null));
        // i-net
        assertEquals(DBTypeEnum.SYBASE.getName(), PlatformUtils.determineDatabaseType("com.inet.syb.SybDriver", null));
    }

    /**
     * Tests the determination of the Sybase platform via JDBC connection urls.
     */
    @Test
    public void testSybaseUrl() {
        assertEquals(DBTypeEnum.SYBASE.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:sybase:Tds:xyz:3767orjdbc:sybase:Tds:130.214.90.27:3767"));
        // DataDirect Connect
        assertEquals(DBTypeEnum.SYBASE.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:datadirect:sybase://server2:5000;User=test;Password=secret"));
        // i-net
        assertEquals(DBTypeEnum.SYBASE.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetsyb:www.inetsoftware.de:3333"));
        assertEquals(DBTypeEnum.SYBASE.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetpool:inetsyb:www.inetsoftware.de:3333"));
        assertEquals(DBTypeEnum.SYBASE.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:inetpool:jdbc:inetsyb:www.inetsoftware.de:3333"));
        // jTDS
        assertEquals(DBTypeEnum.SYBASE.getName(), PlatformUtils.determineDatabaseType(null, "jdbc:jtds:sybase://localhost:8080/test"));
    }
}
