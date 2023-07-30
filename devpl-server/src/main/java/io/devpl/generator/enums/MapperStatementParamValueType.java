package io.devpl.generator.enums;

import com.baomidou.mybatisplus.generator.type.DataType;

/**
 * MyBatis Mapper Statement 参数值类型
 */
public enum MapperStatementParamValueType implements DataType {

    BOOLEAN("Boolean"),
    NUMBER("Number"),
    STRING("String"),
    COLLECTION("Collection");

    private final String typeName;

    MapperStatementParamValueType(String typeName) {
        this.typeName = typeName;
    }

    public static MapperStatementParamValueType valueOfTypeName(String typeName) {
        if (typeName == null) {
            return null;
        }
        for (MapperStatementParamValueType item : values()) {
            if (item.getQualifier().equals(typeName)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public final String getQualifier() {
        return typeName;
    }
}
