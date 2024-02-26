package io.devpl.backend.mock.generator;

import io.devpl.backend.mock.MockContext;
import io.devpl.backend.mock.MockValueTypeEnum;
import io.devpl.codegen.type.DataType;

import java.util.UUID;

/**
 * 字符串类型的生成器
 */
public class StringMockValueGenerator extends SingleValueMockValueGenerator {

    int type;

    @Override
    public void init(MockContext context) {

    }

    @Override
    public DataType getTargetType() {
        return MockValueTypeEnum.STRING;
    }

    @Override
    public String getValue(MockContext context) {

        init(context);
        if (type == 1) {
            return UUID.randomUUID().toString();
        }
        return UUID.randomUUID().toString();
    }
}
