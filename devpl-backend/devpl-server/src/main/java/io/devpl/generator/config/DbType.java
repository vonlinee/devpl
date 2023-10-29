package io.devpl.generator.config;

/**
 * 数据库类型 枚举
 */
public enum DbType {
    MySQL("MySQL", "com.mysql.cj.jdbc.Driver"),
    Oracle("Oracle", "oracle.jdbc.driver.OracleDriver"),
    PostgreSQL("PostgreSQL", "org.postgresql.Driver"),
    SQLServer("SQL Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    DM("达梦", "dm.jdbc.driver.DmDriver"),
    Clickhouse("Clickhouse", "com.clickhouse.jdbc.ClickHouseDriver");

    /**
     * 数据库名称
     */
    private final String name;

    /**
     * 该数据库使用的驱动
     */
    private final String driverClassName;

    DbType(String name, String driverClass) {
        this.name = name;
        this.driverClassName = driverClass;
    }

    public static DbType getValue(String dbType) {
        return getValue(dbType, DbType.MySQL);
    }

    public static DbType getValue(String dbType, DbType defaultType) {
        if (dbType == null || dbType.isBlank()) {
            return defaultType;
        }
        if ("MySQL".equalsIgnoreCase(dbType)) {
            return MySQL;
        }
        if ("Oracle".equalsIgnoreCase(dbType)) {
            return Oracle;
        }
        if ("PostgreSQL".equalsIgnoreCase(dbType)) {
            return PostgreSQL;
        }
        if ("SQLServer".equalsIgnoreCase(dbType) || "Microsoft SQL Server".equalsIgnoreCase(dbType)) {
            return SQLServer;
        }
        if ("DM".equalsIgnoreCase(dbType) || "DM DBMS".equalsIgnoreCase(dbType)) {
            return DM;
        }
        if ("Clickhouse".equalsIgnoreCase(dbType)) {
            return Clickhouse;
        }
        return defaultType;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getName() {
        return name;
    }
}
