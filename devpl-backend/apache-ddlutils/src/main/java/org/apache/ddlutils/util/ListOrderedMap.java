package org.apache.ddlutils.util;

import org.apache.ddlutils.util.collections.AbstractUntypedIteratorDecorator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Decorates a <code>Map</code> to ensure that the order of addition is retained
 * using a <code>List</code> to maintain order.
 * <p>
 * The order will be used via the iterators and toArray methods on the views.
 * The order is also returned by the <code>MapIterator</code>.
 * The <code>orderedMapIterator()</code> method accesses an iterator that can
 * iterate both forwards and backwards through the map.
 * In addition, non-interface methods are provided to access the map by index.
 * <p>
 * If an object is added to the Map for a second time, it will remain in the
 * original position in the iteration.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 * @author Henri Yandell
 * @author Stephen Colebourne
 * @version $Revision: 1.16 $ $Date: 2004/06/07 21:51:39 $
 * @since Commons Collections 3.0
 */
public class ListOrderedMap<K, V> extends AbstractMapDecorator<K, V> implements OrderedMap<K, V>, Serializable {

    /**
     * Serialization version
     */
    private static final long serialVersionUID = 2728177751851003750L;

    /**
     * Internal list to hold the sequence of objects
     */
    protected final List<K> insertOrder = new ArrayList<>();

    //-----------------------------------------------------------------------

    /**
     * Constructs a new empty <code>ListOrderedMap</code> that decorates
     * a <code>HashMap</code>.
     * @since Commons Collections 3.1
     */
    public ListOrderedMap() {
        this(new HashMap<>());
    }

    /**
     * Constructor that wraps (not copies).
     * @param map the map to decorate, must not be null
     * @throws IllegalArgumentException if map is null
     */
    protected ListOrderedMap(Map<K, V> map) {
        super(map);
        insertOrder.addAll(getMap().keySet());
    }

    //-----------------------------------------------------------------------

    /**
     * Write the map out using a custom routine.
     * @param out the output stream
     * @throws IOException
     * @since Commons Collections 3.1
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(map);
    }

    /**
     * Read the map in using a custom routine.
     * @param in the input stream
     * @since Commons Collections 3.1
     */
    @SuppressWarnings("unchecked") // (1) should only fail if input stream is incorrect
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map<K, V>) in.readObject();
    }

    // Implement OrderedMap
    //-----------------------------------------------------------------------
    @Override
    public MapIterator<K, V> mapIterator() {
        return orderedMapIterator();
    }

    @Override
    public OrderedMapIterator<K, V> orderedMapIterator() {
        return new ListOrderedMapIterator<>(this);
    }

    /**
     * Gets the first key in this map by insert order.
     * @return the first key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    @Override
    public K firstKey() {
        if (size() == 0) {
            throw new NoSuchElementException("Map is empty");
        }
        return insertOrder.get(0);
    }

    /**
     * Gets the last key in this map by insert order.
     * @return the last key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    @Override
    public K lastKey() {
        if (size() == 0) {
            throw new NoSuchElementException("Map is empty");
        }
        return insertOrder.get(size() - 1);
    }

    /**
     * Gets the next key to the one specified using insert order.
     * This method performs a list search to find the key and is O(n).
     * @param key the key to find previous for
     * @return the next key, null if no match or at start
     */
    @Override
    public K nextKey(K key) {
        int index = insertOrder.indexOf(key);
        if (index >= 0 && index < size() - 1) {
            return insertOrder.get(index + 1);
        }
        return null;
    }

    /**
     * Gets the previous key to the one specified using insert order.
     * This method performs a list search to find the key and is O(n).
     * @param key the key to find previous for
     * @return the previous key, null if no match or at start
     */
    public K previousKey(K key) {
        int index = insertOrder.indexOf(key);
        if (index > 0) {
            return insertOrder.get(index - 1);
        }
        return null;
    }

    //-----------------------------------------------------------------------
    @Override
    public V put(K key, V value) {
        if (getMap().containsKey(key)) {
            // re-adding doesn't change order
            return getMap().put(key, value);
        } else {
            // first add, so add to both map and list
            V result = getMap().put(key, value);
            insertOrder.add(key);
            return result;
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(Object key) {
        V result = getMap().remove(key);
        insertOrder.remove(key);
        return result;
    }

    public void clear() {
        getMap().clear();
        insertOrder.clear();
    }

    //-----------------------------------------------------------------------
    public Set<K> keySet() {
        return new KeySetView<>(this);
    }

    @Override
    public @NotNull Collection<V> values() {
        return new ValuesView<>(this);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySetView<>(this, this.insertOrder);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns the Map as a string.
     * @return the Map as a String
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder buf = new StringBuilder();
        buf.append('{');
        boolean first = true;
        for (Entry<K, V> entry : entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (first) {
                first = false;
            } else {
                buf.append(", ");
            }
            buf.append(key == this ? "(this Map)" : key);
            buf.append('=');
            buf.append(value == this ? "(this Map)" : value);
        }
        buf.append('}');
        return buf.toString();
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the key at the specified index.
     * @param index the index to retrieve
     * @return the key at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Object get(int index) {
        return insertOrder.get(index);
    }

    /**
     * Gets the value at the specified index.
     * @param index the index to retrieve
     * @return the key at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Object getValue(int index) {
        return get(insertOrder.get(index));
    }

    /**
     * Gets the index of the specified key.
     * @param key the key to find the index of
     * @return the index, or -1 if not found
     */
    public int indexOf(K key) {
        return insertOrder.indexOf(key);
    }

    /**
     * Removes the element at the specified index.
     * @param index the index of the object to remove
     * @return the previous value corresponding the <code>key</code>,
     * or <code>null</code> if none existed
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Object remove(int index) {
        return remove(get(index));
    }

    /**
     * Gets an unmodifiable List view of the keys which changes as the map changes.
     * <p>
     * The returned list is unmodifiable because changes to the values of
     * the list (using {@link java.util.ListIterator#set(Object)}) will
     * effectively remove the value from the list and reinsert that value at
     * the end of the list, which is an unexpected side effect of changing the
     * value of a list.  This occurs because changing the key, changes when the
     * mapping is added to the map and thus where it appears in the list.
     * <p>
     * An alternative to this method is to use {@link #keySet()}.
     * @return The ordered list of keys.
     * @see #keySet()
     */
    public List<K> asList() {
        return Collections.unmodifiableList(insertOrder);
    }

    //-----------------------------------------------------------------------
    static class ValuesView<K, V> extends AbstractCollection<V> {
        private final ListOrderedMap<Object, V> parent;

        @SuppressWarnings("unchecked")
        ValuesView(ListOrderedMap<?, V> parent) {
            super();
            this.parent = (ListOrderedMap<Object, V>) parent;
        }

        @Override
        public int size() {
            return this.parent.size();
        }

        @Override
        public boolean contains(Object value) {
            return this.parent.containsValue(value);
        }

        @Override
        public void clear() {
            this.parent.clear();
        }

        @Override
        public Iterator<V> iterator() {
            return new AbstractUntypedIteratorDecorator<>(parent.entrySet().iterator()) {
                @Override
                public V next() {
                    return getIterator().next().getValue();
                }
            };
        }
    }

    //-----------------------------------------------------------------------
    static class KeySetView<K> extends AbstractSet<K> {
        private final ListOrderedMap<K, ?> parent;

        KeySetView(ListOrderedMap<K, ?> parent) {
            super();
            this.parent = parent;
        }

        @Override
        public int size() {
            return this.parent.size();
        }

        @Override
        public boolean contains(Object value) {
            return this.parent.containsKey(value);
        }

        @Override
        public void clear() {
            this.parent.clear();
        }

        @Override
        public Iterator<K> iterator() {
            return parent.keySet().iterator();
        }
    }

    //-----------------------------------------------------------------------
    static class EntrySetView<K, V> extends AbstractSet<Map.Entry<K, V>> {
        private final ListOrderedMap<K, V> parent;
        private final List<K> insertOrder;
        private Set<Map.Entry<K, V>> entrySet;

        public EntrySetView(ListOrderedMap<K, V> parent, List<K> insertOrder) {
            super();
            this.parent = parent;
            this.insertOrder = insertOrder;
        }

        private Set<Map.Entry<K, V>> getEntrySet() {
            if (entrySet == null) {
                entrySet = parent.getMap().entrySet();
            }
            return entrySet;
        }

        @Override
        public int size() {
            return this.parent.size();
        }

        @Override
        public boolean isEmpty() {
            return this.parent.isEmpty();
        }

        @Override
        public boolean contains(Object obj) {
            return getEntrySet().contains(obj);
        }

        @Override
        public boolean containsAll(Collection<?> coll) {
            return getEntrySet().containsAll(coll);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            if (getEntrySet().contains(obj)) {
                Object key = ((Map.Entry<K, V>) obj).getKey();
                parent.remove(key);
                return true;
            }
            return false;
        }

        @Override
        public void clear() {
            this.parent.clear();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            return getEntrySet().equals(obj);
        }

        @Override
        public int hashCode() {
            return getEntrySet().hashCode();
        }

        @Override
        public String toString() {
            return getEntrySet().toString();
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new ListOrderedIterator<>(parent, insertOrder);
        }
    }

    //-----------------------------------------------------------------------
    static class ListOrderedIterator<K, V> extends AbstractUntypedIteratorDecorator<K, Map.Entry<K, V>> {
        private final ListOrderedMap<K, V> parent;
        private K last = null;

        ListOrderedIterator(ListOrderedMap<K, V> parent, List<K> insertOrder) {
            super(insertOrder.iterator());
            this.parent = parent;
        }

        @Override
        public Map.Entry<K, V> next() {
            last = getIterator().next();
            return new ListOrderedMapEntry<>(parent, last);
        }

        @Override
        public void remove() {
            super.remove();
            parent.getMap().remove(last);
        }
    }

    //-----------------------------------------------------------------------
    static class ListOrderedMapEntry<K, V> extends AbstractMapEntry<K, V> {
        private final ListOrderedMap<K, V> parent;

        ListOrderedMapEntry(ListOrderedMap<K, V> parent, K key) {
            super(key, null);
            this.parent = parent;
        }

        @Override
        public V getValue() {
            return parent.get(key);
        }

        @Override
        public V setValue(V value) {
            return parent.getMap().put(key, value);
        }
    }

    //-----------------------------------------------------------------------
    static class ListOrderedMapIterator<K, V> implements OrderedMapIterator<K, V>, ResettableIterator<K> {
        private final ListOrderedMap<K, V> parent;
        private ListIterator<K> iterator;
        private K last = null;
        private boolean readable = false;

        ListOrderedMapIterator(ListOrderedMap<K, V> parent) {
            super();
            this.parent = parent;
            this.iterator = parent.insertOrder.listIterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public K next() {
            last = iterator.next();
            readable = true;
            return last;
        }

        @Override
        public boolean hasPrevious() {
            return iterator.hasPrevious();
        }

        @Override
        public K previous() {
            last = iterator.previous();
            readable = true;
            return last;
        }

        @Override
        public void remove() {
            if (!readable) {
                throw new IllegalStateException("remove() can only be called once after next()");
            }
            iterator.remove();
            parent.map.remove(last);
            readable = false;
        }

        @Override
        public K getKey() {
            if (!readable) {
                throw new IllegalStateException("getKey() can only be called after next() and before remove()");
            }
            return last;
        }

        @Override
        public Object getValue() {
            if (!readable) {
                throw new IllegalStateException("getValue() can only be called after next() and before remove()");
            }
            return parent.get(last);
        }

        @Override
        public V setValue(V value) {
            if (!readable) {
                throw new IllegalStateException("setValue() can only be called after next() and before remove()");
            }
            return parent.map.put(last, value);
        }

        @Override
        public void reset() {
            iterator = parent.insertOrder.listIterator();
            last = null;
            readable = false;
        }

        @Override
        public String toString() {
            if (readable) {
                return "Iterator[" + getKey() + "=" + getValue() + "]";
            } else {
                return "Iterator[]";
            }
        }
    }
}
