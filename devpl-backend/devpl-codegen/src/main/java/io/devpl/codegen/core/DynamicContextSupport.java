package io.devpl.codegen.core;

import java.util.Map;

/**
 * 动态上下文，支持添加，删除参数，判断是否存在指定名称的参数等
 */
public interface DynamicContextSupport {

    /**
     * 添加上下文参数，用于子类实现逻辑
     *
     * @param key   参数key
     * @param value 参数值
     */
    default void addContext(String key, Object value) {
    }

    /**
     * 批量添加上下文参数，用于子类实现逻辑
     *
     * @param contexMap 多个上下文参数K-V对
     */
    default void addContext(Map<String, Object> contexMap) {
        for (Map.Entry<String, Object> entry : contexMap.entrySet()) {
            addContext(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 移除上下文参数，用于子类实现逻辑
     *
     * @param key 参数key
     */
    default void removeContext(String key) {
    }

    /**
     * 是否存在上下文参数，用于子类实现逻辑
     *
     * @param key 参数key
     */
    default boolean existsContext(String key) {
        return false;
    }
}
