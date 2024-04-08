package org.apache.ddlutils.platform;

/**
 * 对应平台类型
 * TODO 已经废弃的数据库不再使用
 */
public enum DBTypeEnum implements DBType {

    /**
     * Axion <a href="https://db.apache.org/ddlutils/databases/axion.html">...</a>
     * <p>
     * the axion project was abandoned in 2006.
     */
    AXION("Axion"),
    /**
     * Cloudscape
     */
    CLOUDSCAPE("Cloudscape"),
    /**
     * <a href="https://www.ibm.com/products/db2">DB2</a>
     */
    DB2("DB2"),

    DB2V8("DB2v8"),

    /**
     * Microsoft Sql Server
     */
    MSSQL("MsSql"),

    SYBASE("Sybase"),
    SYBASE_ASE15("SybaseASE15"),

    /**
     * Oracle Database
     */
    ORACLE8("Oracle"),
    ORACLE9("Oracle9"),
    ORACLE10("Oracle10"),

    FIREBIRD("Firebird"),
    HSQLDB("HsqlDb"),
    INTERBASE("Interbase"),
    /**
     * Database name of this platform.
     */
    SAPDB("SapDB"),
    MCKOI("McKoi"),
    DERBY("Derby"),
    MAXDB("MaxDB"),

    POSTGRE_SQL("PostgreSql"),
    /**
     * MySQL Database
     */
    MYSQL("MySQL"),
    MYSQL5("MySQL5");

    private final String name;

    DBTypeEnum(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
