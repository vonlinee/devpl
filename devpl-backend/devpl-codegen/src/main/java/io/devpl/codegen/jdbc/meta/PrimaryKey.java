package io.devpl.codegen.jdbc.meta;

/**
 * 主键
 */
public class PrimaryKey {

    /**
     * String => table catalog (may be null)
     */
    private String tableCat;

    /**
     * String => table schema (maybe null)
     */
    private String tableSchem;

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
    private String pkName;

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

    public Short getKeySeq() {
        return keySeq;
    }

    public void setKeySeq(Short keySeq) {
        this.keySeq = keySeq;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    @Override
    public String toString() {
        return "PrimaryKey{" + "tableCat='" + tableCat + '\'' +
            ", tableSchem='" + tableSchem + '\'' +
            ", tableName='" + tableName + '\'' +
            ", columnName='" + columnName + '\'' +
            ", keySeq=" + keySeq +
            ", pkName='" + pkName + '\'' +
            '}';
    }
}