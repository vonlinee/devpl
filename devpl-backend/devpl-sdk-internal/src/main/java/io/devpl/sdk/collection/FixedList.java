package io.devpl.sdk.collection;

import java.util.*;

/**
 * 固定容量大小的列表
 *
 * @param <E>
 */
public class FixedList<E> implements List<E> {

    private final Object[] elementData;
    private transient int cursor;

    public FixedList(int intialCapacity) {
        if (intialCapacity < 0) {
            throw new UnsupportedOperationException("intialCapacity must at least >= 1");
        }
        this.elementData = new Object[intialCapacity];
    }

    @Override
    public int size() {
        return elementData.length;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return elementData;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) elementData;
    }

    @Override
    public boolean add(E e) {
        if (cursor > elementData.length) {
            throw new UnsupportedOperationException("the capacity of FixedList is fixed!");
        }
        elementData[cursor] = e;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public void add(int index, E element) {

    }

    @Override
    public E remove(int index) {
        return null;
    }

    // 6 3
    @SuppressWarnings("unused")
    private int rangeCompare(int index, int min, int max) {
        if (min == max) {
            return Integer.compare(index, min);
        }
        int _min = Math.min(min, max);
        int _max = Math.max(min, max);
        if (index < _min) return -1;
        if (index <= _max) return 0;
        return 1;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) return 0;
        for (int i = 1; i < cursor; i++) {
            if (o == elementData[i] || o.equals(elementData[i])) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIterator<E>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public E next() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public E previous() {
                return null;
            }

            @Override
            public int nextIndex() {
                return 0;
            }

            @Override
            public int previousIndex() {
                return 0;
            }

            @Override
            public void remove() {

            }

            @Override
            public void set(E e) {

            }

            @Override
            public void add(E e) {

            }
        };
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return new ArrayList<>();
    }
}
