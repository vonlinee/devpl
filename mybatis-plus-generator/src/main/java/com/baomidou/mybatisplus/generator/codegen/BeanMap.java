package com.baomidou.mybatisplus.generator.codegen;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class BeanMap implements Comparable<BeanMap> {

    private final Map<String, Object> fields;

    public BeanMap() {
        fields = new HashMap<>();
    }

    public Object put(String key, Object value) {
        return fields.put(key, value);
    }

    public String getString(String key) {
        return (String) fields.get(key);
    }

    public Byte getByte(String key) {
        return (Byte) fields.get(key);
    }

    public Short getShort(String key) {
        return (Short) fields.get(key);
    }

    public Integer getInteger(String key) {
        return (Integer) fields.get(key);
    }

    public Integer getInteger(String key, Integer placeholder) {
        Object val = fields.get(key);
        return val == null ? placeholder : (Integer) val;
    }

    public Long getLong(String key) {
        return (Long) fields.get(key);
    }

    public Double getDouble(String key) {
        return (Double) fields.get(key);
    }

    public Double getDouble(String key, Double placeholder) {
        Object val = fields.get(key);
        return val == null ? placeholder : (Double) val;
    }

    public <T> T getValue(String key, Class<T> requiredType) {
        return (T) fields.get(key);
    }

    public <T> T getValue(String key, Class<T> requiredType, T placeholder) {
        Object val = fields.get(key);
        return val == null ? placeholder : (T) val;
    }

    @Override
    public int compareTo(@NotNull BeanMap o) {
        return this.fields.size() - o.fields.size();
    }

    @Override
    public String toString() {
        return fields.toString();
    }
}
