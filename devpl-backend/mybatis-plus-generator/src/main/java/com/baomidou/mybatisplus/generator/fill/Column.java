package com.baomidou.mybatisplus.generator.fill;

import com.baomidou.mybatisplus.annotation.FieldFill;
import org.jetbrains.annotations.NotNull;

/**
 * 字段填充
 * @author nieqiurong
 * @since 3.5.0 2020/12/1.
 */
public class Column implements FieldFillStrategy {

    private final String columnName;

    private final FieldFill fieldFill;

    public Column(@NotNull String columnName, @NotNull FieldFill fieldFill) {
        this.columnName = columnName;
        this.fieldFill = fieldFill;
    }

    public Column(String columnName) {
        this.columnName = columnName;
        this.fieldFill = FieldFill.DEFAULT;
    }

    @Override
    public @NotNull String getName() {
        return this.columnName;
    }

    @Override
    public @NotNull FieldFill getFieldFill() {
        return this.fieldFill;
    }
}
