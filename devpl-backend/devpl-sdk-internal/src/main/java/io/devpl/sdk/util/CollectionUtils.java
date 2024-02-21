package io.devpl.sdk.util;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 集合操作工具类
 * 简化stream操作
 */
public abstract class CollectionUtils {

    /**
     * Return {@code true} if the supplied Collection is {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param collection the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
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

    public static <E> List<E> sort(List<E> list, Comparator<E> comparator) {
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

    public static <E> int sumInt(Collection<E> list, ToIntFunction<? super E> mapper) {
        return sumInt(list, true, mapper);
    }

    public static <E, T> List<T> toList(Collection<E> list, Function<E, T> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * 先经过一层map，再进行flatMap操作
     *
     * @param collection 愿集合
     * @param mapper     映射逻辑
     * @param <E>        原集合数据类型
     * @param <T>        映射后的集合元素类型
     * @param <C>        映射后的集合类型
     * @return 映射后的list集合
     */
    public static <E, T, C extends Collection<T>> List<T> toFlatList(Collection<E> collection, Function<E, C> mapper) {
        if (isEmpty(collection)) {
            return Collections.emptyList();
        }
        return collection.stream().map(mapper).flatMap(Collection::stream).toList();
    }

    public static <E, T> Set<T> toSet(List<E> list, Function<E, T> mapper) {
        if (isEmpty(list)) {
            return Collections.emptySet();
        }
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

    /**
     * 求和
     *
     * @param collection    集合
     * @param filterNull    是否过滤null元素，即null元素不参与求和
     * @param toIntFunction int function
     * @param <E>           元素类型
     * @return 求和
     */
    public static <E> int sumInt(Collection<E> collection, boolean filterNull, ToIntFunction<? super E> toIntFunction) {
        if (isEmpty(collection)) {
            return 0;
        }
        int sum = 0;
        for (E element : collection) {
            if (filterNull && element == null) {
                continue;
            }
            sum += toIntFunction.applyAsInt(element);
        }
        return sum;
    }

    public static <E> long sumLong(Collection<E> list, ToLongFunction<? super E> toIntFunction) {
        if (isEmpty(list)) {
            return 0;
        }
        long sum = 0;
        for (E element : list) {
            sum += toIntFunction.applyAsLong(element);
        }
        return sum;
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
        coll.addAll(ArrayUtils.asList(arr));
    }

    /**
     * 集合添加另外一个集合的所有元素
     *
     * @param coll              集合
     * @param anotherCollection 新加的元素集合
     * @param <E>               元素类型
     */
    public static <E> void addAll(Collection<E> coll, Collection<? extends E> anotherCollection) {
        if (coll == null || isEmpty(anotherCollection)) {
            return;
        }
        coll.addAll(anotherCollection);
    }

    /**
     * 集合添加多个元素，支持数组和可变参数
     *
     * @param intColl int集合
     * @param ints    新加的元素
     */
    public static void addAll(Collection<Integer> intColl, int[] ints) {
        if (intColl == null || ints == null || ints.length == 0) {
            return;
        }
        addAll(intColl, ArrayUtils.toIntegerArray(ints));
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
    public static <E> int countInt(Collection<E> collection, Predicate<E> condition) {
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
    public static <E> long countLong(Collection<E> collection, Predicate<E> condition) {
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
     * 注意：直接在参数的集合上进行修改元素
     *
     * @param coll1 集合1
     * @param coll2 集合2
     * @param <E>   集合元素类型
     * @return 如果参数为空，则返回原参数
     */
    @Nullable
    public static <C extends Collection<E>, E> C removeAll(C coll1, Collection<E> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2)) {
            return coll1;
        }
        coll1.removeAll(coll2);
        return coll1;
    }

    /**
     * 求差集
     *
     * @param coll1 集合1
     * @param coll2 集合2
     * @param <E>   集合元素类型
     * @return 返回新的list，不影响作为参数的两个集合
     */
    public static <C extends Collection<E>, E> List<E> differ(C coll1, C coll2) {
        if (isEmpty(coll1)) {
            return coll2 == null ? new ArrayList<>() : new ArrayList<>(coll2);
        }
        if (isEmpty(coll2)) {
            return new ArrayList<>(coll1);
        }
        // 创建两个集合
        List<E> list = new ArrayList<>();
        for (E e : coll1) {
            if (!coll2.contains(e)) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * 使用集合覆盖指定的集合元素
     *
     * @param set        待覆盖的集合元素
     * @param collection 集合
     * @param <T>        元素类型
     * @return 覆盖后的集合，最终返回不为空，集合元素以参数collection为准
     */
    public static <S extends Set<T>, T> S setAll(S set, Collection<T> collection, Supplier<S> empty) {
        if (!isEmpty(set)) {
            set.clear();
        } else if (empty != null) {
            set = empty.get();
        }
        set.addAll(collection);
        return set;
    }
}
