package com.baomidou.mybatisplus.generator.jdbc;

import java.util.Objects;

/**
 * 支持的数据库类型,主要用于分页方言
 */
public enum DbType {

    /**
     * MYSQL
     */
    MYSQL("mysql", "MySql数据库", JDBCDriver.MYSQL5, JDBCDriver.MYSQL8),
    /**
     * MARIADB
     */
    MARIADB("mariadb", "MariaDB数据库"),
    /**
     * ORACLE
     */
    ORACLE("oracle", "Oracle11g及以下数据库(高版本推荐使用ORACLE_NEW)", JDBCDriver.ORACLE),
    /**
     * oracle12c new pagination
     */
    ORACLE_12C("oracle12c", "Oracle12c+数据库", JDBCDriver.ORACLE),
    /**
     * DB2
     */
    DB2("db2", "DB2数据库"),
    /**
     * H2
     */
    H2("h2", "H2数据库"),
    /**
     * HSQL
     */
    HSQL("hsql", "HSQL数据库"),
    /**
     * SQLITE
     */
    SQLITE("sqlite", "SQLite数据库", JDBCDriver.SQLITE),
    /**
     * POSTGRE
     */
    POSTGRE_SQL("postgresql", "Postgre数据库", JDBCDriver.POSTGRE_SQL),
    /**
     * SQLSERVER2005
     */
    SQL_SERVER2005("sqlserver2005", "SQLServer2005数据库"),
    /**
     * SQLSERVER
     */
    SQL_SERVER("sqlserver", "SQLServer数据库", JDBCDriver.SQL_SERVER),
    /**
     * DM
     */
    DM("dm", "达梦数据库"),
    /**
     * xugu
     */
    XU_GU("xugu", "虚谷数据库"),
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
     * use {@link  #GBASE_8S}
     *
     * @deprecated 2022-05-30
     */
    @Deprecated GBASEDBT("gbasedbt", "南大通用数据库"),
    /**
     * use {@link  #GBASE_8S}
     *
     * @deprecated 2022-05-30
     */
    @Deprecated GBASE_INFORMIX("gbase 8s", "南大通用数据库 GBase 8s"),
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
    XCloud("xcloud", "行云数据库"),
    /**
     * UNKONWN DB
     */
    OTHER("other", "其他数据库");

    /**
     * 数据库名称
     */
    private final String db;

    /**
     * 描述
     */
    private final String desc;

    private final JDBCDriver[] drivers;

    DbType(String db, String desc) {
        this(db, desc, (JDBCDriver[]) null);
    }

    DbType(String db, String desc, JDBCDriver... drivers) {
        this.db = db;
        this.desc = desc;
        this.drivers = drivers;
    }

    /**
     * 获取数据库类型
     *
     * @param dbType 数据库类型字符串
     */
    public static DbType getDbType(String dbType) {
        for (DbType type : DbType.values()) {
            if (type.db.equalsIgnoreCase(dbType)) {
                return type;
            }
        }
        return OTHER;
    }

    public static DbType getValue(String dbType) {
        return getValue(dbType, DbType.MYSQL);
    }

    public static DbType getValue(String dbType, DbType defaultType) {
        if (Objects.equals(dbType, "MySQL")) {
            return MYSQL;
        }
        if (Objects.equals(dbType, "Oracle")) {
            return ORACLE;
        }
        if (Objects.equals(dbType, "PostgreSQL")) {
            return POSTGRE_SQL;
        }
        if (Objects.equals(dbType, "SQLServer") || Objects.equals(dbType, "Microsoft SQL Server")) {
            return SQL_SERVER;
        }
        if (Objects.equals(dbType, "DM") || Objects.equals(dbType, "DM DBMS")) {
            return DM;
        }
        if (Objects.equals(dbType, "Clickhouse")) {
            return CLICK_HOUSE;
        }
        return defaultType;
    }

    public String getDriverClassName() {
        if (drivers == null || drivers.length == 0) {
            return null;
        }
        return drivers[0].getDriverClassName();
    }

    public String getDescription() {
        return desc;
    }
}
