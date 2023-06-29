package io.devpl.generator.enums;

/**
 * MyBatis Mapper Statement 参数值类型
 */
public enum MapperStatementParamValueType {

    NUMBER,
    STRING,
    LIST,
    SET;

    public static MapperStatementParamValueType valueOfTypeName(String typeName) {
        if (typeName == null) {
            return null;
        }
        return valueOf(typeName);
    }
}
