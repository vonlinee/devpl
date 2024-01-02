package io.devpl.sdk.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
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

    /**
     * 将数组映射为List
     *
     * @param array  数组元素
     * @param mapper 映射
     * @param <E>    数组元素
     * @param <T>    映射后的元素
     * @return 不可变的list
     */
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

    /**
     * 将数组映射为可变的List
     *
     * @param array  数组元素
     * @param mapper 映射
     * @param <E>    数组元素
     * @param <T>    映射后的元素
     * @return 可变的list，ArrayList
     * @see Arrays#toList(Object[], Function)
     */
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

    /**
     * 数组中是否包含元素
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @param value 被检查的元素
     * @return 是否包含
     */
    public static <T> boolean contains(T[] array, T value) {
        if (array == null) {
            return false;
        }
        for (T element : array) {
            if (Objects.equals(element, value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从元素到该元素在数组中的索引，要求数组中的元素不相同，可以作为Map的key
     * 相同key将被覆盖
     *
     * @param array 数组
     * @param <T>   数组元素
     * @return Map
     */
    public static <T extends Comparable<T>> Map<T, Integer> mapOfElementToIndex(T[] array) {
        Map<T, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            indexMap.put(array[i], i);
        }
        return indexMap;
    }
}
