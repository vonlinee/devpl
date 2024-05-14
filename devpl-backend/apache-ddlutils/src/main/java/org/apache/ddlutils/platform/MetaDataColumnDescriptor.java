package org.apache.ddlutils.platform;

import org.apache.ddlutils.jdbc.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Describes a column in a metadata result set.
 */
public class MetaDataColumnDescriptor {
    /**
     * The name of the column.
     */
    private final String _columnName;

    /**
     * the index of the column
     */
    private final int columnIndex;

    /**
     * The jdbc type to read from the result set.
     */
    private final int _jdbcType;
    /**
     * The default value if the column is not present in the result set.
     */
    private final Object _defaultValue;

    /**
     * Creates a new descriptor instance.
     *
     * @param columnName The name of the column
     * @param jdbcType   The jdbc type for reading from the result set, one of
     *                   VARCHAR, INTEGER, TINYINT, BIT
     */
    public MetaDataColumnDescriptor(String columnName, int jdbcType) {
        this(columnName, jdbcType, null);
    }

    /**
     * Creates a new descriptor instance.
     *
     * @param columnName   The name of the column
     * @param jdbcType     The jdbc type for reading from the result set, one of
     *                     VARCHAR, INTEGER, TINYINT, BIT
     * @param defaultValue The default value if the column is not present in the result set
     */
    public MetaDataColumnDescriptor(String columnName, int jdbcType, Object defaultValue) {
        _columnName = columnName.toUpperCase();
        _jdbcType = jdbcType;
        _defaultValue = defaultValue;
        columnIndex = 0;
    }

    /**
     * Returns the name.
     *
     * @return The name
     */
    public String getName() {
        return _columnName;
    }

    /**
     * Returns the default value.
     *
     * @return The default value
     */
    public Object getDefaultValue() {
        return _defaultValue;
    }

    /**
     * Returns the jdbc type to read from the result set.
     *
     * @return The jdbc type
     */
    public int getJdbcType() {
        return _jdbcType;
    }

    /**
     * Reads the column from the result set.
     *
     * @param resultSet The result set
     * @return The column value or the default value if the column is not present in the result set
     */
    public Object readColumn(ResultSet resultSet) throws SQLException {
        Object result;
        try {
            result = _columnName == null
                ? readColumnValue(resultSet, columnIndex)
                : readColumnValue(resultSet, _columnName);
            // Reports whether the last column read had a value of SQL NULL.
            // Note that you must first call one of the getter methods on a column to try to read
            // its value and then call the method wasNull to see if the value read was SQL NULL.
            // Returns: true if the last column value read was SQL NULL and false otherwise
            if (resultSet.wasNull()) {
                return null;
            }
        } catch (SQLException ex) {
            if (JdbcUtils.isColumnInResultSet(resultSet, _columnName)) {
                throw ex;
            }
            result = _defaultValue;
        }
        return result;
    }

    private Object readColumnValue(ResultSet resultSet, String columnName) throws SQLException {
        return switch (_jdbcType) {
            case Types.BIT -> resultSet.getBoolean(columnName);
            case Types.INTEGER -> resultSet.getInt(columnName);
            case Types.TINYINT -> resultSet.getShort(columnName);
            default -> resultSet.getString(columnName);
        };
    }

    private Object readColumnValue(ResultSet resultSet, int columnIndex) throws SQLException {
        return switch (_jdbcType) {
            case Types.BIT -> resultSet.getBoolean(columnIndex);
            case Types.INTEGER -> resultSet.getInt(columnIndex);
            case Types.TINYINT -> resultSet.getShort(columnIndex);
            default -> resultSet.getString(columnIndex);
        };
    }
}
