package io.devpl.codegen.sql;

import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import lombok.Data;

/**
 * 通用的列定义信息
 * @see com.alibaba.druid.sql.ast.statement.SQLColumnDefinition
 */
@Data
public class ColumnDefinitionWrapper {
    private String columnName;
    private Boolean hasDefaultExpression;
    private String defaultExpression;

    final SQLColumnDefinition columnDefinition;

    public ColumnDefinitionWrapper(SQLColumnDefinition columnDefinition) {
        this.columnDefinition = columnDefinition;
        this.columnName = columnDefinition.getColumnName();
    }

    public boolean hasDefaultExpression() {
        if (hasDefaultExpression == null) {
            hasDefaultExpression = columnDefinition.getDefaultExpr() != null;
        }
        return hasDefaultExpression;
    }

    public String getDefaultExpressionAsString() {
        if (columnDefinition.getDefaultExpr() != null) {
            this.defaultExpression = columnDefinition.getDefaultExpr().toString();
        }
        return defaultExpression == null ? "" : defaultExpression;
    }

    public SQLDataType getSqlType() {
        return columnDefinition.getDataType();
    }
}
