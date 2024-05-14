package org.apache.ddlutils.jdbc.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @see DatabaseMetaData#getImportedKeys(String, String, String)
 */
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
    private String foreignKeyTableCatalog;
    /**
     * FKTABLE_SCHEM String => foreign key table schema (maybe null)
     */
    private String foreignKeyTableSchema;
    /**
     * FKTABLE_NAME String => foreign key table name
     */
    private String foreignKeyTableName;
    /**
     * FKCOLUMN_NAME String => foreign key column name
     */
    private String foreignKeyColumnName;
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
    private String foreignKeyName;
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

    public String getPrimaryKeyTableCatalog() {
        return primaryKeyTableCatalog;
    }

    public void setPrimaryKeyTableCatalog(String primaryKeyTableCatalog) {
        this.primaryKeyTableCatalog = primaryKeyTableCatalog;
    }

    public String getPrimaryKeyTableSchema() {
        return primaryKeyTableSchema;
    }

    public void setPrimaryKeyTableSchema(String primaryKeyTableSchema) {
        this.primaryKeyTableSchema = primaryKeyTableSchema;
    }

    public String getPrimaryKeyTableName() {
        return primaryKeyTableName;
    }

    public void setPrimaryKeyTableName(String primaryKeyTableName) {
        this.primaryKeyTableName = primaryKeyTableName;
    }

    public String getPrimaryKeyColumnName() {
        return primaryKeyColumnName;
    }

    public void setPrimaryKeyColumnName(String primaryKeyColumnName) {
        this.primaryKeyColumnName = primaryKeyColumnName;
    }

    public String getForeignKeyTableCatalog() {
        return foreignKeyTableCatalog;
    }

    public void setForeignKeyTableCatalog(String foreignKeyTableCatalog) {
        this.foreignKeyTableCatalog = foreignKeyTableCatalog;
    }

    public String getForeignKeyTableSchema() {
        return foreignKeyTableSchema;
    }

    public void setForeignKeyTableSchema(String foreignKeyTableSchema) {
        this.foreignKeyTableSchema = foreignKeyTableSchema;
    }

    public String getForeignKeyTableName() {
        return foreignKeyTableName;
    }

    public void setForeignKeyTableName(String foreignKeyTableName) {
        this.foreignKeyTableName = foreignKeyTableName;
    }

    public String getForeignKeyColumnName() {
        return foreignKeyColumnName;
    }

    public void setForeignKeyColumnName(String foreignKeyColumnName) {
        this.foreignKeyColumnName = foreignKeyColumnName;
    }

    public String getKeySequence() {
        return keySequence;
    }

    public void setKeySequence(String keySequence) {
        this.keySequence = keySequence;
    }

    public short getUpdateRule() {
        return updateRule;
    }

    public void setUpdateRule(short updateRule) {
        this.updateRule = updateRule;
    }

    public short getDeleteRule() {
        return deleteRule;
    }

    public void setDeleteRule(short deleteRule) {
        this.deleteRule = deleteRule;
    }

    public String getForeignKeyName() {
        return foreignKeyName;
    }

    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    public short getDeferrability() {
        return deferrability;
    }

    public void setDeferrability(short deferrability) {
        this.deferrability = deferrability;
    }
}
