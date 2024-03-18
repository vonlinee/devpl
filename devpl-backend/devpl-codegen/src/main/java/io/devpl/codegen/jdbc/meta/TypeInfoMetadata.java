package io.devpl.codegen.jdbc.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @see DatabaseMetaData#getTypeInfo()
 */
public class TypeInfoMetadata {

    /**
     * TYPE_NAME String => Type name
     **/
    private String typeName;
    /**
     * DATA_TYPE int => SQL data type from java.sql.Types
     **/
    private int dataType;
    /**
     * PRECISION int => maximum precision
     **/
    private int precision;
    /**
     * LITERAL_PREFIX String => prefix used to quote a literal (maybe null)
     **/
    private String literalPrefix;
    /**
     * LITERAL_SUFFIX String => suffix used to quote a literal (maybe null)
     **/
    private String literalSuffix;
    /**
     * CREATE_PARAMS String => parameters used in creating the type (maybe null)
     **/
    private String createParams;
    /**
     * NULLABLE short => can you use NULL for this type.
     * typeNoNulls - does not allow NULL values
     * typeNullable - allows NULL values
     * typeNullableUnknown - nullability unknown
     **/
    private short nullable;
    /**
     * CASE_SENSITIVE boolean=> is it case-sensitive.
     **/
    private boolean caseSensitive;
    /**
     * SEARCHABLE short => can you use "WHERE" based on this type:
     * typePredNone - No support
     * typePredChar - Only supported with WHERE ... LIKE
     * typePredBasic - Supported except for WHERE ... LIKE
     * typeSearchable - Supported for all WHERE ...
     **/
    private short searchable;
    /**
     * UNSIGNED_ATTRIBUTE boolean => is it unsigned.
     **/
    private boolean unsignedAttribute;
    /**
     * FIXED_PREC_SCALE boolean => can it be a money value.
     **/
    private boolean fixedPrecisionScale;
    /**
     * AUTO_INCREMENT boolean => can it be used for an auto-increment value.
     **/
    private boolean autoIncrement;
    /**
     * LOCAL_TYPE_NAME String => localized version of type name (maybe null)
     **/
    private String localTypeName;
    /**
     * MINIMUM_SCALE short => minimum scale supported
     **/
    private short minimumScale;
    /**
     * MAXIMUM_SCALE short => maximum scale supported
     **/
    private short maximumScale;
    /**
     * SQL_DATA_TYPE int => unused
     **/
    private int sqlDataType;
    /**
     * SQL_DATETIME_SUB int => unused
     **/
    private int sqlDatetimeSub;
    /**
     * NUM_PREC_RADIX int => usually 2 or 10
     **/
    private int numericPrecisionRadix;

    public void initialize(ResultSet resultSet) {
        try {
            this.typeName = resultSet.getString(1);
            this.dataType = resultSet.getInt(2);
            this.precision = resultSet.getInt(3);
            this.literalPrefix = resultSet.getString(4);
            this.literalSuffix = resultSet.getString(5);
            this.createParams = resultSet.getString(6);
            this.nullable = resultSet.getShort(7);
            this.caseSensitive = resultSet.getBoolean(8);
            this.searchable = resultSet.getShort(9);
            this.unsignedAttribute = resultSet.getBoolean(10);
            this.fixedPrecisionScale = resultSet.getBoolean(11);
            this.autoIncrement = resultSet.getBoolean(12);
            this.localTypeName = resultSet.getString(13);
            this.minimumScale = resultSet.getShort(14);
            this.maximumScale = resultSet.getShort(15);
            this.sqlDataType = resultSet.getInt(16);
            this.sqlDatetimeSub = resultSet.getInt(17);
            this.numericPrecisionRadix = resultSet.getInt(18);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public String getLiteralPrefix() {
        return literalPrefix;
    }

    public void setLiteralPrefix(String literalPrefix) {
        this.literalPrefix = literalPrefix;
    }

    public String getLiteralSuffix() {
        return literalSuffix;
    }

    public void setLiteralSuffix(String literalSuffix) {
        this.literalSuffix = literalSuffix;
    }

    public String getCreateParams() {
        return createParams;
    }

    public void setCreateParams(String createParams) {
        this.createParams = createParams;
    }

    public short getNullable() {
        return nullable;
    }

    public void setNullable(short nullable) {
        this.nullable = nullable;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public short getSearchable() {
        return searchable;
    }

    public void setSearchable(short searchable) {
        this.searchable = searchable;
    }

    public boolean isUnsignedAttribute() {
        return unsignedAttribute;
    }

    public void setUnsignedAttribute(boolean unsignedAttribute) {
        this.unsignedAttribute = unsignedAttribute;
    }

    public boolean isFixedPrecisionScale() {
        return fixedPrecisionScale;
    }

    public void setFixedPrecisionScale(boolean fixedPrecisionScale) {
        this.fixedPrecisionScale = fixedPrecisionScale;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public String getLocalTypeName() {
        return localTypeName;
    }

    public void setLocalTypeName(String localTypeName) {
        this.localTypeName = localTypeName;
    }

    public short getMinimumScale() {
        return minimumScale;
    }

    public void setMinimumScale(short minimumScale) {
        this.minimumScale = minimumScale;
    }

    public short getMaximumScale() {
        return maximumScale;
    }

    public void setMaximumScale(short maximumScale) {
        this.maximumScale = maximumScale;
    }

    public int getSqlDataType() {
        return sqlDataType;
    }

    public void setSqlDataType(int sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    public int getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    public void setSqlDatetimeSub(int sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    public int getNumericPrecisionRadix() {
        return numericPrecisionRadix;
    }

    public void setNumericPrecisionRadix(int numericPrecisionRadix) {
        this.numericPrecisionRadix = numericPrecisionRadix;
    }
}
