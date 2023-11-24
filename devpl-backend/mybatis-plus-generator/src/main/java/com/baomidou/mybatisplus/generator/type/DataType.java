package com.baomidou.mybatisplus.generator.type;

/**
 * 数据类型
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
    String getQualifier();
}
