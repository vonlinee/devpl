package com.baomidou.mybatisplus.generator.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionUtils {

    /**
     * 转换为Map
     * @param list        原列表
     * @param keyMapper   key映射
     * @param valueMapper value映射
     * @param <K>         key
     * @param <V>         value
     * @param <E>         列表元素
     * @return Map
     */
    public static <K, V, E> Map<K, V> toMap(Collection<E> list, Function<E, K> keyMapper, Function<E, V> valueMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }
}
