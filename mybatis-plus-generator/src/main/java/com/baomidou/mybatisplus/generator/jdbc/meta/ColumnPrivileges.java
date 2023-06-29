package com.baomidou.mybatisplus.generator.jdbc.meta;

/**
 * @see java.sql.DatabaseMetaData#getColumnPrivileges(String, String, String, String)
 */
public class ColumnPrivileges {

    /**
     * TABLE_CAT String => table catalog (may be null)
     */
    private String tableCat;

    /**
     * TABLE_SCHEM String => table schema (may be null)
     */
    private String tableSchem;

    /**
     * TABLE_NAME String => table name
     */
    private String tableName;

    /**
     * COLUMN_NAME String => column name
     */
    private String columnName;

    /**
     * GRANTOR String => grantor of access (may be null)
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
    private String isGrantable;

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(String tableSchem) {
        this.tableSchem = tableSchem;
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

    public String getIsGrantable() {
        return isGrantable;
    }

    public void setIsGrantable(String isGrantable) {
        this.isGrantable = isGrantable;
    }
}
