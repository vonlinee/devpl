package io.devpl.fxui.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * 分组
     * @param collection 集合
     * @param keyMapper  key映射
     * @param <K>        key
     * @param <V>        value
     * @param <C>        集合类型
     * @return 无论什么集合，都会分组为List,因为list可包含重复
     */
    public static <K, V, C extends Collection<V>> Map<K, List<V>> groupingBy(C collection, Function<? super V, ? extends K> keyMapper) {
        return collection.stream().collect(Collectors.groupingBy(keyMapper));
    }

    /**
     * List -> Map
     * @param list        List
     * @param keyMapper   将list元素映射为key
     * @param valueMapper 将list元素映射为value
     * @param <E>
     * @param <K>
     * @param <V>
     * @return
     */
    public static <E, K, V> Map<K, V> toMap(List<E> list, Function<E, K> keyMapper, Function<E, V> valueMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }
}
