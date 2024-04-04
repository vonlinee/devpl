package io.devpl.codegen.generator.config;

import io.devpl.codegen.util.StringUtils;

import java.util.List;

public class IgnoredColumn {

    protected final String columnName;

    private boolean isColumnNameDelimited;

    public IgnoredColumn(String columnName) {
        super();
        this.columnName = columnName;
        isColumnNameDelimited = StringUtils.stringContainsSpace(columnName);
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnNameDelimited(boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IgnoredColumn)) {
            return false;
        }
        return columnName.equals(((IgnoredColumn) obj).getColumnName());
    }

    @Override
    public int hashCode() {
        return columnName.hashCode();
    }

    public void validate(List<String> errors, String tableName) {
        if (columnName == null || columnName.isEmpty()) {
            throw new RuntimeException("column name is empty");
        }
    }

    public boolean matches(String columnName) {
        if (isColumnNameDelimited) {
            return this.columnName.equals(columnName);
        } else {
            return this.columnName.equalsIgnoreCase(columnName);
        }
    }
}
