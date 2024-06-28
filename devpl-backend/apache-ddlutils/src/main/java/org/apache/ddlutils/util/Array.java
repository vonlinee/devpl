package org.apache.ddlutils.util;

import java.util.List;
import java.util.RandomAccess;

@SuppressWarnings("unchecked")
public final class Array<E> implements RandomAccess {

    private final Object[] elements;

    public Array(int length) {
        this.elements = new Object[length];
    }

    public Array(E[] elements) {
        this.elements = elements;
    }

    public E get(int index) {
        if (elements == null) {
            return null;
        }
        if (index < 0 || index >= elements.length) {
            return null;
        }
        return (E) elements[index];
    }

    public void set(int index, E value) {
        if (elements == null) {
            return;
        }
        if (index < 0 || index >= elements.length) {
            return;
        }
        elements[index] = value;
    }

    public int length() {
        return elements == null ? 0 : elements.length;
    }

    public boolean isEmpty() {
        return elements == null || elements.length == 0;
    }

    public static <E> Array<E> of(E... elements) {
        return new Array<>(elements);
    }

    public static <E> Array<E> of(List<E> list) {
        if (list == null) {
            return new Array<>(null);
        }
        return (Array<E>) new Array<>(list.toArray());
    }
}
