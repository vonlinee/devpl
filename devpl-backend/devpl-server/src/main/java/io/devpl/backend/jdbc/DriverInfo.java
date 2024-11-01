package io.devpl.backend.jdbc;

import org.apache.ddlutils.platform.DriverType;

import java.sql.Driver;

class DriverInfo {

    Driver driver;
    String version;
    String filename;
    DriverType driverType;

    public DriverInfo(Driver driver, String version, String filename, DriverType driverType) {
        this.driver = driver;
        this.version = version;
        this.filename = filename;
        this.driverType = driverType;
    }
}
