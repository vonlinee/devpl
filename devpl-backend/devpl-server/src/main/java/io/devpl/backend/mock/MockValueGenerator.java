package io.devpl.backend.mock;

import io.devpl.codegen.type.DataType;

public interface MockValueGenerator {

    void init(MockContext context);

    /**
     * 生成值的数据类型
     *
     * @return 数据类型
     */
    DataType getTargetType();

    /**
     * 获取指定类型的值，字符串形式
     *
     * @return 字符串形式的值
     */
    String getValue(MockContext context);
}
