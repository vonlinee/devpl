package org.apache.ddlutils.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Provides a base decorator that enables additional functionality to be added
 * to a Map via decoration.
 * <p>
 * Methods are forwarded directly to the decorated map.
 * <p>
 * This implementation does not perform any special processing with
 * {@link #entrySet()}, {@link #keySet()} or {@link #values()}. Instead
 * it simply returns the set/collection from the wrapped map. This may be
 * undesirable, for example if you are trying to write a validating
 * implementation it would provide a loophole around the validation.
 * But, you might want that loophole, so this class is kept simple.
 * @author Daniel Rall
 * @author Stephen Colebourne
 * @version $Revision: 1.5 $ $Date: 2004/04/02 21:02:54 $
 * @since Commons Collections 3.0
 */
public abstract class AbstractMapDecorator<K, V> implements Map<K, V> {

    /**
     * The map to decorate
     */
    protected transient Map<K, V> map;

    /**
     * Constructor only used in deserialization, do not use otherwise.
     * @since Commons Collections 3.1
     */
    protected AbstractMapDecorator() {
        super();
    }

    /**
     * Constructor that wraps (not copies).
     * @param map the map to decorate, must not be null
     * @throws IllegalArgumentException if the collection is null
     */
    public AbstractMapDecorator(Map<K, V> map) {
        if (map == null) {
            throw new IllegalArgumentException("Map must not be null");
        }
        this.map = map;
    }

    /**
     * Gets the map being decorated.
     * @return the decorated map
     */
    protected Map<K, V> getMap() {
        return map;
    }

    //-----------------------------------------------------------------------
    @Override
    public void clear() {
        map.clear();
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
    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public @NotNull Collection<V> values() {
        return map.values();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        return map.equals(object);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
