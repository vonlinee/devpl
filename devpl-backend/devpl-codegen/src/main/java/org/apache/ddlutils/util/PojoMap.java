package org.apache.ddlutils.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 替代Map<String, Object>
 */
public final class PojoMap implements Map<String, Object> {

    private final Map<String, Object> map;

    public PojoMap() {
        this(new HashMap<>());
    }

    public PojoMap(Map<String, Object> map) {
        this.map = map;
    }

    public <V> void add(String key, V value) {
        this.map.put(key, value);
    }

    public String getString(String key) {
        return (String) map.get(key);
    }

    public boolean getBoolean(String key) {
        Object val = map.get(key);
        if (val instanceof Boolean bv) {
            return bv;
        }
        return false;
    }

    public short getShort(String key, int defaultValue) {
        Object val = map.get(key);
        if (val instanceof Short) {
            return (short) val;
        } else if (val instanceof Number number) {
            return number.shortValue();
        }
        return (short) defaultValue;
    }

    public short getShort(String key, short defaultValue) {
        Object val = map.get(key);
        if (val instanceof Short) {
            return (short) val;
        } else if (val instanceof Number number) {
            return number.shortValue();
        }
        return defaultValue;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Nullable
    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ?> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @NotNull
    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }
}
