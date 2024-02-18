package io.devpl.codegen.type;

/**
 * 通用的数据类型
 */
public enum CommonDataType implements DataType {

    /**
     * 字符串，包括字符
     */
    STRING,
    /**
     * 整数类型
     */
    INTEGER,
    /**
     * 数字类型，包含整数类型
     */
    NUMBER,
    /**
     * 布尔值
     */
    BOOLEAN,
    /**
     * 数组类型，包括任意序列类型
     */
    ARRAY,
    /**
     * 任意类型
     */
    ANY,
    /**
     * Null类型
     */
    NULL
}
