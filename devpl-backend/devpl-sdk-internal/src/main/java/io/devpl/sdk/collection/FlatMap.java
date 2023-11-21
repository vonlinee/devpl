package io.devpl.sdk.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 针对嵌套Map变成一层Map
 * @param <V>
 */
public class FlatMap<V> implements Map<String, V> {

    private static final String SEPARATOR = "\\.";

    private final transient Map<String, Object> map;

    public FlatMap(Map<String, Object> map) {
        this.map = map;
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
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(String key, V value) {
        final String[] split = key.split(SEPARATOR);
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return null;
    }
}
