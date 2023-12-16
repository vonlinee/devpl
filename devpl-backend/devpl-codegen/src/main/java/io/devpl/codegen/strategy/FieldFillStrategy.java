package io.devpl.codegen.strategy;

import io.devpl.codegen.db.FieldFill;

/**
 * 填充接口
 */
public interface FieldFillStrategy {

    String getName();

    FieldFill getFieldFill();
}
