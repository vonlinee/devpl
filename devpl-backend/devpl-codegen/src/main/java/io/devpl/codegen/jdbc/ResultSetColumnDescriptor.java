package io.devpl.codegen.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Describes a result set column in a metadata result set.
 */
public class ResultSetColumnDescriptor {
    /**
     * The name of the column.
     */
    private final String columnName;

    /**
     * the index of the column
     */
    private final int columnIndex;

    /**
     * The jdbc type to read from the result set.
     */
    private final int jdbcType;
    /**
     * The default value if the column is not present in the result set.
     */
    private final Object defaultValue;

    /**
     * Creates a new descriptor instance.
     *
     * @param columnName The name of the column
     * @param jdbcType   The jdbc type for reading from the result set, one of
     *                   VARCHAR, INTEGER, TINYINT, BIT
     */
    public ResultSetColumnDescriptor(String columnName, int jdbcType) {
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
    public ResultSetColumnDescriptor(String columnName, int jdbcType, Object defaultValue) {
        this.columnName = columnName.toUpperCase();
        this.jdbcType = jdbcType;
        this.defaultValue = defaultValue;
        columnIndex = 0;
    }

    /**
     * Returns the name.
     *
     * @return The name
     */
    public String getName() {
        return columnName;
    }

    /**
     * Returns the default value.
     *
     * @return The default value
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns the jdbc type to read from the result set.
     *
     * @return The jdbc type
     */
    public int getJdbcType() {
        return jdbcType;
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
            result = columnName == null
                ? readColumnValue(resultSet, columnIndex)
                : readColumnValue(resultSet, columnName);
            // Reports whether the last column read had a value of SQL NULL.
            // Note that you must first call one of the getter methods on a column to try to read
            // its value and then call the method wasNull to see if the value read was SQL NULL.
            // Returns: true if the last column value read was SQL NULL and false otherwise
            if (resultSet.wasNull()) {
                return null;
            }
        } catch (SQLException ex) {
            if (JdbcUtils.isColumnInResultSet(resultSet, columnName)) {
                throw ex;
            }
            result = defaultValue;
        }
        return result;
    }

    private Object readColumnValue(ResultSet resultSet, String columnName) throws SQLException {
        return switch (jdbcType) {
            case Types.BIT -> resultSet.getBoolean(columnName);
            case Types.INTEGER -> resultSet.getInt(columnName);
            case Types.TINYINT -> resultSet.getShort(columnName);
            default -> resultSet.getString(columnName);
        };
    }

    private Object readColumnValue(ResultSet resultSet, int columnIndex) throws SQLException {
        return switch (jdbcType) {
            case Types.BIT -> resultSet.getBoolean(columnIndex);
            case Types.INTEGER -> resultSet.getInt(columnIndex);
            case Types.TINYINT -> resultSet.getShort(columnIndex);
            default -> resultSet.getString(columnIndex);
        };
    }
}
