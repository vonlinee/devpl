package io.devpl.backend.mock.generator;

import io.devpl.backend.mock.MockValueGenerator;
import io.devpl.codegen.type.DataType;

/**
 * 单个值的生成，单个字段
 */
public abstract class SingleValueMockValueGenerator implements MockValueGenerator {

    DataType targetDataType;

    public void setTargetDataType(DataType dataType) {
        this.targetDataType = dataType;
    }

    @Override
    public DataType getTargetType() {
        return targetDataType;
    }
}
