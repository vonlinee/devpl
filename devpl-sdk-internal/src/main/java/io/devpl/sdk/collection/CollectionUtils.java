package io.devpl.sdk.collection;

import java.util.*;

public abstract class CollectionUtils {

    public static <K> boolean existKey(K key, Map<K, ?> map) {
        return map.containsKey(key);
    }

    public static <K, V> HashMap<K, V> newHashMap(int intialCapacity) {
        if (intialCapacity < 0) {
        	throw new IllegalArgumentException("intialCapacity of HashMap cannot be less than zero!");
        }
        return new HashMap<>(intialCapacity);
    }

    public static <T> List<T> emptyArrayList() {
        return new ArrayList<T>(0);
    }

    public static <T> List<T> newArrayList(int intialCapacity) {
        return new ArrayList<T>(intialCapacity);
    }

	/**
	 * Return {@code true} if the supplied Collection is {@code null} or empty.
	 * Otherwise, return {@code false}.
	 * @param collection the Collection to check
	 * @return whether the given Collection is empty
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return isEmpty(map);
	}
	
	public static boolean isEmpty(List<?> list) {
		return isEmpty(list);
	}
}
