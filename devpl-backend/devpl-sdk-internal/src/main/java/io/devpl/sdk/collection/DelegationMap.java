package io.devpl.sdk.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface DelegationMap<K, V> extends Map<K, V> {

    /**
     * 不能为空
     * @return 返回一个Map类型
     */
    Map<K, V> delegator();

    @Override
    default int size() {
        return delegator().size();
    }

    @Override
    default boolean isEmpty() {
        return delegator().isEmpty();
    }

    @Override
    default boolean containsKey(Object key) {
        return delegator().containsKey(key);
    }

    @Override
    default boolean containsValue(Object value) {
        return delegator().containsValue(value);
    }

    @Override
    default V get(Object key) {
        return delegator().get(key);
    }

    @Override
    default V put(K key, V value) {
        return delegator().put(key, value);
    }

    @Override
    V remove(Object key);

    @Override
    void putAll(Map<? extends K, ? extends V> m);

    @Override
    void clear();

    @Override
    Set<K> keySet();

    @Override
    Collection<V> values();

    @Override
    default Set<Entry<K, V>> entrySet() {
        return delegator().entrySet();
    }

    @Override
    default V getOrDefault(Object key, V defaultValue) {
        return Map.super.getOrDefault(key, defaultValue);
    }

    @Override
    default void forEach(BiConsumer<? super K, ? super V> action) {
        Map.super.forEach(action);
    }

    @Override
    default void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Map.super.replaceAll(function);
    }

    @Override
    default V putIfAbsent(K key, V value) {
        return Map.super.putIfAbsent(key, value);
    }

    @Override
    default boolean remove(Object key, Object value) {
        return Map.super.remove(key, value);
    }

    @Override
    default boolean replace(K key, V oldValue, V newValue) {
        return Map.super.replace(key, oldValue, newValue);
    }

    @Override
    default V replace(K key, V value) {
        return Map.super.replace(key, value);
    }

    @Override
    default V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return Map.super.computeIfAbsent(key, mappingFunction);
    }

    @Override
    default V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return Map.super.computeIfPresent(key, remappingFunction);
    }

    @Override
    default V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return Map.super.compute(key, remappingFunction);
    }

    @Override
    default V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return Map.super.merge(key, value, remappingFunction);
    }
}
