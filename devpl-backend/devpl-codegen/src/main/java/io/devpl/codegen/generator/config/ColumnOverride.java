package io.devpl.codegen.generator.config;

import io.devpl.codegen.util.Messages;
import io.devpl.sdk.util.StringUtils;

import java.util.List;

public class ColumnOverride extends PropertyHolder {

    private final String columnName;

    private String javaProperty;

    private String jdbcType;

    private String javaType;

    private String typeHandler;

    private boolean isColumnNameDelimited;

    /**
     * If true, the column is a GENERATED ALWAYS column which means
     * that it should not be used in insert or update statements.
     */
    private boolean isGeneratedAlways;

    public ColumnOverride(String columnName) {
        super();

        this.columnName = columnName;
        isColumnNameDelimited = StringUtils.hasText(columnName);
    }

    public String getColumnName() {
        return columnName;
    }

    public String getJavaProperty() {
        return javaProperty;
    }

    public void setJavaProperty(String javaProperty) {
        this.javaProperty = javaProperty;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }

    public boolean isColumnNameDelimited() {
        return isColumnNameDelimited;
    }

    public void setColumnNameDelimited(boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
    }

    public void validate(List<String> errors, String tableName) {
        if (!StringUtils.hasText(columnName)) {
            errors.add(Messages.getString("ValidationError.22",
                tableName));
        }
    }

    public boolean isGeneratedAlways() {
        return isGeneratedAlways;
    }

    public void setGeneratedAlways(boolean isGeneratedAlways) {
        this.isGeneratedAlways = isGeneratedAlways;
    }
}
