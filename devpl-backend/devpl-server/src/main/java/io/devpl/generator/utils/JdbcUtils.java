package io.devpl.generator.utils;

import io.devpl.generator.config.ConnectionInfo;
import io.devpl.generator.config.DbType;
import oracle.jdbc.OracleConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB工具类
 */
public class JdbcUtils {
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
    public static Connection getConnection(ConnectionInfo dataSource) throws SQLException {
        DriverManager.setLoginTimeout(CONNECTION_TIMEOUTS_SECONDS);
        loadDriver(dataSource.getDbType().getDriverClassName());
        Connection connection = DriverManager.getConnection(dataSource.getConnUrl(), dataSource.getUsername(), dataSource.getPassword());
        if (dataSource.getDbType() == DbType.Oracle) {
            ((OracleConnection) connection).setRemarksReporting(true);
        }
        return connection;
    }

    /**
     * 从连接url字符串解析数据库名称
     *
     * @param url 连接url字符串
     * @return 数据库名称，可能为空
     */
    public static String getDatabaseNameFromConnectionUrl(String url) {
        int index = url.lastIndexOf("?");
        int i = index;
        while (url.charAt(i) != '/') {
            i--;
        }
        return url.substring(i + 1, index);
    }
}
