package io.devpl.codegen.jdbc.meta;

import lombok.Getter;
import lombok.Setter;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @see DatabaseMetaData#getImportedKeys(String, String, String)
 */
@Getter
@Setter
public class ForeignKeyMetadata implements JdbcMetadataObject {
    /**
     * PKTABLE_CAT String => primary key table catalog being imported (maybe null)
     */
    private String primaryKeyTableCatalog;
    /**
     * PKTABLE_SCHEM String => primary key table schema being imported (maybe null)
     */
    private String primaryKeyTableSchema;
    /**
     * PKTABLE_NAME String => primary key table name being imported
     */
    private String primaryKeyTableName;
    /**
     * PKCOLUMN_NAME String => primary key column name being imported
     */
    private String primaryKeyColumnName;
    /**
     * FKTABLE_CAT String => foreign key table catalog (may be null)
     */
    private String foriegnKeyTableCatalog;
    /**
     * FKTABLE_SCHEM String => foreign key table schema (maybe null)
     */
    private String foriegnKeyTableSchema;
    /**
     * FKTABLE_NAME String => foreign key table name
     */
    private String foriegnKeyTableName;
    /**
     * FKCOLUMN_NAME String => foreign key column name
     */
    private String foriegnKeyColumnName;
    /**
     * KEY_SEQ short => sequence number within a foreign key( a value of 1 represents the first column of the foreign key, a value of 2 would represent the second column within the foreign key).
     */
    private String keySequence;
    /**
     * UPDATE_RULE short => What happens to a foreign key when the primary key is updated:
     * importedNoAction - do not allow update of primary key if it has been imported
     * importedKeyCascade - change imported key to agree with primary key update
     * importedKeySetNull - change imported key to NULL if its primary key has been updated
     * importedKeySetDefault - change imported key to default values if its primary key has been updated
     * importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
     */
    private short updateRule;
    /**
     * DELETE_RULE short => What happens to the foreign key when primary is deleted.
     * importedKeyNoAction - do not allow to delete of primary key if it has been imported
     * importedKeyCascade - delete rows that import a deleted key
     * importedKeySetNull - change imported key to NULL if its primary key has been deleted
     * importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
     * importedKeySetDefault - change imported key to default if its primary key has been deleted
     */
    private short deleteRule;
    /**
     * FK_NAME String => foreign key name (maybe null)
     */
    private String foriegnKeyName;
    /**
     * PK_NAME String => primary key name (may be null)
     */
    private String primaryKeyName;
    /**
     * DEFERRABILITY short => can the evaluation of foreign key constraints be deferred until commit
     * importedKeyInitiallyDeferred - see SQL92 for definition
     * importedKeyInitiallyImmediate - see SQL92 for definition
     * importedKeyNotDeferrable - see SQL92 for definition
     */
    private short deferrability;

    @Override
    public void initialize(ResultSet resultSet) throws SQLException {
        primaryKeyTableCatalog = resultSet.getString("");
        primaryKeyTableCatalog = resultSet.getString("");
        primaryKeyTableCatalog = resultSet.getString("");
        primaryKeyTableCatalog = resultSet.getString("");
        primaryKeyTableCatalog = resultSet.getString("");
        primaryKeyTableCatalog = resultSet.getString("");
        primaryKeyTableCatalog = resultSet.getString("");
        primaryKeyTableCatalog = resultSet.getString("");
        primaryKeyTableCatalog = resultSet.getString("");
        primaryKeyTableCatalog = resultSet.getString("");
        primaryKeyTableCatalog = resultSet.getString("");
        primaryKeyTableCatalog = resultSet.getString("");
        primaryKeyTableCatalog = resultSet.getString("");
        primaryKeyTableCatalog = resultSet.getString("");
    }
}
