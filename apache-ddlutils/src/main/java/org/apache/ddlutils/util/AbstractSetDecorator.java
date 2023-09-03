package org.apache.ddlutils.util;

import java.util.Set;

/**
 * Decorates another <code>Set</code> to provide additional behaviour.
 * <p>
 * Methods are forwarded directly to the decorated set.
 * @author Stephen Colebourne
 * @since Commons Collections 3.0
 */
public abstract class AbstractSetDecorator<E> extends AbstractCollectionDecorator<E> implements Set<E> {

    /**
     * Constructor only used in deserialization, do not use otherwise.
     * @since Commons Collections 3.1
     */
    protected AbstractSetDecorator() {
        super();
    }

    /**
     * Constructor that wraps (not copies).
     * @param set the set to decorate, must not be null
     * @throws IllegalArgumentException if set is null
     */
    protected AbstractSetDecorator(Set<E> set) {
        super(set);
    }

    /**
     * Gets the set being decorated.
     * @return the decorated set
     */
    protected Set<E> getSet() {
        return (Set<E>) getCollection();
    }
}
