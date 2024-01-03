package io.devpl.backend.jdbc;

import io.devpl.codegen.db.JDBCDriver;

import java.sql.Driver;

class DriverInfo {

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
