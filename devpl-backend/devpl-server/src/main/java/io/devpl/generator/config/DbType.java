package io.devpl.generator.config;

import io.devpl.sdk.util.StringUtils;

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

    private final String name;
    private final String driverClassName;

    DbType(String name, String driverClass) {
        this.name = name;
        this.driverClassName = driverClass;
    }

    public static DbType getValue(String dbType) {
        return getValue(dbType, DbType.MySQL);
    }

    public static DbType getValue(String dbType, DbType defaultType) {
        if (StringUtils.equalsAny(dbType, "MySQL")) {
            return MySQL;
        }
        if (StringUtils.equalsAny(dbType, "Oracle")) {
            return Oracle;
        }
        if (StringUtils.equalsAny(dbType, "PostgreSQL")) {
            return PostgreSQL;
        }
        if (StringUtils.equalsAny(dbType, "SQLServer", "Microsoft SQL Server")) {
            return SQLServer;
        }
        if (StringUtils.equalsAny(dbType, "DM", "DM DBMS")) {
            return DM;
        }
        if (StringUtils.equalsAny(dbType, "Clickhouse")) {
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
