package com.baomidou.mybatisplus.generator.fill;

import com.baomidou.mybatisplus.annotation.FieldFill;
import org.jetbrains.annotations.NotNull;

/**
 * 属性填充
 * @author nieqiurong
 * @since 3.5.0 2020/11/30.
 */
public class Property implements FieldFillStrategy {

    private final String propertyName;

    private final FieldFill fieldFill;

    public Property(@NotNull String propertyName, @NotNull FieldFill fieldFill) {
        this.propertyName = propertyName;
        this.fieldFill = fieldFill;
    }

    public Property(@NotNull String propertyName) {
        this.propertyName = propertyName;
        this.fieldFill = FieldFill.DEFAULT;
    }

    @Override
    public @NotNull String getName() {
        return this.propertyName;
    }

    @Override
    public @NotNull FieldFill getFieldFill() {
        return this.fieldFill;
    }
}
