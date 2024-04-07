package io.devpl.codegen.generator.config;

import io.devpl.codegen.db.JavaFieldDataType;

/**
 * 数据库字段类型转换
 */
public interface TypeConverter {

    /**
     * 执行类型转换
     *
     * @param globalConfiguration 全局配置
     * @param fieldType           字段类型
     * @return ignore
     */
    JavaFieldDataType convert(GlobalConfiguration globalConfiguration, String fieldType);
}
