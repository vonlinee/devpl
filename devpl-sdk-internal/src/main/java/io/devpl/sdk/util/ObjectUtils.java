package io.devpl.sdk.util;

public class ObjectUtils {

    // TODO 待实现
    public static boolean isEmpty(Object array) {
        return false;
    }

    public static <K> boolean nullSafeEquals(K key, Object key1) {
        return false;
    }

    public static <K> int nullSafeHashCode(K key) {
        return key.hashCode();
    }

    public static String nullSafeToString(Object o) {
        return String.valueOf(o);
    }
}
