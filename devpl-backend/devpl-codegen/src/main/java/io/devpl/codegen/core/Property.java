package io.devpl.codegen.core;

import io.devpl.codegen.db.FieldFill;
import io.devpl.codegen.strategy.FieldFillStrategy;

/**
 * 属性填充
 */
public class Property implements FieldFillStrategy {

    private final String propertyName;

    private final FieldFill fieldFill;

    public Property(String propertyName, FieldFill fieldFill) {
        this.propertyName = propertyName;
        this.fieldFill = fieldFill;
    }

    public Property(String propertyName) {
        this.propertyName = propertyName;
        this.fieldFill = FieldFill.DEFAULT;
    }

    @Override
    public String getName() {
        return this.propertyName;
    }

    @Override
    public FieldFill getFieldFill() {
        return this.fieldFill;
    }
}
