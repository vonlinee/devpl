package io.devpl.backend.utils;

public class Utils {

    public static boolean isTrue(Object val) {
        if (val == null) {
            return false;
        }
        if (val instanceof Boolean b) {
            return b;
        }
        if (val instanceof Number n) {
            return n.intValue() > 0;
        }
        if (val instanceof String s) {
            return "true".equalsIgnoreCase(s);
        }
        return false;
    }

    public static boolean isFalse(Object val) {
        if (val == null) {
            return false;
        }
        if (val instanceof Boolean b) {
            return !b;
        }
        if (val instanceof Number n) {
            return n.intValue() < 0;
        }
        if (val instanceof String s) {
            return "false".equalsIgnoreCase(s);
        }
        return false;
    }
}
