package io.devpl.codegen.type;

/**
 * JSON数据类型枚举
 */
public enum JsonDataType implements EnumBasedDataType<JsonDataType> {

    /**
     * 数组类型
     */
    ARRAY() {

        DataType componentType;

        @Override
        public boolean isArray() {
            return true;
        }

        @Override
        public void setComponentType(DataType componentType) {
            if (componentType instanceof JsonDataType) {
                this.componentType = componentType;
            }
        }

        @Override
        public DataType getComponentType() {
            return componentType;
        }
    },
    OBJECT,
    STRING,
    NUMBER,
    BOOLEAN,
    /**
     * 未知数据类型
     */
    UNKNOWN,
    /**
     * NULL类型
     */
    NULL;

    @Override
    public String getQualifier() {
        return name();
    }
}
