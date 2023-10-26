package io.devpl.generator.enums;

import com.baomidou.mybatisplus.generator.type.DataType;

/**
 * MyBatis Mapper Statement 参数值类型
 */
public enum MapperStatementParamValueType implements DataType {

    NULL(0, "Null"),
    BOOLEAN(1, "Boolean"),
    NUMBER(2, "Number"),
    STRING(3, "String"),
    COLLECTION(4, "Collection");

    private final int type;
    private final String typeName;

    MapperStatementParamValueType(int type, String typeName) {
        this.type = type;
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

    public int getType() {
        return type;
    }

    @Override
    public final String getQualifier() {
        return typeName;
    }
}
