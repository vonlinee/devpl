package io.devpl.codegen.type;

/**
 * JSON数据类型枚举
 */
public enum StandardJsonDataType implements JsonDataType {
    ARRAY,
    OBJECT,
    STRING,
    NUMBER,
    BOOLEAN,
    NULL;

    @Override
    public String getQualifier() {
        return name();
    }
}
