package org.apache.ddlutils.util;

import java.util.Map;

/**
 * Abstract Pair class to assist with creating correct Map Entry implementations.
 * @author James Strachan
 * @author Michael A. Smith
 * @author Neil O'Toole
 * @author Stephen Colebourne
 * @version $Revision: 1.4 $ $Date: 2004/02/18 01:00:08 $
 * @since Commons Collections 3.0
 */
public abstract class AbstractMapEntry<K, V> extends AbstractKeyValue<K, V> implements Map.Entry<K, V> {

    /**
     * Constructs a new entry with the given key and given value.
     * @param key   the key for the entry, may be null
     * @param value the value for the entry, may be null
     */
    protected AbstractMapEntry(K key, V value) {
        super(key, value);
    }

    // Map.Entry interface
    //-------------------------------------------------------------------------

    /**
     * Sets the value stored in this Map Entry.
     * <p>
     * This Map Entry is not connected to a Map, so only the local data is changed.
     * @param value the new value
     * @return the previous value
     */
    @Override
    public V setValue(V value) {
        V answer = this.value;
        this.value = value;
        return answer;
    }

    /**
     * Compares this Map Entry with another Map Entry.
     * <p>
     * Implemented per API documentation of {@link java.util.Map.Entry#equals(Object)}
     * @param obj the object to compare to
     * @return true if equal key and value
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map.Entry)) {
            return false;
        }
        Map.Entry<K, V> other = (Map.Entry<K, V>) obj;
        return (getKey() == null ? other.getKey() == null : getKey().equals(other.getKey())) &&
            (getValue() == null ? other.getValue() == null : getValue().equals(other.getValue()));
    }

    /**
     * Gets a hashCode compatible with the equals' method.
     * <p>
     * Implemented per API documentation of {@link java.util.Map.Entry#hashCode()}
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return (getKey() == null ? 0 : getKey().hashCode()) ^
            (getValue() == null ? 0 : getValue().hashCode());
    }
}
