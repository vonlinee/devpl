package io.devpl.codegen.template;

import java.util.Map;

/**
 * 模板参数，使用Map或者对象
 */
public interface TemplateArguments {

    /**
     * 添加参数
     *
     * @param name  参数名
     * @param value 参数值
     */
    void setValue(String name, Object value);

    /**
     * 根据参数名称获取参数值
     *
     * @param name 参数名
     * @return 参数值
     */
    Object getValue(String name);

    /**
     * 实现类是否是Map
     *
     * @return 默认false
     */
    default boolean isMap() {
        return false;
    }

    /**
     * 转为Map结构
     *
     * @return map
     */
    Map<String, Object> asMap();
}
