package io.devpl.codegen.type;

/**
 * JSON数据类型枚举
 */
public enum JsonDataType implements DataType {

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
    NULL;

    @Override
    public String getQualifier() {
        return name();
    }
}
