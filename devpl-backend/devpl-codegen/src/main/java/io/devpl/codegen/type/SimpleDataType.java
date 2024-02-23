package io.devpl.codegen.type;

public enum SimpleDataType implements EnumBasedDataType<SimpleDataType> {
    /**
     * 任意字符串
     */
    STRING,
    /**
     * 模板字符串
     */
    TEMPLATE_STRING,
    /**
     * 数值
     */
    NUMBER,
    /**
     * 布尔值
     */
    BOOLEAN,
    /**
     * 字符串形式的布尔值， true/false, 1/0, YES/NO等
     */
    STRING_BOOLEAN
}
