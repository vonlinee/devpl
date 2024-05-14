package org.apache.ddlutils.jdbc.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 主键
 */
public class PrimaryKeyMetadata implements JdbcMetadataObject {

    /**
     * String => table catalog (may be null)
     */
    private String tableCatalog;

    /**
     * TABLE_SCHEMA String => table schema (maybe null)
     */
    private String tableSchema;

    /**
     * TABLE_NAME String => table name
     */
    private String tableName;

    /**
     * COLUMNS_NAME String => column name
     */
    private String columnName;

    /**
     * KEY_SEQ short => sequence number within primary key( a value of 1 represents
     * the first column of the primary key, a value of 2 would represent the
     * second column within the primary key).
     */
    private Short primaryKeySequenceNumber;

    /**
     * PK_NAME String => primary key name (may be null)
     */
    private String primaryKeyName;

    /**
     * @param resultSet ResultSet
     * @throws SQLException SQLException
     * @see java.sql.DatabaseMetaData#getPrimaryKeys(String, String, String)
     */
    @Override
    public void initialize(ResultSet resultSet) throws SQLException {
        this.tableCatalog = resultSet.getString(1);
        this.tableSchema = resultSet.getString(2);
        this.tableName = resultSet.getString(3);
        this.columnName = resultSet.getString(4);
        this.primaryKeySequenceNumber = resultSet.getShort(5);
        this.primaryKeyName = resultSet.getString(6);
    }

    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Short getPrimaryKeySequenceNumber() {
        return primaryKeySequenceNumber;
    }

    public void setPrimaryKeySequenceNumber(Short primaryKeySequenceNumber) {
        this.primaryKeySequenceNumber = primaryKeySequenceNumber;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }
}
