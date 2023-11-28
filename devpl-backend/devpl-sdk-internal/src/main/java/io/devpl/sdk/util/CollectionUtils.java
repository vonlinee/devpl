package io.devpl.sdk.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

public abstract class CollectionUtils {

    /**
     * Return {@code true} if the supplied Collection is {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param collection the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * Return {@code true} if the supplied Collection is {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param map the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    public static <K, E> Map<K, List<E>> groupingBy(List<E> list, Function<? super E, ? extends K> classifier) {
        return list.stream().collect(Collectors.groupingBy(classifier));
    }

    public static <E> List<E> streamSort(List<E> list, Comparator<E> comparator) {
        return list.stream().sorted(comparator).collect(Collectors.toList());
    }

    public static <E, T extends Comparable<T>> List<E> sortBy(List<E> list, Function<E, T> keyExtractor) {
        return sortBy(list, keyExtractor, true);
    }

    public static <E, T extends Comparable<T>> List<E> sortBy(List<E> list, Function<E, T> keyExtractor, boolean asc) {
        return list.stream()
            .sorted(asc ? Comparator.comparing(keyExtractor) : Comparator.comparing(keyExtractor).reversed())
            .collect(Collectors.toList());
    }

    public static <E> List<E> filter(Collection<E> list, Predicate<? super E> filter) {
        return list.stream().filter(filter).collect(Collectors.toList());
    }

    public static <E> int sumInt(List<E> list, ToIntFunction<? super E> mapper) {
        return sumInt(list, true, mapper);
    }

    public static <E, T> List<T> toList(Collection<E> list, Function<E, T> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    public static <E, T> Set<T> toSet(List<E> list, Function<E, T> mapper) {
        return list.stream().map(mapper).collect(Collectors.toSet());
    }

    public static <E, T> Set<T> toSet(List<E> list, Function<E, T> mapper, Predicate<? super T> condition) {
        return list.stream().map(mapper).filter(condition).collect(Collectors.toSet());
    }

    public static <E, K, V> Map<K, V> toMap(List<E> list, Function<? super E, ? extends K> keyMapper, Function<? super E, ? extends V> valueMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public static <E, K> Map<K, E> toMap(List<E> list, Function<? super E, ? extends K> keyMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    public static <E> int sumInt(List<E> list, boolean filterNull, ToIntFunction<? super E> toIntFunction) {
        if (isEmpty(list)) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            E e = list.get(i);
            if (filterNull && e == null) {
                continue;
            }
            sum += toIntFunction.applyAsInt(list.get(i));
        }
        return sum;
    }

    public static <E> long sumLong(List<E> list, ToLongFunction<? super E> toIntFunction) {
        if (isEmpty(list)) {
            return 0;
        }
        long sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += toIntFunction.applyAsLong(list.get(i));
        }
        return sum;
    }

    public static <V, T> List<T> values(Map<?, V> map, Function<V, T> mapper) {
        if (isEmpty(map)) {
            return Collections.emptyList();
        }
        return map.values().stream().map(mapper).collect(Collectors.toList());
    }


    public static <E> E findFirst(List<E> list, E defaults) {
        if (isEmpty(list)) {
            return defaults;
        }
        return list.get(0);
    }

    public static <E, T> T findFirst(List<E> list, Function<E, T> mapper, T defaults) {
        if (isEmpty(list)) {
            return defaults;
        }
        if (mapper != null) {
            T val = mapper.apply(list.get(0));
            return val == null ? defaults : val;
        }
        return defaults;
    }

    public static <E, V> String join(List<E> list, Function<E, V> mapper) {
        return join(list, mapper, String::valueOf);
    }

    public static <E, V> String join(List<E> list, Function<E, V> mapper, Function<V, String> toStringMapper) {
        return join(list, mapper, ",");
    }

    public static <E, V> String join(List<E> list, Function<E, V> mapper, CharSequence delimiter) {
        return join(list, mapper, delimiter, String::valueOf);
    }

    public static <E, V> String join(List<E> list, Function<E, V> mapper, CharSequence delimiter, Function<V, String> toStringMapper) {
        return list.stream().map(mapper).map(toStringMapper).collect(Collectors.joining(delimiter));
    }

    public static <K, E, T> T treeify(Collection<E> collection, TreeBuilder<K, E, T> builder) {
        return builder.apply(collection);
    }
}
