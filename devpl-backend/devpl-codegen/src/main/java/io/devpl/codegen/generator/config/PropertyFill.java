package io.devpl.codegen.generator.config;

import io.devpl.codegen.db.FieldFill;
import io.devpl.codegen.strategy.FieldFillStrategy;

/**
 * 属性填充
 */
public class PropertyFill implements FieldFillStrategy {

    private final String propertyName;

    private final FieldFill fieldFill;

    public PropertyFill(String propertyName, FieldFill fieldFill) {
        this.propertyName = propertyName;
        this.fieldFill = fieldFill;
    }

    public PropertyFill(String propertyName) {
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
