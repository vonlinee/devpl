package io.devpl.codegen.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class DefaultContextObject implements ContextObject {

    private final Map<String, Object> stringKeyMap = new HashMap<>();
    private final Map<Class<?>, Object> classKeyMap = new HashMap<>();

    @Override
    public <T> T getObject(Class<T> type) {
        Objects.requireNonNull(type, "type cannot be null");
        return (T) classKeyMap.get(type);
    }

    @Override
    public <T> T getObject(Class<T> type, T defaultValue) {
        Objects.requireNonNull(type, "type cannot be null");
        T val = getObject(type);
        return val == null ? defaultValue : val;
    }

    @Override
    public <T> T putObject(T object) {
        Objects.requireNonNull(object, "object cannot be null");
        return (T) classKeyMap.put(object.getClass(), object);
    }

    @Override
    public <C extends T, T> T putObject(Class<T> type, C object) {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(object, "object cannot be null");
        return (T) classKeyMap.put(type, object);
    }

    @Override
    public <T> T put(String key, T object) {
        return (T) stringKeyMap.put(key, object);
    }

    @Override
    public <T> T get(String key, T defaultValue) {
        return (T) stringKeyMap.getOrDefault(key, defaultValue);
    }

    @Override
    public boolean contains(Object key) {
        if (key instanceof Class<?>) {
            return classKeyMap.containsKey(key);
        } else if (key instanceof String) {
            return stringKeyMap.containsKey(key);
        } else if (key != null) {
            return classKeyMap.containsKey(key.getClass());
        }
        return false;
    }
}
