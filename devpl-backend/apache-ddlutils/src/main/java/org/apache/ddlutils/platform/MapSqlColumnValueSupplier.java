package org.apache.ddlutils.platform;

import org.apache.ddlutils.model.Column;

import java.util.Map;

public class MapSqlColumnValueSupplier implements SqlColumnValueSupplier {

    private final Map<String, Object> columnValueMap;

    public MapSqlColumnValueSupplier(Map<String, Object> columnValueMap) {
        this.columnValueMap = columnValueMap;
    }

    @Override
    public String getColumnValue(Column column) {
        if (columnValueMap == null) {
            return "NULL";
        }
        Object value = columnValueMap.get(column.getName());
        if (value == null) {
            return "NULL";
        }
        return String.valueOf(value);
    }
}
