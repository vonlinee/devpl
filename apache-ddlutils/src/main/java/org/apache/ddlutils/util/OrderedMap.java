package org.apache.ddlutils.util;

/**
 * Defines a map that maintains order and allows both forward and backward
 * iteration through that order.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 1.6 $ $Date: 2004/02/18 01:15:42 $
 *
 * @author Stephen Colebourne
 */
public interface OrderedMap extends IterableMap {

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
     *
     * @return a map iterator
     */
    OrderedMapIterator orderedMapIterator();

    /**
     * Gets the first key currently in this map.
     *
     * @return the first key currently in this map
     * @throws java.util.NoSuchElementException if this map is empty
     */
    public Object firstKey();

    /**
     * Gets the last key currently in this map.
     *
     * @return the last key currently in this map
     * @throws java.util.NoSuchElementException if this map is empty
     */
    public Object lastKey();

    /**
     * Gets the next key after the one specified.
     *
     * @param key  the key to search for next from
     * @return the next key, null if no match or at end
     */
    public Object nextKey(Object key);

    /**
     * Gets the previous key before the one specified.
     *
     * @param key  the key to search for previous from
     * @return the previous key, null if no match or at start
     */
    public Object previousKey(Object key);

}
