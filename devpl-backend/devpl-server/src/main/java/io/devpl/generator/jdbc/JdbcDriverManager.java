package io.devpl.generator.jdbc;

import java.sql.Connection;
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
     * @param url             链接地址
     * @param username        用户名
     * @param password        密码
     * @param properties      连接属性
     * @return 数据库连接
     * @throws RuntimeException 获取连接失败
     */
    Connection getConnection(String driverClassName, String url, String username, String password, Properties properties);

    /**
     * 驱动是否注册
     *
     * @param driverClassName 驱动类名
     * @return 是否已注册
     */
    boolean isRegisted(String driverClassName);

    /**
     * 取消注册驱动
     *
     * @param driverClassName 驱动类名
     */
    void deregister(String driverClassName);
}
