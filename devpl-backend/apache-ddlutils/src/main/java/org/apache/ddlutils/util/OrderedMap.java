package org.apache.ddlutils.util;

/**
 * Defines a map that maintains order and allows both forward and backward
 * iteration through that order.
 * @author Stephen Colebourne
 */
public interface OrderedMap<K, V> extends IterableMap<K, V> {

    /**
     * Obtains an <code>OrderedMapIterator</code> over the map.
     * <p>
     * A ordered map iterator is an efficient way of iterating over maps
     * in both directions.
     * <pre>
     * BidiMap map = new TreeBidiMap();
     * MapIterator it = map.mapIterator();
     * while (it.hasNext()) {
     *   Object key = it.next();
     *   Object value = it.getValue();
     *   it.setValue("newValue");
     *   Object previousKey = it.previous();
     * }
     * </pre>
     * @return a map iterator
     */
    OrderedMapIterator<K, V> orderedMapIterator();

    /**
     * Gets the first key currently in this map.
     * @return the first key currently in this map
     * @throws java.util.NoSuchElementException if this map is empty
     */
    K firstKey();

    /**
     * Gets the last key currently in this map.
     * @return the last key currently in this map
     * @throws java.util.NoSuchElementException if this map is empty
     */
    K lastKey();

    /**
     * Gets the next key after the one specified.
     * @param key the key to search for next from
     * @return the next key, null if no match or at end
     */
    K nextKey(K key);

    /**
     * Gets the previous key before the one specified.
     * @param key the key to search for previous from
     * @return the previous key, null if no match or at start
     */
    K previousKey(K key);
}
