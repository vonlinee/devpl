package io.devpl.codegen.jdbc.meta;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @see java.sql.DatabaseMetaData
 */
public class DatabaseInfoMetaData {

    /**
     * @see DatabaseMetaData#getUserName()
     */
    private String userName;
    /**
     * @see DatabaseMetaData#isReadOnly()
     */
    private boolean readOnly;
    /**
     * @see DatabaseMetaData#getDriverVersion()
     */
    private String driverVersion;
    /**
     * @see DatabaseMetaData#getDriverMajorVersion()
     */
    private int driverMajorVersion;
    /**
     * @see DatabaseMetaData#getDriverMinorVersion()
     */
    private int driverMinorVersion;

    /**
     * @see DatabaseMetaData#supportsMixedCaseIdentifiers()
     */
    private boolean supportsMixedCaseIdentifiers;

    public void initialize(DatabaseMetaData dbmd) throws SQLException {
        this.userName = dbmd.getUserName();
        this.readOnly = dbmd.isReadOnly();
        this.driverVersion = dbmd.getDriverVersion();
        this.driverMajorVersion = dbmd.getDriverMajorVersion();
        this.driverMinorVersion = dbmd.getDriverMinorVersion();
        this.supportsMixedCaseIdentifiers = dbmd.supportsMixedCaseIdentifiers();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    public int getDriverMajorVersion() {
        return driverMajorVersion;
    }

    public void setDriverMajorVersion(int driverMajorVersion) {
        this.driverMajorVersion = driverMajorVersion;
    }

    public int getDriverMinorVersion() {
        return driverMinorVersion;
    }

    public void setDriverMinorVersion(int driverMinorVersion) {
        this.driverMinorVersion = driverMinorVersion;
    }

    public boolean isSupportsMixedCaseIdentifiers() {
        return supportsMixedCaseIdentifiers;
    }

    public void setSupportsMixedCaseIdentifiers(boolean supportsMixedCaseIdentifiers) {
        this.supportsMixedCaseIdentifiers = supportsMixedCaseIdentifiers;
    }
}
