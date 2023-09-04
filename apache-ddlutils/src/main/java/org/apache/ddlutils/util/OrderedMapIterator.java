package org.apache.ddlutils.util;

/**
 * Defines an iterator that operates over an ordered <code>Map</code>.
 * <p>
 * This iterator allows both forward and reverse iteration through the map.
 * @author Stephen Colebourne
 */
public interface OrderedMapIterator<K> extends MapIterator<K>, OrderedIterator<K> {

    /**
     * Checks to see if there is a previous entry that can be iterated to.
     * @return <code>true</code> if the iterator has a previous element
     */
    @Override
    boolean hasPrevious();

    /**
     * Gets the previous <em>key</em> from the <code>Map</code>.
     * @return the previous key in the iteration
     * @throws java.util.NoSuchElementException if the iteration is finished
     */
    @Override
    K previous();
}
