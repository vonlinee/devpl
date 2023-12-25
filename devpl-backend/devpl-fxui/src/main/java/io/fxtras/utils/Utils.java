package io.fxtras.utils;

public class Utils {

    /**
     * 调用Object的toString方法，不管子类是否重写toString
     *
     * @param obj
     * @return
     */
    public static String objectToString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode());
    }

    public static <T> T whenNull(T val, T defaultValue) {
        if (val == null) {
            return defaultValue;
        }
        return val;
    }
}
