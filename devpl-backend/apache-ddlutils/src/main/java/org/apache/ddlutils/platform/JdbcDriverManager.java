package org.apache.ddlutils.platform;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 驱动管理
 * 根据驱动类名唯一表示一个驱动实例
 */
public interface JdbcDriverManager {

    /**
     * 获取数据库连接
     *
     * @param driverClassName 驱动类名
     * @param jdbcUrl         JDBC驱动连接地址
     * @param username        用户名
     * @param password        密码
     * @param properties      连接属性
     * @return 数据库连接
     * @throws RuntimeException 获取连接失败
     */
    Connection getConnection(String driverClassName, String jdbcUrl, String username, String password, Properties properties) throws SQLException;

    /**
     * 驱动是否注册
     *
     * @param driverClassName 驱动类名
     * @return 是否已注册
     */
    boolean isRegistered(String driverClassName);

    /**
     * 取消注册驱动
     *
     * @param driverClassName 驱动类名
     */
    void deregister(String driverClassName);
}
