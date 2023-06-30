package com.baomidou.mybatisplus.generator.type;

/**
 * An object that is used to identify a generic Java type
 * @since 1.8
 */
public interface JavaType extends DataType {

    /**
     * 类型标识，一般是数字或字符串
     * @return 类型标识
     */
    String getName();

    default String getType() {
        return getName();
    }

    /**
     * 限定类型名称，一般是全限定类名，如果是基本类型，则为基本类型名称，比如int, long等
     * @return 限定类型名称
     */
    String getQualifier();
}
