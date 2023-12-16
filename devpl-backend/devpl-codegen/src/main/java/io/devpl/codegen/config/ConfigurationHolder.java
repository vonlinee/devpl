package io.devpl.codegen.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放动态的配置信息
 */
public class ConfigurationHolder {

    /**
     * 配置Map
     */
    private final Map<String, Object> configurations;

    public ConfigurationHolder() {
        configurations = new ConcurrentHashMap<>();
    }

    /**
     * 设置配置项的值
     *
     * @param name  配置项的key
     * @param value 配置项的值
     */
    public void setValue(String name, Object value) {
        configurations.put(name, value);
    }

    /**
     * 获取值为字符串配置
     *
     * @param name         配置名称
     * @param defaultValue 默认值
     * @return 配置项值
     */
    public String getString(String name, String defaultValue) {
        Object val = configurations.get(name);
        if (val == null) {
            return defaultValue;
        }
        if (!(val instanceof String)) {
            throw new ClassCastException("the value of config[" + name + "] is not a string");
        }
        return (String) val;
    }

    /**
     * 获取值为Number类型的配置
     *
     * @param name         配置名称
     * @param defaultValue 默认值
     * @return 配置项值
     */
    public final Number getNumber(String name, Number defaultValue) {
        Object val = configurations.get(name);
        if (val == null) {
            return defaultValue;
        }
        if (!(val instanceof Number)) {
            throw new ClassCastException("the value of config[" + name + "] is not a number");
        }
        return (Number) val;
    }

    /**
     * 获取值为对象类型的配置
     *
     * @param name         配置名称
     * @param defaultValue 默认值
     * @return 配置项值
     */
    @SuppressWarnings("unchecked")
    public final <T> T getValue(String name, Class<T> requiredType, T defaultValue) {
        Object val = configurations.get(name);
        if (val == null) {
            return defaultValue;
        }
        if (requiredType == null) {
            throw new IllegalArgumentException("required type cannot be null");
        }
        if (!(requiredType.isAssignableFrom(val.getClass()))) {
            throw new ClassCastException("the value of config[" + name + "] is not a type of " + requiredType);
        }
        return (T) val;
    }
}
