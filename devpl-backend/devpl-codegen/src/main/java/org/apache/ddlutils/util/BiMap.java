package org.apache.ddlutils.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * just for internal useage
 *
 * @param <K>
 * @param <V>
 */
public class BiMap<K, V> {

    private final Map<K, V> kvMap;
    private final Map<V, K> vkMap;

    public BiMap() {
        this.kvMap = new HashMap<>();
        this.vkMap = new HashMap<>();
    }

    /**
     * value -> key
     *
     * @param value value
     * @return key
     */
    public K getKey(V value) {
        if (value == null) {
            return null;
        }
        return vkMap.get(value);
    }

    public V getValue(K key) {
        if (key == null) {
            return null;
        }
        return kvMap.get(key);
    }

    public V put(K key, V value) {
        Objects.requireNonNull(key, "key can not be null");
        Objects.requireNonNull(key, "value can not be null");
        vkMap.put(value, key);
        return kvMap.put(key, value);
    }

    public Set<K> keys() {
        return kvMap.keySet();
    }

    public Set<V> values() {
        return vkMap.keySet();
    }
}
