package io.devpl.generator.jdbc;

import com.baomidou.mybatisplus.generator.jdbc.JDBCDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 每种驱动类名仅支持1个版本
 */
public class JdbcDriverManagerImpl implements JdbcDriverManager {

    Map<String, DriverClassLoader> driverClassLoaderMap = new ConcurrentHashMap<>();
    Map<JDBCDriver, Class<?>> drivers = new HashMap<>();

    @Override
    public boolean isRegisted(String driverClassName) {
        return false;
    }

    @Override
    public void register(String driverClassName) {

    }

    @Override
    public void deregister(String driverClassName) {

    }
}
