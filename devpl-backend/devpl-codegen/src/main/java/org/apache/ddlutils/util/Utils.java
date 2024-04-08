package org.apache.ddlutils.util;

/**
 * 内部使用
 * 参数检查
 */
public class Utils {

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className) throws ReflectiveOperationException {
        Class<?> clazz = Class.forName(className);
        return (T) newInstance(clazz);
    }

    public static <T> T newInstance(Class<T> clazz) throws ReflectiveOperationException {
        if (clazz == null) {
            return null;
        }
        return clazz.getDeclaredConstructor().newInstance();
    }

    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> T[] withDefaults(T[] arr, T[] defaults) {
        return isEmpty(arr) ? defaults : arr;
    }

    public static <T> T withDefaults(T obj, T defaults) {
        return obj == null ? defaults : obj;
    }
}
