package io.devpl.codegen.jdbc.meta;

import lombok.Data;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @see java.sql.DatabaseMetaData
 */
@Data
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
}
