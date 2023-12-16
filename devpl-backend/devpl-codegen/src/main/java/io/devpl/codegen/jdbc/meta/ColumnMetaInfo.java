package io.devpl.codegen.jdbc.meta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

/**
 * JDBC column metadata
 */
public class ColumnMetaInfo {
    private final String name;
    private final String typeName;
    private final int columnSize;
    private final int decimalDigits;
    private final String isNullable;
    private final int typeCode;

    ColumnMetaInfo(ResultSet rs) throws SQLException {
        name = rs.getString("COLUMN_NAME");
        columnSize = rs.getInt("COLUMN_SIZE");
        decimalDigits = rs.getInt("DECIMAL_DIGITS");
        isNullable = rs.getString("IS_NULLABLE");
        typeCode = rs.getInt("DATA_TYPE");
        typeName = new StringTokenizer(rs.getString("TYPE_NAME"), "() ").nextToken();
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public String getNullable() {
        return isNullable;
    }

    @Override
    public String toString() {
        return "ColumnMetaInfo(" + name + ')';
    }

    public int getTypeCode() {
        return typeCode;
    }
}
