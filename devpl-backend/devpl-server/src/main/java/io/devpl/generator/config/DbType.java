package io.devpl.generator.config;

import com.baomidou.mybatisplus.generator.jdbc.JDBCDriver;
import io.devpl.sdk.util.StringUtils;

/**
 * 数据库类型 枚举
 */
public enum DbType {
    MySQL("com.mysql.cj.jdbc.Driver"),
    Oracle("oracle.jdbc.driver.OracleDriver"),
    PostgreSQL("org.postgresql.Driver"),
    SQLServer("com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    DM("dm.jdbc.driver.DmDriver"),
    Clickhouse("com.clickhouse.jdbc.ClickHouseDriver");

    private final String driverClass;
    private JDBCDriver driver;

    DbType(String driverClass) {
        this.driverClass = driverClass;
    }

    DbType(String driverClass, JDBCDriver driver) {
        this.driverClass = driverClass;
        this.driver = driver;
    }

    public static DbType getValue(String dbType) {
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
        return null;
    }

    public String getDriverClass() {
        return driverClass;
    }
}
