package org.apache.ddlutils.jdbc.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @see java.sql.DatabaseMetaData#getColumnPrivileges(String, String, String, String)
 */
public class ColumnPrivilegesMetadata {

    /**
     * TABLE_CAT String => table catalog (may be null)
     */
    private String tableCat;

    /**
     * TABLE_SCHEM String => table schema (maybe null)
     */
    private String tableSchema;

    /**
     * TABLE_NAME String => table name
     */
    private String tableName;

    /**
     * COLUMN_NAME String => column name
     */
    private String columnName;

    /**
     * GRANTOR String => grantor of access (maybe null)
     */
    private String grantor;

    /**
     * GRANTEE String => grantee of access
     */
    private String grantee;

    /**
     * PRIVILEGE String => name of access (SELECT, INSERT, UPDATE, REFERENCES, ...)
     */
    private String privilege;

    /**
     * IS_GRANTABLE String => "YES" if grantee is permitted to grant to others; "NO" if not; null if unknown
     */
    private String grantable;

    public void initialize(ResultSet resultSet) throws SQLException {
        setTableCat(resultSet.getString(1));
        setTableSchema(resultSet.getString(2));
        setTableName(resultSet.getString(3));
        setColumnName(resultSet.getString(4));
        setGrantor(resultSet.getString(5));
        setGrantee(resultSet.getString(6));
        setPrivilege(resultSet.getString(7));
        setGrantable(resultSet.getString(8));
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

    public String getGrantor() {
        return grantor;
    }

    public void setGrantor(String grantor) {
        this.grantor = grantor;
    }

    public String getGrantee() {
        return grantee;
    }

    public void setGrantee(String grantee) {
        this.grantee = grantee;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getGrantable() {
        return grantable;
    }

    public void setGrantable(String grantable) {
        this.grantable = grantable;
    }
}
