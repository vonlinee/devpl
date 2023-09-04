package org.apache.ddlutils.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Decorates another <code>Set</code> to ensure that the order of addition
 * is retained and used by the iterator.
 * <p>
 * If an object is added to the set for a second time, it will remain in the
 * original position in the iteration.
 * The order can be observed from the set via the iterator or toArray methods.
 * <p>
 * The ListOrderedSet also has various useful direct methods. These include many
 * from <code>List</code>, such as <code>get(int)</code>, <code>remove(int)</code>
 * and <code>indexOf(int)</code>. An unmodifiable <code>List</code> view of
 * the set can be obtained via <code>asList()</code>.
 * <p>
 * This class cannot implement the <code>List</code> interface directly as
 * various interface methods (notably equals/hashCode) are incompatable with a set.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 * @author Stephen Colebourne
 * @author Henning P. Schmiedehausen
 * @version $Revision: 1.9 $ $Date: 2004/06/07 21:42:12 $
 * @since Commons Collections 3.0
 */
public class ListOrderedSet<E> extends AbstractSerializableSetDecorator<E> implements Set<E> {

    /**
     * Serialization version
     */
    private static final long serialVersionUID = -228664372470420141L;

    /**
     * Internal list to hold the sequence of objects
     */
    protected final List<E> setOrder;

    /**
     * Factory method to create an ordered set specifying the list and set to use.
     * @param set  the set to decorate, must be empty and not null
     * @param list the list to decorate, must be empty and not null
     * @throws IllegalArgumentException if set or list is null
     * @throws IllegalArgumentException if either the set or list is not empty
     * @since Commons Collections 3.1
     */
    public static <E> ListOrderedSet<E> decorate(Set<E> set, List<E> list) {
        if (set == null) {
            throw new IllegalArgumentException("Set must not be null");
        }
        if (list == null) {
            throw new IllegalArgumentException("List must not be null");
        }
        if (!set.isEmpty() || !list.isEmpty()) {
            throw new IllegalArgumentException("Set and List must be empty");
        }
        return new ListOrderedSet<>(set, list);
    }

    /**
     * Factory method to create an ordered set.
     * <p>
     * An <code>ArrayList</code> is used to retain order.
     * @param set the set to decorate, must not be null
     * @throws IllegalArgumentException if set is null
     */
    public static <E> ListOrderedSet<E> decorate(Set<E> set) {
        return new ListOrderedSet<>(set);
    }

    /**
     * Factory method to create an ordered set using the supplied list to retain order.
     * <p>
     * A <code>HashSet</code> is used for the set behaviour.
     * @param list the list to decorate, must not be null
     * @throws IllegalArgumentException if list is null
     */
    public static <T> ListOrderedSet<T> decorate(List<T> list) {
        if (list == null) {
            throw new IllegalArgumentException("List must not be null");
        }
        Set<T> set = new HashSet<>(list);
        list.retainAll(set);

        return new ListOrderedSet<>(set, list);
    }

    //-----------------------------------------------------------------------

    /**
     * Constructs a new empty <code>ListOrderedSet</code> using
     * a <code>HashSet</code> and an <code>ArrayList</code> internally.
     * @since Commons Collections 3.1
     */
    public ListOrderedSet() {
        super(new HashSet<>());
        setOrder = new ArrayList<>();
    }

    /**
     * Constructor that wraps (not copies).
     * @param set the set to decorate, must not be null
     * @throws IllegalArgumentException if set is null
     */
    protected ListOrderedSet(Set<E> set) {
        super(set);
        setOrder = new ArrayList<>(set);
    }

    /**
     * Constructor that wraps (not copies) the Set and specifies the list to use.
     * <p>
     * The set and list must both be correctly initialised to the same elements.
     * @param set  the set to decorate, must not be null
     * @param list the list to decorate, must not be null
     * @throws IllegalArgumentException if set or list is null
     */
    protected ListOrderedSet(Set<E> set, List<E> list) {
        super(set);
        if (list == null) {
            throw new IllegalArgumentException("List must not be null");
        }
        setOrder = list;
    }

    //-----------------------------------------------------------------------

    /**
     * Gets an unmodifiable view of the order of the Set.
     * @return an unmodifiable list view
     */
    public List<E> asList() {
        return Collections.unmodifiableList(setOrder);
    }

    //-----------------------------------------------------------------------
    @Override
    public void clear() {
        collection.clear();
        setOrder.clear();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return new OrderedSetIterator<>(setOrder.iterator(), collection);
    }

    @Override
    public boolean add(E object) {
        if (collection.contains(object)) {
            // re-adding doesn't change order
            return collection.add(object);
        } else {
            // first add, so add to both set and list
            boolean result = collection.add(object);
            setOrder.add(object);
            return result;
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> coll) {
        boolean result = false;
        for (E object : coll) {
            result = result | add(object);
        }
        return result;
    }

    @Override
    public boolean remove(Object object) {
        boolean result = collection.remove(object);
        setOrder.remove(object);
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> coll) {
        boolean result = false;
        for (Object object : coll) {
            result = result | remove(object);
        }
        return result;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> coll) {
        boolean result = collection.retainAll(coll);
        if (!result) {
            return false;
        } else if (collection.isEmpty()) {
            setOrder.clear();
        } else {
            setOrder.removeIf(object -> !collection.contains(object));
        }
        return true;
    }

    @Override
    public Object @NotNull [] toArray() {
        return setOrder.toArray();
    }

    @Override
    public Object[] toArray(Object @NotNull [] a) {
        return setOrder.toArray(a);
    }

    //-----------------------------------------------------------------------
    public Object get(int index) {
        return setOrder.get(index);
    }

    public int indexOf(E object) {
        return setOrder.indexOf(object);
    }

    public void add(int index, E object) {
        if (!contains(object)) {
            collection.add(object);
            setOrder.add(index, object);
        }
    }

    public boolean addAll(int index, Collection<E> coll) {
        boolean changed = false;
        for (E object : coll) {
            if (!contains(object)) {
                collection.add(object);
                setOrder.add(index, object);
                index++;
                changed = true;
            }
        }
        return changed;
    }

    public Object remove(int index) {
        Object obj = setOrder.remove(index);
        remove(obj);
        return obj;
    }

    /**
     * Uses the underlying List's toString so that order is achieved.
     * This means that the decorated Set's toString is not used, so
     * any custom toStrings will be ignored.
     */
    // Fortunately List.toString and Set.toString look the same
    @Override
    public String toString() {
        return setOrder.toString();
    }

    //-----------------------------------------------------------------------

    /**
     * Internal iterator handle remove.
     */
    static class OrderedSetIterator<T> extends AbstractIteratorDecorator<T> {

        /**
         * Object we iterate on
         */
        protected final Collection<T> set;
        /**
         * Last object retrieved
         */
        protected T last;

        private OrderedSetIterator(Iterator<T> iterator, Collection<T> set) {
            super(iterator);
            this.set = set;
        }

        @Override
        public T next() {
            last = iterator.next();
            return last;
        }

        @Override
        public void remove() {
            set.remove(last);
            iterator.remove();
            last = null;
        }
    }
}
