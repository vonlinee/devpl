package io.devpl.sdk;

public interface Getter<K, V> {

    V get(K key);

    default V get(K key, V defaultValue) {
        V v = get(key);
        return v == null ? defaultValue : v;
    }

    default V get(K key, Class<?> type) {
        V v = get(key);
        if (type == null || v.getClass() == type || type.isAssignableFrom(v.getClass())) {
            return v;
        }
        return null;
    }

    default V get(K key, Class<?> type, V defaultValue) {
        V v = get(key);
        if (type == null || v.getClass() == type || type.isAssignableFrom(v.getClass())) {
            return v;
        }
        return defaultValue;
    }
}
