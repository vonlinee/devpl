package org.apache.ddlutils.io;

import org.apache.ddlutils.io.converters.*;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;

import java.sql.Types;
import java.util.HashMap;

/**
 * Contains the configuration for converters, which convert between the Java data types
 * corresponding to SQL data, and string representations.
 * @version $Revision: 289996 $
 */
public class ConverterConfiguration {
    /**
     * The converters per type.
     */
    private final HashMap<Integer, SqlTypeConverter> _convertersPerType = new HashMap<>();
    /**
     * The converters per table-column path.
     */
    private final HashMap<String, SqlTypeConverter> _convertersPerPath = new HashMap<>();

    /**
     * Creates a new configuration object with the default converters.
     */
    public ConverterConfiguration() {
        NumberConverter numberConverter = new NumberConverter();
        ByteArrayBase64Converter binaryConverter = new ByteArrayBase64Converter();

        registerConverter(Types.DATE, new DateConverter());
        registerConverter(Types.TIME, new TimeConverter());
        registerConverter(Types.TIMESTAMP, new TimestampConverter());
        registerConverter(Types.BIGINT, numberConverter);
        registerConverter(Types.BIT, numberConverter);
        registerConverter(Types.BOOLEAN, numberConverter);
        registerConverter(Types.DECIMAL, numberConverter);
        registerConverter(Types.DOUBLE, numberConverter);
        registerConverter(Types.FLOAT, numberConverter);
        registerConverter(Types.INTEGER, numberConverter);
        registerConverter(Types.NUMERIC, numberConverter);
        registerConverter(Types.REAL, numberConverter);
        registerConverter(Types.SMALLINT, numberConverter);
        registerConverter(Types.TINYINT, numberConverter);
        registerConverter(Types.BINARY, binaryConverter);
        registerConverter(Types.VARBINARY, binaryConverter);
        registerConverter(Types.LONGVARBINARY, binaryConverter);
        registerConverter(Types.BLOB, binaryConverter);
    }

    /**
     * Registers the given type converter for an sql type.
     * @param sqlTypeCode The type code, one of the {@link java.sql.Types} constants
     * @param converter   The converter
     */
    public void registerConverter(int sqlTypeCode, SqlTypeConverter converter) {
        _convertersPerType.put(sqlTypeCode, converter);
    }

    /**
     * Registers the given type converter for the specified column.
     * @param tableName  The name of the table
     * @param columnName The name of the column
     * @param converter  The converter
     */
    public void registerConverter(String tableName, String columnName, SqlTypeConverter converter) {
        _convertersPerPath.put(tableName + "/" + columnName, converter);
    }

    /**
     * Returns the converter registered for the specified column.
     * @param table  The table
     * @param column The column
     * @return The converter
     */
    public SqlTypeConverter getRegisteredConverter(Table table, Column column) {
        SqlTypeConverter result = _convertersPerPath.get(table.getName() + "/" + column.getName());

        if (result == null) {
            result = _convertersPerType.get(column.getJdbcTypeCode());
        }
        return result;
    }
}
