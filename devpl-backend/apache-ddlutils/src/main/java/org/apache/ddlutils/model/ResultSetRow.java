package org.apache.ddlutils.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * @see java.sql.ResultSet
 */
public final class ResultSetRow {

    private int columnCount;
    private final Set<String> columns;
    private final List<String> columnNames;
    private final List<Object> columnValues;

    public ResultSetRow() {
        this.columns = new HashSet<>();
        this.columnNames = new ArrayList<>();
        this.columnValues = new ArrayList<>();
    }

    public boolean addColumn(String columnName) {
        return addColumn(columnName, null);
    }

    public boolean addColumn(String columnName, Object value) {
        if (columns.add(columnName)) {
            columnNames.add(columnName);
            columnValues.add(value);
            columnCount++;
            return true;
        }
        return false;
    }

    public Object getColumnValue(int columnIndex) {
        if (columnCount > columnIndex) {
            return columnValues.get(columnIndex);
        }
        return null;
    }

    public Object getColumnValue(String name) {
        int index = columnNames.indexOf(name);
        if (index >= 0) {
            return columnValues.get(index);
        }
        return null;
    }

    public void setColumnValue(int columnIndex, Object value) {
        if (columnIndex >= 0 && columnIndex < columnCount) {
            columnValues.set(columnIndex, value);
        }
    }

    public void setColumnValue(String name, Object value) {
        int index = columnNames.indexOf(name);
        if (index >= 0) {
            columnValues.set(index, value);
        } else {
            // column doesn't exists
            addColumn(name, value);
        }
    }

    public boolean hasColumn(String columnName) {
        return columns.contains(columnName);
    }

    public int getColumnCount() {
        return columnCount;
    }

    public boolean isEmpty() {
        return columnCount == 0;
    }

    public void foreach(BiConsumer<String, Object> consumer) {
        for (int i = 0; i < columnCount; i++) {
            consumer.accept(columnNames.get(i), columnValues.get(i));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnCount; i++) {
            sb.append(columnNames.get(i)).append("=").append(columnValues.get(i)).append(",");
        }
        return sb.toString();
    }
}
