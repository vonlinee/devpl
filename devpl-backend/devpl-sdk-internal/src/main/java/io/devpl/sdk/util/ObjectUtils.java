package io.devpl.sdk.util;

public class ObjectUtils {

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

    /**
     * 忽略对象重写的toString方法
     *
     * @param obj 对象
     * @return 对象.toString方法
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        return obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode());
    }
}
