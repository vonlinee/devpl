package org.apache.ddlutils.util;

/**
 * Abstract pair class to assist with creating KeyValue and MapEntry implementations.
 * @author James Strachan
 * @author Michael A. Smith
 * @author Neil O'Toole
 * @author Stephen Colebourne
 */
public abstract class AbstractKeyValue<K, V> implements KeyValue<K, V> {

    /**
     * The key
     */
    protected K key;
    /**
     * The value
     */
    protected V value;

    /**
     * Constructs a new pair with the specified key and given value.
     * @param key   the key for the entry, may be null
     * @param value the value for the entry, may be null
     */
    protected AbstractKeyValue(K key, V value) {
        super();
        this.key = key;
        this.value = value;
    }

    /**
     * Gets the key from the pair.
     * @return the key
     */
    @Override
    public K getKey() {
        return key;
    }

    /**
     * Gets the value from the pair.
     * @return the value
     */
    @Override
    public V getValue() {
        return value;
    }

    /**
     * Gets a debugging String view of the pair.
     * @return a String view of the entry
     */
    @Override
    public String toString() {
        return String.valueOf(getKey()) +
               '=' +
               getValue();
    }
}
