package org.apache.ddlutils.util;

import java.util.Iterator;

/**
 * Defines an iterator that can be reset back to an initial state.
 * <p>
 * This interface allows an iterator to be repeatedly reused.
 * @author Stephen Colebourne
 * @version $Revision: 1.4 $ $Date: 2004/02/18 01:15:42 $
 * @since Commons Collections 3.0
 */
public interface ResettableIterator<E> extends Iterator<E> {

    /**
     * Resets the iterator back to the position at which the iterator
     * was created.
     */
    void reset();
}
