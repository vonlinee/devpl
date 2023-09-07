package org.apache.ddlutils.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Objects;

/**
 * Represents a column in the database model.
 */
public class Column implements SchemaObject, Serializable {
    /**
     * Unique ID for serialization purposes.
     */
    private static final long serialVersionUID = -6226348998874210093L;

    /**
     * The name of the column.
     */
    private String name;
    /**
     * The java name of the column (optional and unused by DdlUtils, for Torque compatibility).
     */
    private String javaName;
    /**
     * The column's description.
     */
    private String description;
    /**
     * Whether the column is a primary key column.
     */
    private boolean primaryKey;
    /**
     * Whether the column is required, i.e. it must not contain <code>NULL</code>.
     */
    private boolean required;
    /**
     * Whether the column's value is incremented automatically.
     */
    private boolean autoIncrement;
    /**
     * The JDBC type code, one of the constants in {@link java.sql.Types}.
     */
    private int jdbcTypeCode;
    /**
     * The name of the JDBC type.
     */
    private String jdbcTypeName;
    /**
     * The size of the column for JDBC types that require/support this.
     */
    private String _size;
    /**
     * The size of the column for JDBC types that require/support this.
     */
    private Integer _sizeAsInt;
    /**
     * The scale of the column for JDBC types that require/support this.
     */
    private int _scale;

    /**
     * The precision of the column for JDBC types that require/support this.
     */
    private int precision;

    /**
     * The default value.
     */
    private String _defaultValue;

    /**
     * Returns the name of the column.
     * @return The name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the column.
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the java name of the column. This property is unused by DdlUtils and only
     * for Torque compatibility.
     * @return The java name
     */
    public String getJavaName() {
        return javaName;
    }

    /**
     * Sets the java name of the column. This property is unused by DdlUtils and only
     * for Torque(Apache Torque, an ORM framework) compatibility.
     * @param javaName The java name
     */
    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }

    /**
     * Returns the description of the column.
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the column.
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Determines whether this column is a primary key column.
     * @return <code>true</code> if this column is a primary key column
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * Specifies whether this column is a primary key column.
     * @param primaryKey <code>true</code> if this column is a primary key column
     */
    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * Determines whether this column is a required column, i.e. that it is not allowed
     * to contain <code>NULL</code> values.
     * @return <code>true</code> if this column is a required column
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Specifies whether this column is a required column, i.e. that it is not allowed
     * to contain <code>NULL</code> values.
     * @param required <code>true</code> if this column is a required column
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * Determines whether this column is an auto-increment column.
     * @return <code>true</code> if this column is an auto-increment column
     */
    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    /**
     * Specifies whether this column is an auto-increment column.
     * @param autoIncrement <code>true</code> if this column is an auto-increment column
     */
    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    /**
     * Returns the code (one of the constants in {@link java.sql.Types}) of the
     * JDBC type of the column.
     * @return The type code
     */
    public int getJdbcTypeCode() {
        return jdbcTypeCode;
    }

    /**
     * Sets the code (one of the constants in {@link java.sql.Types}) of the
     * JDBC type of the column.
     * @param jdbcTypeCode The type code
     */
    public void setJdbcTypeCode(int jdbcTypeCode) {
        jdbcTypeName = TypeMap.getJdbcTypeName(jdbcTypeCode);
        if (jdbcTypeName == null) {
            throw new ModelException("Unknown JDBC type code " + jdbcTypeCode);
        }
        this.jdbcTypeCode = jdbcTypeCode;
    }

    /**
     * Returns the JDBC type of the column.
     * @return The type
     */
    public String getType() {
        return jdbcTypeName;
    }

    /**
     * Sets the JDBC type of the column.
     * @param type The type
     */
    public void setType(String type) {
        Integer typeCode = TypeMap.getJdbcTypeCode(type);
        if (typeCode == null) {
            throw new ModelException("Unknown JDBC type " + type);
        }
        this.jdbcTypeCode = typeCode;
        // we get the corresponding string value from the TypeMap in order
        // to detect extension types which we don't want in the model
        jdbcTypeName = TypeMap.getJdbcTypeName(typeCode);
    }

    /**
     * Determines whether this column is of a numeric type.
     * @return <code>true</code> if this column is of a numeric type
     */
    public boolean isOfNumericType() {
        return TypeMap.isNumericType(getJdbcTypeCode());
    }

    /**
     * Determines whether this column is of a text type.
     * @return <code>true</code> if this column is of a text type
     */
    public boolean isOfTextType() {
        return TypeMap.isTextType(getJdbcTypeCode());
    }

    /**
     * Determines whether this column is of a binary type.
     * @return <code>true</code> if this column is of a binary type
     */
    public boolean isOfBinaryType() {
        return TypeMap.isBinaryType(getJdbcTypeCode());
    }

    /**
     * Determines whether this column is of a special type.
     * @return <code>true</code> if this column is of a special type
     */
    public boolean isOfSpecialType() {
        return TypeMap.isSpecialType(getJdbcTypeCode());
    }

    /**
     * Returns the size of the column.
     * @return The size
     */
    public String getSize() {
        return _size;
    }

    /**
     * Sets the size of the column. This is either a simple integer value or
     * a comma-separated pair of integer values specifying the size and scale.
     * I.e. "size" or "precision,scale".
     * @param size The size
     */
    public void setSize(String size) {
        if (size != null) {
            int pos = size.indexOf(",");
            _size = size;
            if (pos < 0) {
                _scale = 0;
                _sizeAsInt = Integer.parseInt(_size.trim());
            } else {
                _sizeAsInt = Integer.valueOf(size.substring(0, pos).trim());
                _scale = Integer.parseInt(size.substring(pos + 1).trim());
            }
        } else {
            _size = null;
            _sizeAsInt = null;
            _scale = 0;
        }
    }

    /**
     * Returns the size of the column as an integer.
     * @return The size as an integer
     */
    public int getSizeAsInt() {
        return _sizeAsInt == null ? 0 : _sizeAsInt;
    }

    /**
     * Returns the scale of the column.
     * @return The scale
     */
    public int getScale() {
        return _scale;
    }

    /**
     * Sets the scale of the column.
     * @param scale The scale
     */
    public void setScale(int scale) {
        setSizeAndScale(getSizeAsInt(), scale);
    }

    /**
     * Sets both the size and scale.
     * @param size  The size
     * @param scale The scale
     */
    public void setSizeAndScale(int size, int scale) {
        _sizeAsInt = size;
        _scale = scale;
        _size = String.valueOf(size);
        if (scale > 0) {
            _size += "," + _scale;
        }
    }

    /**
     * Returns the precision radix of the column.
     * @return The precision radix
     */
    public int getPrecisionRadix() {
        return getSizeAsInt();
    }

    /**
     * Sets the precision radix of the column.
     * @param precisionRadix The precision radix
     */
    public void setPrecisionRadix(int precisionRadix) {
        _sizeAsInt = precisionRadix;
        _size = String.valueOf(precisionRadix);
    }

    /**
     * Returns the default value of the column.
     * @return The default value
     */
    public String getDefaultValue() {
        return _defaultValue;
    }

    /**
     * Sets the default value of the column. Note that this expression will be used
     * within quotation marks when generating the column, and thus is subject to
     * the conversion rules of the target database.
     * @param defaultValue The default value
     */
    public void setDefaultValue(String defaultValue) {
        _defaultValue = defaultValue;
    }

    /**
     * Tries to parse the default value of the column and returns it as an object of the
     * corresponding java type. If the value could not be parsed, then the original
     * definition is returned.
     * @return The parsed default value
     */
    public Object getParsedDefaultValue() {
        if ((_defaultValue != null) && (!_defaultValue.isEmpty())) {
            try {
                switch (jdbcTypeCode) {
                    case Types.TINYINT, Types.SMALLINT -> {
                        return Short.parseShort(_defaultValue);
                    }
                    case Types.INTEGER -> {
                        return Integer.parseInt(_defaultValue);
                    }
                    case Types.BIGINT -> {
                        return Long.parseLong(_defaultValue);
                    }
                    case Types.DECIMAL, Types.NUMERIC -> {
                        return new BigDecimal(_defaultValue);
                    }
                    case Types.REAL -> {
                        return Float.parseFloat(_defaultValue);
                    }
                    case Types.DOUBLE, Types.FLOAT -> {
                        return Double.parseDouble(_defaultValue);
                    }
                    case Types.DATE -> {
                        return Date.valueOf(_defaultValue);
                    }
                    case Types.TIME -> {
                        return Time.valueOf(_defaultValue);
                    }
                    case Types.TIMESTAMP -> {
                        return Timestamp.valueOf(_defaultValue);
                    }
                    case Types.BIT, Types.BOOLEAN -> {
                        return Boolean.valueOf(_defaultValue);
                    }
                }
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }
        return _defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        if (primaryKey != column.primaryKey) return false;
        if (required != column.required) return false;
        if (autoIncrement != column.autoIncrement) return false;
        if (jdbcTypeCode != column.jdbcTypeCode) return false;
        if (_scale != column._scale) return false;
        if (!Objects.equals(name, column.name)) return false;
        if (!Objects.equals(javaName, column.javaName)) return false;
        if (!Objects.equals(description, column.description))
            return false;
        if (!Objects.equals(jdbcTypeName, column.jdbcTypeName)) return false;
        if (!Objects.equals(_size, column._size)) return false;
        if (!Objects.equals(_sizeAsInt, column._sizeAsInt)) return false;
        return Objects.equals(_defaultValue, column._defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, javaName, description, primaryKey, required, autoIncrement, jdbcTypeCode, jdbcTypeName, _size, _sizeAsInt, _scale, _defaultValue);
    }

    @Override
    public String toString() {
        return "Column [name=" + getName() + "; type=" + getType() + "]";
    }

    /**
     * Returns a verbose string representation of this column.
     * @return The string representation
     */
    public String toVerboseString() {
        return "Column [name=" + getName() + "; javaName=" + getJavaName() + "; type=" + getType() + "; typeCode=" + getJdbcTypeCode() + "; size=" + getSize() + "; required=" + isRequired() + "; primaryKey=" + isPrimaryKey() + "; autoIncrement=" + isAutoIncrement() + "; defaultValue=" + getDefaultValue() + "; precisionRadix=" + getPrecisionRadix() + "; scale=" + getScale() + "]";
    }
}
