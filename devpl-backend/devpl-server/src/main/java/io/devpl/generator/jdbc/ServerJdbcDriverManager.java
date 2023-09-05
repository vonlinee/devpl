package io.devpl.generator.jdbc;

import com.baomidou.mybatisplus.generator.jdbc.JDBCDriver;

import java.util.HashMap;
import java.util.Map;

public class ServerJdbcDriverManager implements JdbcDriverManager {

    Map<JDBCDriver, Class<?>> drivers = new HashMap<>();

    @Override
    public boolean isRegisted(String driverClassName) {
        return false;
    }

    @Override
    public void register(String driverClassName) {

    }
}
