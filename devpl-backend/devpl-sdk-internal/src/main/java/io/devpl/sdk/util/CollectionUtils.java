package io.devpl.sdk.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
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
     * 所有都为空时返回true
     *
     * @param collections 多个集合
     * @return 所有都为空时返回true
     */
    public static boolean isEmpty(Collection<?>... collections) {
        if (collections == null) {
            return true;
        }
        if (collections.length == 1) {
            return isEmpty(collections[0]);
        }
        for (Collection<?> collection : collections) {
            if (!isEmpty(collection)) {
                return false;
            }
        }
        return true;
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

    /**
     * 分组
     *
     * @param list       列表
     * @param classifier 分组的key
     * @param <K>        分组Key类型
     * @param <E>        列表元素
     * @return 分组Map
     */
    public static <K, E> Map<K, List<E>> groupingBy(Collection<E> list, Function<? super E, ? extends K> classifier) {
        return list.stream().collect(Collectors.groupingBy(classifier));
    }

    public static <E> List<E> streamSort(List<E> list, Comparator<E> comparator) {
        return list.stream().sorted(comparator).collect(Collectors.toList());
    }

    public static <E, T extends Comparable<T>> List<E> sortBy(List<E> list, Function<E, T> keyExtractor) {
        return sortBy(list, keyExtractor, true);
    }

    public static <E, T extends Comparable<T>> List<E> sortBy(List<E> list, Function<E, T> keyExtractor, boolean asc) {
        return list.stream().sorted(asc ? Comparator.comparing(keyExtractor) : Comparator.comparing(keyExtractor).reversed()).collect(Collectors.toList());
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


    public static <E, T, U extends Comparable<? super U>> T min(Collection<E> collection, Function<E, T> key, Function<? super T, ? extends U> keyExtractor) {
        if (collection == null) {
            return null;
        }
        return collection.stream().map(key).min(Comparator.comparing(keyExtractor)).orElse(null);
    }

    public static <E, T, U extends Comparable<? super U>> T min(Collection<E> collection, Function<E, T> key, Function<? super T, ? extends U> keyExtractor, T defaults) {
        if (collection == null) {
            return defaults;
        }
        return collection.stream().map(key).min(Comparator.comparing(keyExtractor)).orElse(defaults);
    }

    public static <E, T, U extends Comparable<? super U>> T max(Collection<E> collection, Function<E, T> key, Function<? super T, ? extends U> keyExtractor) {
        if (collection == null) {
            return null;
        }
        return collection.stream().map(key).max(Comparator.comparing(keyExtractor)).orElse(null);
    }

    public static <E, T, U extends Comparable<? super U>> T max(Collection<E> collection, Function<E, T> key, Function<? super T, ? extends U> keyExtractor, T defaults) {
        if (collection == null) {
            return defaults;
        }
        return collection.stream().map(key).max(Comparator.comparing(keyExtractor)).orElse(defaults);
    }

    /**
     * 集合添加多个元素，支持数组和可变参数
     *
     * @param coll 集合
     * @param arr  新加的元素
     * @param <E>  元素类型
     */
    public static <E> void addAll(Collection<E> coll, E[] arr) {
        if (coll == null || arr == null || arr.length == 0) {
            return;
        }
        coll.addAll(Arrays.asList(arr));
    }

    public static void addAll(Collection<Integer> intColl, int[] ints) {
        if (intColl == null || ints == null || ints.length == 0) {
            return;
        }
        for (int i = 0; i < ints.length; i++) {
            intColl.add(ints[i]);
        }
    }

    public static <E, T> long count(Collection<E> collection, Function<E, T> key, Predicate<T> condition) {
        return collection.stream().map(key).filter(condition).count();
    }

    /**
     * 按条件统计某个字段
     *
     * @param collection 集合
     * @param condition  条件
     * @param <E>        集合元素类型
     * @return 统计数量，返回Int
     */
    public static <E> int counti(Collection<E> collection, Predicate<E> condition) {
        if (isEmpty(collection)) {
            return 0;
        }
        return (int) collection.stream().filter(condition).count();
    }

    /**
     * 按条件统计某个字段
     *
     * @param collection 集合
     * @param condition  条件
     * @param <E>        集合元素类型
     * @return 统计数量，返回long
     */
    public static <E> long countl(Collection<E> collection, Predicate<E> condition) {
        if (isEmpty(collection)) {
            return 0;
        }
        return collection.stream().filter(condition).count();
    }

    /**
     * 将集合平铺
     *
     * @param collection 集合，元素也为集合
     * @param collector  Collector
     * @param <E>        集合元素类型
     * @param <R>        平铺后的集合类型
     * @return 平铺后的集合
     */
    public static <E, R extends Collection<E>> R flatten(Collection<? extends Collection<E>> collection, Collector<E, ?, R> collector) {
        return collection.stream().flatMap(Collection::stream).collect(collector);
    }

    public static <E> boolean anyMatch(List<E> patrolTeams, Predicate<E> condition) {
        if (isEmpty(patrolTeams)) {
            return false;
        }
        for (E team : patrolTeams) {
            if (condition.test(team)) {
                return true;
            }
        }
        return false;
    }

    /**
     * nullsafe版本removeAll
     *
     * @param coll1 集合1
     * @param coll2 集合2
     * @param <E>   集合元素类型
     */
    public static <E> void removeAll(Collection<E> coll1, List<E> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2)) {
            return;
        }
        coll1.removeAll(coll2);
    }
}
