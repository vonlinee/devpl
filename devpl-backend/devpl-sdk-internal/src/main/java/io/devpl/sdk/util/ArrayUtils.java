package io.devpl.sdk.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 数组工具类
 */
public abstract class ArrayUtils {

    public static int[] intArray(int... nums) {
        if (nums == null) return new int[0];
        return nums;
    }

    @SafeVarargs
    public static <T> List<T> asList(T... elements) {
        if (elements == null) {
            return new ArrayList<>(0);
        }
        return java.util.Arrays.asList(elements);
    }
}
