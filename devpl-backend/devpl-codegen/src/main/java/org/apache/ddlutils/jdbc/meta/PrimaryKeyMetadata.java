package org.apache.ddlutils.jdbc.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 主键
 */
public class PrimaryKeyMetadata {

    /**
     * String => table catalog (may be null)
     */
    private String tableCat;

    /**
     * String => table schema (maybe null)
     */
    private String tableSchema;

    /**
     * String => table name
     */
    private String tableName;

    /**
     * String => column name
     */
    private String columnName;

    /**
     * short => sequence number within primary key( a value of 1 represents
     * the first column of the primary key, a value of 2 would represent the
     * second column within the primary key).
     */
    private Short keySeq;

    /**
     * String => primary key name (may be null)
     */
    private String primaryKeyName;

    /**
     * @param resultSet ResultSet
     * @throws SQLException SQLException
     * @see java.sql.DatabaseMetaData#getPrimaryKeys(String, String, String)
     */
    public void initialize(ResultSet resultSet) throws SQLException {
        setTableCat(resultSet.getString(1));
        setTableSchema(resultSet.getString(2));
        setTableName(resultSet.getString(3));
        setColumnName(resultSet.getString(4));
        setKeySeq(resultSet.getShort(5));
        setPrimaryKeyName(resultSet.getString(6));
    }

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
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

    public Short getKeySeq() {
        return keySeq;
    }

    public void setKeySeq(Short keySeq) {
        this.keySeq = keySeq;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }
}
