package org.apache.ddlutils.platform;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Describes a column in a metadata result set.
 * @version $Revision: $
 */
public class MetaDataColumnDescriptor {
    /**
     * The name of the column.
     */
    private final String columnName;
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
     * @param columnName The name of the column
     * @param jdbcType   The jdbc type for reading from the result set, one of
     *                   VARCHAR, INTEGER, TINYINT, BIT
     */
    public MetaDataColumnDescriptor(String columnName, int jdbcType) {
        this(columnName, jdbcType, null);
    }

    /**
     * Creates a new descriptor instance.
     * @param columnName   The name of the column
     * @param jdbcType     The jdbc type for reading from the result set, one of
     *                     VARCHAR, INTEGER, TINYINT, BIT
     * @param defaultValue The default value if the column is not present in the result set
     */
    public MetaDataColumnDescriptor(String columnName, int jdbcType, Object defaultValue) {
        this.columnName = columnName.toUpperCase();
        this.jdbcType = jdbcType;
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the name.
     * @return The name
     */
    public String getName() {
        return columnName;
    }

    /**
     * Returns the default value.
     * @return The default value
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns the jdbc type to read from the result set.
     * @return The jdbc type
     */
    public int getJdbcType() {
        return jdbcType;
    }

    /**
     * Reads the column from the result set.
     * @param resultSet The result set
     * @return The column value or the default value if the column is not present in the result set
     */
    public Object readColumn(ResultSet resultSet) throws SQLException {
        Object result;
        try {
            result = switch (jdbcType) {
                case Types.BIT -> resultSet.getBoolean(columnName);
                case Types.INTEGER -> resultSet.getInt(columnName);
                case Types.TINYINT -> resultSet.getShort(columnName);
                default -> resultSet.getString(columnName);
            };
            if (resultSet.wasNull()) {
                result = null;
            }
        } catch (SQLException ex) {
            if (isColumnInResultSet(resultSet)) {
                throw ex;
            } else {
                result = defaultValue;
            }
        }
        return result;
    }

    /**
     * Determines whether a value for the specified column is present in the given result set.
     * @param resultSet The result set
     * @return <code>true</code> if the column is present in the result set
     */
    private boolean isColumnInResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        for (int idx = 1; idx <= metaData.getColumnCount(); idx++) {
            if (columnName.equals(metaData.getColumnName(idx).toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
