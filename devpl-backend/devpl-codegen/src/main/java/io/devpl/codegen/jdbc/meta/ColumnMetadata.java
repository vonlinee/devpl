package io.devpl.codegen.jdbc.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * metadata of a column in a database table
 *
 * @see java.sql.DatabaseMetaData#getColumns(String, String, String, String)
 */
public class ColumnMetadata {

    /**
     * TABLE_CAT String => table catalog (maybe null)
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
     * COLUMN_NAME String => column name
     */
    private String columnName;

    /**
     * DATA_TYPE int => SQL type from java.sql.Type
     */
    private Integer dataType;

    /**
     * TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
     */
    private String typeName;

    /**
     * 有符号数长度会减少1，比如bigint(20)，此时columnSize=19
     * COLUMN_SIZE int => column size.
     *
     * @see java.sql.DatabaseMetaData#getColumns(String, String, String, String)
     */
    private Integer columnSize;

    /**
     * BUFFER_LENGTH is not used.
     */
    private Integer bufferLength;

    /**
     * DECIMAL_DIGITS int => the number of fractional digits(小数位数). Null is returned for data
     * types where DECIMAL_DIGITS is not applicable, such as the {@code java.sql.Typse#INT}.
     */
    private Integer decimalDigits;

    /**
     * NUM_PREC_RADIX int => Radix (typically either 10 or 2) (基数,即十进制或者二进制)
     */
    private Integer numPrecRadix;

    /**
     * NULLABLE int => is NULL allowed.
     * 0 - Indicates that the column definitely allows NULL values.
     * 1 - Indicates that the column definitely allows NULL values.
     * 2 - Indicates that the nullability of columns is unknown.
     *
     * @see java.sql.DatabaseMetaData#columnNoNulls
     * @see java.sql.DatabaseMetaData#columnNullable
     * @see java.sql.DatabaseMetaData#columnNullableUnknown
     */
    private Integer nullable;

    /**
     * REMARKS String => comment describing column (may be null)
     */
    private String remarks;

    /**
     * COLUMN_DEF String => default value for the column, which
     * should be interpreted as a string when the value is enclosed in single quotes (maybe null)
     */
    private String columnDef;

    /**
     * SQL_DATA_TYPE int => unused
     */
    private Integer sqlDataType;

    /**
     * SQL_DATETIME_SUB int => unused
     */
    private Integer sqlDatetimeSub;

    /**
     * 字符类型的最大字节数
     * CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
     */
    private Integer charOctetLength;

    /**
     * 在表中的位置
     * ORDINAL_POSITION int => index of column in table (starting at 1)
     */
    private Integer ordinalPosition;

    /**
     * IS_NULLABLE String => ISO rules are used to determine the nullability for a column.
     * YES --- if the column can include NULLs
     * NO --- if the column cannot include NULLs
     * empty string --- if the nullability for the column is unknown
     *
     * @see ColumnMetadata#nullable
     */
    private String isNullable;

    /**
     * SCOPE_CATALOG String => catalog of table that is the scope
     * of a reference attribute (null if DATA_TYPE isn't REF)
     */
    private String scopeCatalog;

    /**
     * SCOPE_SCHEMA String => schema of table that is the scope of a
     * reference attribute (null if the DATA_TYPE isn't REF)
     */
    private String scopeSchema;

    /**
     * SCOPE_TABLE String => table name that this the scope of a reference
     * attribute (null if the DATA_TYPE isn't REF)
     */
    private String scopeTable;

    /**
     * SOURCE_DATA_TYPE short => source type of distinct type or user-generated Ref type,
     * SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
     */
    private Short sourceDataType;

    /**
     * IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
     * YES --- if the column is auto incremented
     * NO --- if the column is not auto incremented
     * empty string --- if it cannot be determined whether the column is auto incremented
     */
    private String isAutoincrement;

    /**
     * IS_GENERATEDCOLUMN String => Indicates whether this is a generated column
     * YES --- if this a generated column
     * NO --- if this not a generated column
     * empty string --- if it cannot be determined whether this is a generated column
     */
    private String isGeneratedcolumn;

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

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }

    public Integer getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(Integer bufferLength) {
        this.bufferLength = bufferLength;
    }

    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public Integer getNumPrecRadix() {
        return numPrecRadix;
    }

    public void setNumPrecRadix(Integer numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    public Integer getNullable() {
        return nullable;
    }

    /**
     * JDBC使用 setter/getter 方法来确定字段
     *
     * @param nullable 是否可为null
     */
    public void setNullable(Integer nullable) {
        this.nullable = nullable;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * for the api readability
     *
     * @return the default value of this column
     */
    public String getDefaultValue() {
        return columnDef;
    }

    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(String columnDef) {
        this.columnDef = columnDef;
    }

    public Integer getSqlDataType() {
        return sqlDataType;
    }

    public void setSqlDataType(Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    public void setSqlDatetimeSub(Integer sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getScopeCatalog() {
        return scopeCatalog;
    }

    public void setScopeCatalog(String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }

    public String getScopeSchema() {
        return scopeSchema;
    }

    public void setScopeSchema(String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }

    public String getScopeTable() {
        return scopeTable;
    }

    public void setScopeTable(String scopeTable) {
        this.scopeTable = scopeTable;
    }

    public Short getSourceDataType() {
        return sourceDataType;
    }

    public void setSourceDataType(Short sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    /**
     * isAutoincrement cannot be null
     *
     * @return if this column is autoincrement, true, or else false
     */
    public boolean isAutoIncrement() {
        return "YES".equals(isAutoincrement);
    }

    public String getIsAutoincrement() {
        return isAutoincrement;
    }

    public void setIsAutoincrement(String isAutoincrement) {
        this.isAutoincrement = isAutoincrement;
    }

    public String getIsGeneratedcolumn() {
        return isGeneratedcolumn;
    }

    public void setIsGeneratedcolumn(String isGeneratedcolumn) {
        this.isGeneratedcolumn = isGeneratedcolumn;
    }

    @Override
    public String toString() {
        return "ColumnMetadata{" + "tableCat='" + tableCat + '\'' +
            ", tableSchem='" + tableSchem + '\'' +
            ", tableName='" + tableName + '\'' +
            ", columnName='" + columnName + '\'' +
            ", dataType=" + dataType +
            ", typeName='" + typeName + '\'' +
            ", columnSize=" + columnSize +
            ", bufferLength=" + bufferLength +
            ", decimalDigits='" + decimalDigits + '\'' +
            ", numPrecRadix=" + numPrecRadix +
            ", nullable=" + nullable +
            ", remarks='" + remarks + '\'' +
            ", columnDef='" + columnDef + '\'' +
            ", sqlDataType=" + sqlDataType +
            ", sqlDatetimeSub=" + sqlDatetimeSub +
            ", charOctetLength=" + charOctetLength +
            ", ordinalPosition=" + ordinalPosition +
            ", isNullable='" + isNullable + '\'' +
            ", scopeCatalog='" + scopeCatalog + '\'' +
            ", scopeSchema='" + scopeSchema + '\'' +
            ", scopeTable='" + scopeTable + '\'' +
            ", sourceDataType='" + sourceDataType + '\'' +
            ", isAutoincrement='" + isAutoincrement + '\'' +
            ", isGeneratedcolumn='" + isGeneratedcolumn + '\'' +
            '}';
    }

    public void initialize(ResultSet resultSet) throws SQLException {
        this.tableCat = resultSet.getString("TABLE_CAT");
        this.tableSchem = resultSet.getString("TABLE_SCHEM");
        this.tableName = resultSet.getString("TABLE_NAME");
        this.columnName = resultSet.getString("COLUMN_NAME");
        this.dataType = resultSet.getInt("DATA_TYPE");
        this.typeName = resultSet.getString("TYPE_NAME");
        this.columnSize = resultSet.getInt("COLUMN_SIZE");
        this.bufferLength = resultSet.getInt("BUFFER_LENGTH");
        this.decimalDigits = resultSet.getInt("DECIMAL_DIGITS");
        this.numPrecRadix = resultSet.getInt("NUM_PREC_RADIX");
        this.nullable = resultSet.getInt("NULLABLE");
        this.remarks = resultSet.getString("REMARKS");
        this.columnDef = resultSet.getString("COLUMN_DEF");
        this.sqlDataType = resultSet.getInt("SQL_DATA_TYPE");
        this.sqlDatetimeSub = resultSet.getInt("SQL_DATETIME_SUB");
        this.charOctetLength = resultSet.getInt("CHAR_OCTET_LENGTH");
        this.ordinalPosition = resultSet.getInt("ORDINAL_POSITION");
        this.isNullable = resultSet.getString("IS_NULLABLE");
        this.scopeCatalog = resultSet.getString("SCOPE_CATALOG");
        this.scopeSchema = resultSet.getString("SCOPE_SCHEMA");
        this.scopeTable = resultSet.getString("SCOPE_TABLE");
        this.sourceDataType = resultSet.getShort("SOURCE_DATA_TYPE");
        this.isAutoincrement = resultSet.getString("IS_AUTOINCREMENT");
        this.isGeneratedcolumn = resultSet.getString("IS_GENERATEDCOLUMN");
    }
}
