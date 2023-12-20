package io.devpl.codegen.type;

/**
 * 所有数据类型的父接口
 */
public interface DataType {

    /**
     * 数据类型ID
     *
     * @return 数据类型ID，全局唯一
     */
    default String id() {
        return getQualifier();
    }

    /**
     * 唯一限定类型名称
     *
     * @return 限定类型名称
     */
    default String getQualifier() {
        return toString();
    }

    /**
     * 获取该类型的泛型类型
     *
     * @return 类型的泛型类型
     */
    default DataType[] getGenericTypes() {
        return null;
    }

    /**
     * 元素类型，用于集合类型(数组，列表等)
     *
     * @return 元素类型
     */
    default DataType getComponentType() {
        return null;
    }

    /**
     * 是否是数组类型，同种数据的集合
     *
     * @return 是否是数组类型
     */
    default boolean isArray() {
        return false;
    }

    /**
     * 设置元素类型
     *
     * @param componentType 元素数据类型
     */
    default void setComponentType(DataType componentType) {
    }
}
