package io.devpl.sdk;

import java.util.Map;
import java.util.Objects;

/**
 * <p>A generic key/value pair.</p>
 */
public class KVPair<K, V> implements Map.Entry<K, V> {

    private K key;
    private V value;

    public KVPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KVPair)) {
            return false;
        }
        KVPair<?, ?> that = (KVPair<?, ?>) obj;
        return Objects.equals(this.key, that.key) && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return key.hashCode() ^ value.hashCode();
    }

    @Override
    public String toString() {
        return this.key + "=" + this.value;
    }
}
