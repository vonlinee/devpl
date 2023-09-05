package io.devpl.sdk.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 对java.util.Arrays做一些封装
 */
public final class ArrayUtils {

    private ArrayUtils() {
    }

    public static int[] of(int... nums) {
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
