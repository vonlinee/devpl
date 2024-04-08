package org.apache.ddlutils.util;

import java.util.*;

/**
 * a map to support one key mapping multiple value
 *
 * @param <K> key type
 * @param <V> value type
 */
public class MultiValueMap<K, V> {

    private final Map<K, Collection<V>> map = new HashMap<>();

    public boolean containsValue(K key, V value) {
        Collection<V> collection = map.get(key);
        if (collection == null || collection.isEmpty()) {
            return false;
        }
        for (V val : collection) {
            if (Objects.equals(val, value)) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    public final void addAll(K key, V... values) {
        Collection<V> collection = map.get(key);
        if (collection != null) {
            collection.addAll(List.of(values));
        } else {
            collection = new HashSet<>(List.of(values));
            map.put(key, collection);
        }
    }
}
