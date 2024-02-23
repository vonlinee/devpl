package io.devpl.codegen.type;

/**
 * 基于枚举的数据类型定义
 * 实现类需要是枚举类型
 *
 * @param <E> 枚举类
 */
public interface EnumBasedDataType<E extends Enum<E>> extends DataType {

}
