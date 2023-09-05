package io.devpl.generator.utils;

import io.devpl.generator.config.DataSourceInfo;
import io.devpl.generator.config.DbType;
import oracle.jdbc.OracleConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB工具类
 */
public class DbUtils {
    private static final int CONNECTION_TIMEOUTS_SECONDS = 6;

    public static void loadDriver(String driverClassName) {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("cannot load driver class " + driverClassName + " becauese it does not exist!");
        }
    }

    /**
     * 获得数据库连接
     */
    public static Connection getConnection(DataSourceInfo dataSource) throws SQLException {
        DriverManager.setLoginTimeout(CONNECTION_TIMEOUTS_SECONDS);
        loadDriver(dataSource.getDbType().getDriverClass());
        Connection connection = DriverManager.getConnection(dataSource.getConnUrl(), dataSource.getUsername(), dataSource.getPassword());
        if (dataSource.getDbType() == DbType.Oracle) {
            ((OracleConnection) connection).setRemarksReporting(true);
        }
        return connection;
    }
}
