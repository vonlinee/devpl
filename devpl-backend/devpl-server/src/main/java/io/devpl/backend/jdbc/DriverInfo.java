package io.devpl.backend.jdbc;

import com.baomidou.mybatisplus.generator.jdbc.JDBCDriver;

import java.sql.Driver;

public class DriverInfo {

    Driver driver;
    String version;
    String filename;
    JDBCDriver driverType;

    public DriverInfo(Driver driver, String version, String filename, JDBCDriver driverType) {
        this.driver = driver;
        this.version = version;
        this.filename = filename;
        this.driverType = driverType;
    }
}
