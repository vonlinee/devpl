package io.devpl.generator.utils;

import com.baomidou.mybatisplus.generator.util.StringUtils;

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

    public static boolean isAnyNull(Object... args) {
        if (args == null) {
            return true;
        }
        for (Object arg : args) {
            if (arg == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 所有值都不为null
     *
     * @param args
     * @return
     */
    public static boolean allNotNull(Object... args) {
        if (args == null) {
            return false;
        }
        for (Object arg : args) {
            if (arg == null) {
                return false;
            }
        }
        return true;
    }

    public static <T> T whenNull(T value, T placeholder) {
        return value == null ? placeholder : value;
    }

    public static String whenBlank(String text, String placeholder) {
        return StringUtils.hasText(text) ? text : placeholder;
    }
}
