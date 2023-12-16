package io.devpl.codegen.db;

import io.devpl.codegen.type.JavaType;

/**
 * 获取实体类字段属性类信息接口
 */
public interface ColumnJavaType extends JavaType {

    /**
     * 获取字段类型
     *
     * @return 字段类型
     */
    String getType();

    /**
     * 获取字段类型完整名
     *
     * @return 字段类型完整名
     */
    String getQualifiedName();
}
