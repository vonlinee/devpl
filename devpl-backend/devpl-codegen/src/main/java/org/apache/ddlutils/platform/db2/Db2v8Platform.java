package org.apache.ddlutils.platform.db2;


/**
 * The DB2 platform implementation for DB2 v8 and above.
 */
public class Db2v8Platform extends Db2Platform {

    /**
     * Creates a new platform instance.
     */
    public Db2v8Platform() {
        super();
        // DB2 v8 has a maximum identifier length of 128 bytes for things like table names,
        // stored procedure names etc., 30 bytes for column names and 18 bytes for foreign key names
        // Note that we optimistically assume that number of characters = number of bytes
        // If the name contains characters that are more than one byte in the database's
        // encoding, then the db will report an error anyway, but we cannot really calculate
        // the number of bytes
        getPlatformInfo().setMaxIdentifierLength(128);
        getPlatformInfo().setMaxColumnNameLength(30);
        getPlatformInfo().setMaxConstraintNameLength(18);
        getPlatformInfo().setMaxForeignKeyNameLength(18);
        setSqlBuilder(new Db2v8Builder(this));
    }
}
