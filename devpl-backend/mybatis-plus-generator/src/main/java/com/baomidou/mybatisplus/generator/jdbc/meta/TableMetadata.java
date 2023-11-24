package com.baomidou.mybatisplus.generator.jdbc.meta;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 仅包含表的元数据信息，且所有字段和DatabaseMetaData#getTables返回值一致
 * <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/">JDBC specification</a>
 *
 * @see java.sql.DatabaseMetaData#getTables(String, String, String, String[])
 */
public final class TableMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 7207765860662369964L;

    /**
     * TABLE_CAT String => table catalog (may be null)
     */
    private String tableCat;

    /**
     * TABLE_SCHEM String => table schema (maybe null)
     */
    private String tableSchem;

    /**
     * TABLE_NAME String => table name
     */
    private String tableName;

    /**
     * TABLE_TYPE String => table type. Typical types are
     * "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
     */
    private String tableType;

    /**
     * String => explanatory comment on the table (maybe null)
     */
    private String remarks;

    /**
     * String => the types catalog (maybe null)
     */
    private String typeCat;

    /**
     * String => the types schema (maybe null)
     */
    private String typeSchem;

    /**
     * TYPE_NAME String => type name (maybe null)
     */
    private String typeName;

    /**
     * SELF_REFERENCING_COL_NAME String => name of the designated "identifier" column of a typed table (may be null)
     */
    private String selfReferencingColName;

    /**
     * REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)
     */
    private String refGeneration;

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

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(String typeCat) {
        this.typeCat = typeCat;
    }

    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(String typeSchem) {
        this.typeSchem = typeSchem;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSelfReferencingColName() {
        return selfReferencingColName;
    }

    public void setSelfReferencingColName(String selfReferencingColName) {
        this.selfReferencingColName = selfReferencingColName;
    }

    public String getRefGeneration() {
        return refGeneration;
    }

    public void setRefGeneration(String refGeneration) {
        this.refGeneration = refGeneration;
    }

    @Override
    public String toString() {
        return "TableMetadata{" + "tableCat='" + tableCat + '\'' + ", tableSchem='" + tableSchem + '\'' + ", tableName='" + tableName + '\'' + ", tableType='" + tableType + '\'' + ", remarks='" + remarks + '\'' + ", typeCat='" + typeCat + '\'' + ", typeSchem='" + typeSchem + '\'' + ", typeName='" + typeName + '\'' + ", selfReferencingColName='" + selfReferencingColName + '\'' + ", refGeneration='" + refGeneration + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableMetadata that = (TableMetadata) o;
        return Objects.equals(tableCat, that.tableCat) && Objects.equals(tableSchem, that.tableSchem) && Objects.equals(tableName, that.tableName) && Objects.equals(tableType, that.tableType) && Objects.equals(remarks, that.remarks) && Objects.equals(typeCat, that.typeCat) && Objects.equals(typeSchem, that.typeSchem) && Objects.equals(typeName, that.typeName) && Objects.equals(selfReferencingColName, that.selfReferencingColName) && Objects.equals(refGeneration, that.refGeneration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCat, tableSchem, tableName, tableType, remarks, typeCat, typeSchem, typeName, selfReferencingColName, refGeneration);
    }
}
