package org.apache.ddlutils.util;

public class NumberUtils {

    public static int parseInt(String s) {
        return parseInt(s, Integer.MIN_VALUE);
    }

    public static int parseInt(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (Exception exception) {
            return defaultValue;
        }
    }
}
