package io.devpl.sdk.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数组工具类，用于替换java.util.Arrays
 *
 * @see java.util.Arrays
 */
public final class Arrays {

    private Arrays() {
    }

    /**
     * 构造一个基本类型的int数组
     *
     * @param nums 元素列表
     */
    public static int @NotNull [] intArray(int... nums) {
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
        list.addAll(asList(elements));
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
    public static <T> List<T> asList(T... elements) {
        if (elements == null) {
            return Collections.emptyList();
        }
        if (elements.length == 1) {
            return Collections.singletonList(elements[0]);
        }
        return java.util.Arrays.asList(elements);
    }
}
