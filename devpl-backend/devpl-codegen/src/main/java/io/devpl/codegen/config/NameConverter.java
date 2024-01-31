package io.devpl.codegen.config;

import io.devpl.codegen.core.TableGeneration;

/**
 * 名称转换接口类
 *
 * @author hubin
 * @since 2017-01-20
 */
public interface NameConverter {

    /**
     * 执行实体名称转换
     *
     * @param tableInfo 表信息对象
     * @return 实体名称
     */
    String entityNameConvert(TableGeneration tableInfo);

    /**
     * 执行属性名称转换
     *
     * @param fieldName 表字段对象，如果属性表字段命名不一致注意 convert 属性的设置
     * @return 属性名称
     */
    String propertyNameConvert(String fieldName);
}
