package io.devpl.generator.utils;

/**
 * 简化一些代码
 */
public abstract class Vals {

    public static int nil(Integer val, int defaultValue) {
        return val == null ? defaultValue : val;
    }

    public static boolean nil(Boolean val, int defaultValue) {
        return val == null ? defaultValue > 0 : val;
    }

    public static boolean nil(Boolean val, String defaultValue) {
        return val == null ? "true".equalsIgnoreCase(defaultValue) : val;
    }

    public static boolean nil(Boolean val, boolean defaultValue) {
        return val == null ? defaultValue : val;
    }
}
