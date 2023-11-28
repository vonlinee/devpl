package io.devpl.backend.mock;

import lombok.Builder;

import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 将各种对象转成字符串
 */
public final class ToString {

    /**
     * 配置选项
     */
    @Builder
    public static class Option {

        private String start;
        private String end;
        private String separator;
        private boolean skipNull;

    }

    public static <T> String toString(T[] array, Option option) {
        StringJoiner res = new StringJoiner(option.separator, option.start, option.end);
        for (T t : array) {
            if (option.skipNull && t == null) {
                continue;
            }
            res.add(String.valueOf(t));
        }
        return res.toString();
    }

    public static <T> String toString(T obj, Option option) {
        if (obj.getClass().isArray()) {
            return toString((Object[]) obj, option);
        }
        return null;
    }

    public static <T> String toString(Iterator<T> iterator, Option option) {
        return null;
    }

    public static <K, V> String toString(Map<K, V> map, Option option) {
        return null;
    }
}
