package org.apache.ddlutils.util;

/**
 * Defines an iterator that operates over an ordered <code>Map</code>.
 * <p>
 * This iterator allows both forward and reverse iteration through the map.
 * @author Stephen Colebourne
 * @version $Revision: 1.4 $ $Date: 2004/02/18 01:15:42 $
 * @since Commons Collections 3.0
 */
public interface OrderedMapIterator extends MapIterator, OrderedIterator {

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
    Object previous();
}
