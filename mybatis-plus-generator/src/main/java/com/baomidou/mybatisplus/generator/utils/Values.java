package com.baomidou.mybatisplus.generator.utils;

public class Values {

    private Values() {
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
