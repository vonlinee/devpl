package io.devpl.codegen.db;

import lombok.Getter;

/**
 * 支持的数据库类型,主要用于分页方言
 * 抄自com.baomidou.mybatisplus.annotation.DbType
 */
@Getter
public enum DBType {

    /**
     * MYSQL
     */
    MYSQL("MySQL", 3306, "MySQL数据库", JDBCDriver.MYSQL5, JDBCDriver.MYSQL8),
    /**
     * MARIADB
     */
    MARIADB("MariaDB", "MariaDB数据库"),
    /**
     * ORACLE
     */
    ORACLE("Oracle", "Oracle11g及以下数据库(高版本推荐使用ORACLE_NEW)", JDBCDriver.ORACLE),
    /**
     * oracle12c new pagination
     */
    ORACLE_12C("Oracle12c", "Oracle12c+数据库", JDBCDriver.ORACLE_12C),
    /**
     * DB2
     */
    DB2("DB2", "DB2数据库"),
    /**
     * H2
     */
    H2("H2", "H2数据库"),
    /**
     * HSQL
     */
    HSQL("Hsql", "HSQL数据库"),
    /**
     * SQLITE
     */
    SQLITE("Sqlite", "SQLite数据库", JDBCDriver.SQLITE),
    /**
     * POSTGRE
     */
    POSTGRE_SQL("PostgreSQL", "Postgre数据库", JDBCDriver.POSTGRE_SQL),
    /**
     * SQLSERVER2005
     */
    SQL_SERVER2005("SQL Server2005", "SQLServer2005数据库"),
    /**
     * SQLSERVER
     */
    SQL_SERVER("SQL Server", "SQLServer数据库", JDBCDriver.SQL_SERVER),
    /**
     * DM
     */
    DM("达梦数据库", "达梦数据库"),
    /**
     * xugu
     */
    XU_GU("虚谷数据库", "虚谷数据库"),
    /**
     * Kingbase
     */
    KINGBASE_ES("kingbasees", "人大金仓数据库"),
    /**
     * Phoenix
     */
    PHOENIX("phoenix", "Phoenix HBase数据库"),
    /**
     * Gauss
     */
    GAUSS("zenith", "Gauss 数据库"),
    /**
     * ClickHouse
     */
    CLICK_HOUSE("clickhouse", "clickhouse 数据库"),
    /**
     * GBase
     */
    GBASE("gbase", "南大通用(华库)数据库"),
    /**
     * GBase-8s
     */
    GBASE_8S("gbase-8s", "南大通用数据库 GBase 8s"),
    /**
     * Oscar
     */
    OSCAR("oscar", "神通数据库"),
    /**
     * Sybase
     */
    SYBASE("sybase", "Sybase ASE 数据库"),
    /**
     * OceanBase
     */
    OCEAN_BASE("oceanbase", "OceanBase 数据库"),
    /**
     * Firebird
     */
    FIREBIRD("Firebird", "Firebird 数据库"),
    /**
     * HighGo
     */
    HIGH_GO("highgo", "瀚高数据库"),
    /**
     * CUBRID
     */
    CUBRID("cubrid", "CUBRID数据库"),
    /**
     * GOLDILOCKS
     */
    GOLDILOCKS("goldilocks", "GOLDILOCKS数据库"),
    /**
     * CSIIDB
     */
    CSIIDB("csiidb", "CSIIDB数据库"),
    /**
     * CSIIDB
     */
    SAP_HANA("hana", "SAP_HANA数据库"),
    /**
     * Impala
     */
    IMPALA("impala", "impala数据库"),
    /**
     * Vertica
     */
    VERTICA("vertica", "vertica数据库"),
    /**
     * xcloud
     */
    XCloud("行云数据库", "行云数据库"),
    /**
     * UNKONWN DB
     */
    OTHER("other", "其他数据库");

    /**
     * 数据库名称，不区分版本
     */
    private final String name;
    /**
     * 描述
     */
    private final String description;
    /**
     * 支持的驱动列表
     */
    private final JDBCDriver[] drivers;
    /**
     * 默认端口号
     */
    private int defaultPort;

    DBType(String name, String description, JDBCDriver... drivers) {
        this.name = name;
        this.description = description;
        this.drivers = drivers;
    }

    DBType(String name, int port, String description, JDBCDriver... drivers) {
        this.name = name;
        this.defaultPort = port;
        this.description = description;
        this.drivers = drivers;
    }

    /**
     * 获取数据库类型
     *
     * @param dbType 数据库类型字符串
     */
    public static DBType getDbType(String dbType) {
        for (DBType type : DBType.values()) {
            if (type.name.equalsIgnoreCase(dbType)) {
                return type;
            }
        }
        return OTHER;
    }

    public static DBType getValue(String dbType) {
        return getValue(dbType, DBType.MYSQL);
    }

    public static DBType getValue(String dbType, DBType defaultType) {
        if ("MySQL".equalsIgnoreCase(dbType)) {
            return MYSQL;
        }
        if ("Oracle".equalsIgnoreCase(dbType)) {
            return ORACLE;
        }
        if ("PostgreSQL".equalsIgnoreCase(dbType)) {
            return POSTGRE_SQL;
        }
        if ("SQLServer".equalsIgnoreCase(dbType) || "Microsoft SQL Server".equalsIgnoreCase(dbType)) {
            return SQL_SERVER;
        }
        if ("DM".equalsIgnoreCase(dbType) || "DM DBMS".equalsIgnoreCase(dbType)) {
            return DM;
        }
        if ("Clickhouse".equalsIgnoreCase(dbType)) {
            return CLICK_HOUSE;
        }
        return defaultType;
    }

    public String getDriverClassName() {
        return getDriverClassName(0);
    }

    public String getDriverClassName(int index) {
        JDBCDriver driver = getDriver(index);
        return driver == null ? null : driver.getDriverClassName();
    }

    public JDBCDriver getDriver() {
        return getDriver(0);
    }

    public JDBCDriver getDriver(int index) {
        if (drivers == null || drivers.length == 0) {
            return null;
        }
        if (index > drivers.length - 1) {
            index = 0;
        }
        return drivers[index];
    }

}
