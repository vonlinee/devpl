package io.devpl.codegen.utils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 集合工具类
 * @see org.springframework.util.CollectionUtils
 */
public class CollectionUtils {

    /**
     * 用来过渡下Jdk1.8下ConcurrentHashMap的性能bug
     * <a href="https://bugs.openjdk.java.net/browse/JDK-8161372">...</a>
     * @param concurrentHashMap ConcurrentHashMap 没限制类型了，非ConcurrentHashMap就别调用这方法了
     * @param key               key
     * @param mappingFunction   function
     * @param <K>               k
     * @param <V>               v
     * @return V
     * @since 3.4.0
     */
    public static <K, V> V computeIfAbsent(ConcurrentHashMap<K, V> concurrentHashMap, K key, Function<? super K, ? extends V> mappingFunction) {
        V v = concurrentHashMap.get(key);
        if (v != null) {
            return v;
        }
        return concurrentHashMap.computeIfAbsent(key, mappingFunction);
    }

    /**
     * Map集合是否为空
     * @param map Map
     * @return 集合是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 单列集合是否为空
     * @param collection Collection
     * @return 集合是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
