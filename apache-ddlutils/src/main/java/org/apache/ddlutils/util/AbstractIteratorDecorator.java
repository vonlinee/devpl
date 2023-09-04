package org.apache.ddlutils.util;

import java.util.Iterator;

/**
 * Provides basic behaviour for decorating an iterator with extra functionality.
 * <p>
 * All methods are forwarded to the decorated iterator.
 * @author James Strachan
 * @author Stephen Colebourne
 * @version $Revision: 1.4 $ $Date: 2004/02/18 00:59:50 $
 * @since Commons Collections 3.0
 */
public class AbstractIteratorDecorator<E> implements Iterator<E> {

    /**
     * The iterator being decorated
     */
    protected final Iterator<E> iterator;

    //-----------------------------------------------------------------------

    /**
     * Constructor that decorates the specified iterator.
     * @param iterator the iterator to decorate, must not be null
     * @throws IllegalArgumentException if the collection is null
     */
    public AbstractIteratorDecorator(Iterator<E> iterator) {
        super();
        if (iterator == null) {
            throw new IllegalArgumentException("Iterator must not be null");
        }
        this.iterator = iterator;
    }

    //-----------------------------------------------------------------------
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public E next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}
