package io.devpl.generator.jdbc;

/**
 * 驱动管理
 */
public interface JdbcDriverManager {

    boolean isRegisted(String driverClassName);

    void register(String driverClassName);

    void deregister(String driverClassName);
}
