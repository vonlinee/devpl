package io.devpl.backend.jdbc;

import org.apache.ddlutils.platform.JDBCDriverType;

import java.sql.Driver;

class DriverInfo {

    Driver driver;
    String version;
    String filename;
    JDBCDriverType driverType;

    public DriverInfo(Driver driver, String version, String filename, JDBCDriverType driverType) {
        this.driver = driver;
        this.version = version;
        this.filename = filename;
        this.driverType = driverType;
    }
}
