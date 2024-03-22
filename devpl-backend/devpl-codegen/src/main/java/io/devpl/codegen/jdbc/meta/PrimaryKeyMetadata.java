package io.devpl.codegen.jdbc.meta;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 主键
 */
@Data
public class PrimaryKeyMetadata {

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
    public void initialize(ResultSet resultSet) throws SQLException {
        setTableCatalog(resultSet.getString(1));

        this.tableSchema = resultSet.getString(2);
        setTableName(resultSet.getString(3));
        setColumnName(resultSet.getString(4));
        setPrimaryKeySequenceNumber(resultSet.getShort(5));

        this.primaryKeyName = resultSet.getString(6);
    }
}
