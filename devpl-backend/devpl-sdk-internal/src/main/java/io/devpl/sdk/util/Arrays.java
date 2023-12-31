package io.devpl.sdk.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * 数组工具类，用于替换java.util.Arrays
 *
 * @see java.util.Arrays
 */
public final class Arrays {

    private Arrays() {
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     */
    public static <E> boolean isEmpty(E[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 构造一个基本类型的int数组
     *
     * @param nums 元素列表
     */
    public static int @NotNull [] intArray(int @Nullable ... nums) {
        if (nums == null) return new int[0];
        return nums;
    }

    /**
     * 构造一个ArrayList
     *
     * @param elements 元素列表
     * @param <T>      元素类型
     * @return ArrayList
     * @see java.util.Arrays#asList(Object[])
     */
    @SafeVarargs
    public static <T> List<T> asArrayList(T... elements) {
        if (elements == null) {
            return new ArrayList<>(0);
        }
        ArrayList<T> list = new ArrayList<>(elements.length);
        list.addAll(java.util.Arrays.asList(elements));
        return list;
    }

    /**
     * 返回一个不可变的集合，用于替代 {@link java.util.Arrays#asList(Object[])}
     *
     * @param elements 元素列表
     * @param <T>      元素类型
     * @return ArrayList
     * @see java.util.Arrays#asList(Object[])
     */
    @SafeVarargs
    public static <T> List<T> asList(T... elements) {
        if (elements == null) {
            return Collections.emptyList();
        }
        if (elements.length == 1) {
            return Collections.singletonList(elements[0]);
        }
        return java.util.Arrays.asList(elements);
    }

    @SuppressWarnings("unchecked")
    public static <E, T> List<T> toList(E[] array, Function<E, T> mapper) {
        if (isEmpty(array)) {
            return Collections.emptyList();
        }
        Object[] toArray = new Object[array.length];
        for (int i = 0; i < array.length; i++) {
            toArray[i] = mapper.apply(array[i]);
        }
        return (List<T>) java.util.Arrays.asList(toArray);
    }

    public static <E, T> List<T> toArrayList(E[] array, Function<E, T> mapper) {
        if (isEmpty(array)) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>();
        for (E e : array) {
            result.add(mapper.apply(e));
        }
        return result;
    }
}
