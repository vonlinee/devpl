package io.devpl.codegen.core;

import io.devpl.codegen.db.FieldFill;
import io.devpl.codegen.strategy.FieldFillStrategy;

/**
 * 字段填充
 *
 * @author nieqiurong
 * @since 3.5.0 2020/12/1.
 */
public class ColumnFill implements FieldFillStrategy {

    private final String columnName;

    private final FieldFill fieldFill;

    public ColumnFill(String columnName, FieldFill fieldFill) {
        this.columnName = columnName;
        this.fieldFill = fieldFill;
    }

    public ColumnFill(String columnName) {
        this.columnName = columnName;
        this.fieldFill = FieldFill.DEFAULT;
    }

    @Override
    public String getName() {
        return this.columnName;
    }

    @Override
    public FieldFill getFieldFill() {
        return this.fieldFill;
    }
}
