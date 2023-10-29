package io.devpl.generator.utils;

import io.devpl.generator.config.DbType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Jdbc工具类
 */
public class JdbcUtils {

    private static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);

    private static final int CONNECTION_TIMEOUTS_SECONDS = 6;

    public static void loadDriver(String driverClassName) {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("cannot load driver class " + driverClassName + " becauese it does not exist!");
        }
    }

    /**
     * 获取数据库连接
     *
     * @param url      链接地址
     * @param username 用户名
     * @param password 密码
     * @param dbType   驱动类型
     * @return JDBC链接
     * @throws SQLException 连接失败
     */
    public static Connection getConnection(String url, String username, String password, DbType dbType) throws SQLException {
        DriverManager.setLoginTimeout(CONNECTION_TIMEOUTS_SECONDS);
        loadDriver(dbType.getDriverClassName());
        Connection connection = DriverManager.getConnection(url, username, password);
//        if (dbType == DbType.Oracle) {
//            /**
//             * https://docs.oracle.com/database/121/JAJDB/oracle/jdbc/OracleConnection.html#setRemarksReporting_boolean_
//             */
//            ((OracleConnection) connection).setRemarksReporting(true);
//        }
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

    /**
     * 提取单列的值，不关闭ResultSet
     *
     * @param resultSet ResultSet
     * @param result    存储结果的列表
     * @param <T>       该列数据类型
     * @return 该列的结果
     */
    public static <T> List<T> extractSingleColumn(ResultSet resultSet, List<T> result) {
        SingleColumnRowMapper<T> rowMapper = new SingleColumnRowMapper<>();
        int rowNum = 0;
        try {
            if (resultSet.isClosed()) {
                return result;
            }
            while (resultSet.next()) {
                result.add(rowMapper.mapRow(resultSet, rowNum++));
            }
        } catch (SQLException sqlException) {
            logger.info("error when extract single column of resultset", sqlException);
        }
        return result;
    }
}
