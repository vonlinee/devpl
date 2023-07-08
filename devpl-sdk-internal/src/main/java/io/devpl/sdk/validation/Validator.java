package io.devpl.sdk.validation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * parameter validation all methods throws {@code IllegalArgumentException}
 * 1.when开头的进行校验，并抛出附带信息的异常 2.is开头的均返回boolean:只做判断，不抛异常
 * 3.not开头的除了进行校验之外，如果校验通过则返回被校验对象，校验不通过则抛出附带信息的异常 jsr305.jar
 */
public final class Validator {

    private Validator() {
    }

    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    // 默认提示信息
    private static final String DEFAULT_NOT_NAN_EX_MESSAGE = "The validated value is not a number";
    private static final String DEFAULT_FINITE_EX_MESSAGE = "The value is invalid: %f";
    private static final String DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified exclusive range of %s to %s";
    private static final String DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified inclusive range of %s to %s";
    private static final String DEFAULT_MATCHES_PATTERN_EX = "The string %s does not match the pattern %s";
    private static final String DEFAULT_IS_NULL_EX_MESSAGE = "The validated object is null";
    private static final String DEFAULT_IS_TRUE_EX_MESSAGE = "The validated expression is false";
    private static final String DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE = "The validated array contains null element at index: %d";
    private static final String DEFAULT_NO_NULL_ELEMENTS_COLLECTION_EX_MESSAGE = "The validated collection contains null element at index: %d";
    private static final String DEFAULT_NOT_BLANK_EX_MESSAGE = "The validated character sequence is blank";
    private static final String DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE = "The validated array is empty";
    private static final String DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence is empty";
    private static final String DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE = "The validated collection is empty";
    private static final String DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE = "The validated map is empty";
    private static final String DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE = "The validated array index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE = "The validated collection index is invalid: %d";
    private static final String DEFAULT_VALID_STATE_EX_MESSAGE = "The validated state is false";
    private static final String DEFAULT_IS_ASSIGNABLE_EX_MESSAGE = "Cannot assign a %s to a %s";
    private static final String DEFAULT_IS_INSTANCE_OF_EX_MESSAGE = "Expected type: %s, actual: %s";

    // =============================== WHEN ========================================

    public static void whenTrue(final boolean expression, final String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void whenTrue(final boolean expression, final String message, final long value) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value));
        }
    }

    public static void whenTrue(final boolean expression, final String message, final double value) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value));
        }
    }

    public static void whenTrue(final boolean expression, final String message, final Object... values) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    /**
     * 断言失败，则提供默认值，默认值为空，则报空指针；断言成功，则直接返回该值，即使是NULL
     * @param value        校验的值
     * @param condition    条件，测试对象为value
     * @param defaultValue 默认值，不可为空，确保这个值是预期的值
     * @param <T>          泛型
     * @return <T>
     */
    public static <T> T whenTrue(T value, Predicate<T> condition, T defaultValue) {
        if (condition.test(value)) {
            return value;
        }
        if (defaultValue == null) {
            throw new NullPointerException("defaultValue cannot be null");
        }
        return defaultValue;
    }

    public static <T> void when(T obj, Function<T, Boolean> func, final String message, final Object... values) {
        if (func.apply(obj)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static <T> void when(T obj, Predicate<T> test, final String message, final Object... values) {
        if (test.test(obj)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static void whenTrue(final boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException(DEFAULT_IS_TRUE_EX_MESSAGE);
        }
    }

    public static <T> void whenNull(final T obj) {
        if (obj == null) {
            throw new IllegalArgumentException(DEFAULT_IS_NULL_EX_MESSAGE);
        }
    }

    public static <T> void whenNull(final T obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void whenEmptyOrNull(final T obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
        if ("".equals(obj)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 对Object的任意类型做值检查
     * @param obj     对象
     * @param message 异常信息
     */
    @SuppressWarnings({"unhcecked", "raw"})
    public static void whenEmptyNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
        if (obj instanceof String) {
            if (((String) obj).length() == 0) {
                throw new IllegalArgumentException(message);
            }
        } else if (obj instanceof Map && ((Map<?, ?>) obj).isEmpty()) {
            throw new IllegalArgumentException(message);
        } else if (obj instanceof Collection && ((Collection<?>) obj).isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    // =============================== WHEN ========================================

    // =============================== NOT ========================================
    // not开头的会进行校验，校验通过则原样返回，不通过则抛出相应异常

    /**
     * 不包含null元素
     * @param iterable 可迭代类型
     * @param <T>      <T extends Iterable<?>>
     * @return Iterable
     */
    public static <T extends Iterable<?>> T notNullElements(final T iterable) {
        return noNullElements(iterable, DEFAULT_NO_NULL_ELEMENTS_COLLECTION_EX_MESSAGE);
    }

    public static <T> T[] noNullElements(final T[] array) {
        return notNullElements(array, DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE);
    }

    /**
     * 非空元素
     * 数组没有NULL元素
     * @param array   数组
     * @param message 消息
     * @param values  值
     * @return T[]
     */
    public static <T> T[] notNullElements(final T[] array, final String message, final Object... values) {
        notNull(array);
        for (T t : array) {
            if (t == null) {
                throw new IllegalArgumentException(String.format(message, (Object[]) null));
            }
        }
        return array;
    }

    public static <T extends Iterable<?>> T noNullElements(final T iterable, final String message, final Object... values) {
        notNull(iterable);
        int i = 0;
        for (final Iterator<?> it = iterable.iterator(); it.hasNext(); i++) {
            if (it.next() == null) {
                final Object[] values2 = null;
                System.out.println(i);
                throw new IllegalArgumentException(String.format(message, values2));
            }
        }
        return iterable;
    }

    public static <T> T notNull(final T object) {
        return notNull(object, DEFAULT_IS_NULL_EX_MESSAGE);
    }

    public static <T> T notNull(final T object, final String message, final Object... values) {
        return Objects.requireNonNull(object, () -> String.format(message, values));
    }

    public static <T> T[] notEmpty(final T[] array, final String message, final Object... values) {
        Objects.requireNonNull(array, () -> String.format(message, values));
        if (array.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return array;
    }

    public static <T> T[] notEmpty(final T[] array) {
        return notEmpty(array, DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE);
    }

    public static <T extends Collection<?>> T notEmpty(final T collection, final String message, final Object... values) {
        Objects.requireNonNull(collection, () -> String.format(message, values));
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return collection;
    }

    public static <T extends Collection<?>> T notEmpty(final T collection) {
        return notEmpty(collection, DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE);
    }

    public static <T extends Map<?, ?>> T notEmpty(final T map, final String message, final Object... values) {
        Objects.requireNonNull(map, () -> String.format(message, values));
        if (map.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return map;
    }

    /**
     * @param map
     * @param key
     * @param message void
     */
    public static <T extends Map<K, ?>, K> void notExistKey(final T map, K key, final String message) {
        notNull(map);
        notEmpty(map);
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 不存在关键
     * 不包含指定的key
     * @param map 地图
     * @param key 关键
     */
    public static <T extends Map<K, ?>, K> void notExistKey(final T map, K key) {
        notNull(map);
        notEmpty(map);
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException(String.format("the key:%s does not exist in map", key));
        }
    }

    /**
     * 非空值
     * map has null value for the specificied key
     * @param message void
     * @param map     地图
     * @param key     关键
     */
    public static <T, K> void notNullValue(final Map<K, ?> map, K key, final String message) {
        notNull(map);
        notEmpty(map);
        notExistKey(map, key);
        if (map.get(key) == null) {
            throw new IllegalArgumentException(String.format("the value of key: {} in the map cannot be null", key));
        }
    }

    public static <T extends Map<K, ? extends CharSequence>, K> void notBlankValue(final T map, K key, final String message) {
        notNull(map);
        notEmpty(map);
        notExistKey(map, key);
        notNullValue(map, key, message);
        if (isBlank(map.get(key))) {
            throw new IllegalArgumentException(String.format("the value of key:{} in the map cannot be blank", key));
        }
    }

    public static <T extends CharSequence> T notBlank(final T sequence) {
        if (isBlank(sequence)) {
            throw new IllegalArgumentException(DEFAULT_NOT_BLANK_EX_MESSAGE);
        }
        return sequence;
    }

    public static <T extends Map<?, ?>> T notEmpty(final T map) {
        return notEmpty(map, DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE);
    }

    public static <T extends CharSequence> T notEmpty(final T chars, final String message, final Object... values) {
        Objects.requireNonNull(chars, () -> String.format(message, values));
        if (chars.length() == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return chars;
    }

    public static <T extends CharSequence> T notEmpty(final T chars) {
        return notEmpty(chars, DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE);
    }

    public static <T> T[] validIndex(final T[] array, final int index, final String message, final Object... values) {
        notNull(array);
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return array;
    }

    // ================================ Expression
    // =======================================

    public static boolean isTrue(final boolean expression) {
        return expression;
    }

    /**
     * 获取字符序列的长度，null check
     * @param cs
     * @return int
     */
    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static <T> T[] validIndex(final T[] array, final int index) {
        return validIndex(array, index, DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE, Integer.valueOf(index));
    }

    public static <T extends Collection<?>> T validIndex(final T collection, final int index, final String message, final Object... values) {
        notNull(collection);
        if (index < 0 || index >= collection.size()) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return collection;
    }

    public static <T extends Collection<?>> T validIndex(final T collection, final int index) {
        return validIndex(collection, index, DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE, Integer.valueOf(index));
    }

    public static <T extends CharSequence> T validIndex(final T chars, final int index, final String message, final Object... values) {
        notNull(chars);
        if (index < 0 || index >= chars.length()) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return chars;
    }

    public static <T extends CharSequence> T validIndex(final T chars, final int index) {
        return validIndex(chars, index, DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE, Integer.valueOf(index));
    }

    public static void validState(final boolean expression) {
        if (!expression) {
            throw new IllegalStateException(DEFAULT_VALID_STATE_EX_MESSAGE);
        }
    }

    public static void validState(final boolean expression, final String message, final Object... values) {
        if (!expression) {
            throw new IllegalStateException(String.format(message, values));
        }
    }

    // matchesPattern
    // ---------------------------------------------------------------------------------
    public static void matchesPattern(final CharSequence input, final String pattern) {
        // TODO when breaking BC, consider returning input
        if (!Pattern.matches(pattern, input)) {
            throw new IllegalArgumentException(String.format(DEFAULT_MATCHES_PATTERN_EX, input, pattern));
        }
    }

    public static void matchesPattern(final CharSequence input, final String pattern, final String message, final Object... values) {
        // TODO when breaking BC, consider returning input
        if (!Pattern.matches(pattern, input)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static void notNaN(final double value) {
        notNaN(value, DEFAULT_NOT_NAN_EX_MESSAGE);
    }

    public static void notNaN(final double value, final String message, final Object... values) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    // finite
    // ---------------------------------------------------------------------------------

    public static void finite(final double value) {
        finite(value, DEFAULT_FINITE_EX_MESSAGE, value);
    }

    public static void finite(final double value, final String message, final Object... values) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static <T> void inclusiveBetween(final T start, final T end, final Comparable<T> value) {
        // TODO when breaking BC, consider returning value
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw new IllegalArgumentException(String.format(DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    public static <T> void inclusiveBetween(final T start, final T end, final Comparable<T> value, final String message, final Object... values) {
        // TODO when breaking BC, consider returning value
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    @SuppressWarnings("boxing")
    public static void inclusiveBetween(final long start, final long end, final long value) {
        // TODO when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(String.format(DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    public static void inclusiveBetween(final long start, final long end, final long value, final String message) {
        // TODO when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(message);
        }
    }

    @SuppressWarnings("boxing")
    public static void inclusiveBetween(final double start, final double end, final double value) {
        // TODO when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(String.format(DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    public static void inclusiveBetween(final double start, final double end, final double value, final String message) {
        // TODO when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void exclusiveBetween(final T start, final T end, final Comparable<T> value) {
        // TODO when breaking BC, consider returning value
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(String.format(DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    public static <T> void exclusiveBetween(final T start, final T end, final Comparable<T> value, final String message, final Object... values) {
        // TODO when breaking BC, consider returning value
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    @SuppressWarnings("boxing")
    public static void exclusiveBetween(final long start, final long end, final long value) {
        // TODO when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(String.format(DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    public static void exclusiveBetween(final long start, final long end, final long value, final String message) {
        // TODO when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(message);
        }
    }

    @SuppressWarnings("boxing")
    public static void exclusiveBetween(final double start, final double end, final double value) {
        // TODO when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(String.format(DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    public static void exclusiveBetween(final double start, final double end, final double value, final String message) {
        // TODO when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isInstanceOf(final Class<?> type, final Object obj) {
        // TODO when breaking BC, consider returning obj
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(String.format(DEFAULT_IS_INSTANCE_OF_EX_MESSAGE, type.getName(), obj == null ? "null" : obj
                .getClass()
                .getName()));
        }
    }

    public static void isInstanceOf(final Class<?> type, final Object obj, final String message, final Object... values) {
        // TODO when breaking BC, consider returning obj
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    // isAssignableFrom
    public static void isAssignableFrom(final Class<?> superType, final Class<?> type) {
        // TODO when breaking BC, consider returning type
        if (!superType.isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format(DEFAULT_IS_ASSIGNABLE_EX_MESSAGE, type == null ? "null" : type.getName(), superType.getName()));
        }
    }

    public static void isAssignableFrom(final Class<?> superType, final Class<?> type, final String message, final Object... values) {
        // TODO when breaking BC, consider returning type
        if (!superType.isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static <T> void test(T value, Predicate<T> rule, String message) {
        if (rule.test(value)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void testNegate(T value, Predicate<T> rule, String message) {
        if (rule.negate().test(value)) {
            throw new IllegalArgumentException(message);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void testMultiAnd(T value, String message, Predicate<T>... rules) {
        if (rules.length == 1) {
            test(value, rules[0], message);
        }
        if (rules.length > 1) {
            Predicate<T> rule = rules[0];
            for (int i = 1; i < rules.length; i++) {
                rule = rule.and(rules[i]);
            }
            test(value, rule, message);
        }
    }

    @SafeVarargs
    public static <T> void testMultiOr(T value, String message, Predicate<T>... rules) {
        if (rules.length == 1) {
            test(value, rules[0], message);
        }
        if (rules.length > 1) {
            Predicate<T> rule = rules[0];
            for (int i = 1; i < rules.length; i++) {
                rule = rule.or(rules[i]);
            }
            test(value, rule, message);
        }
    }

    // DateTime Validation
    public static LocalDateTime notValidDatetime(final String dt, String format) {
        try {
            return LocalDateTime.parse(dt, DateTimeFormatter.ofPattern(format));
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("datetime :{} is not suitable for the format:{}", dt, format));
        }
    }

    public static boolean dateTimeInRange(String s1, String e1, String s2, String e2, String format) {
        boolean result = false;
        try {
            LocalDateTime dts1 = LocalDateTime.parse(s1, DEFAULT_DATETIME_FORMATTER);
            LocalDateTime dte1 = LocalDateTime.parse(e1, DEFAULT_DATETIME_FORMATTER);
            LocalDateTime dts2 = LocalDateTime.parse(s1, DEFAULT_DATETIME_FORMATTER);
            LocalDateTime dte2 = LocalDateTime.parse(e2, DEFAULT_DATETIME_FORMATTER);
            // 开始时间大于结束时间
            if (dts1.compareTo(dte2) > 0 || dte1.compareTo(dts2) < 0) {
                result = true;
            }
            return result;
        } catch (Exception ignore) {
            // ignore
        }
        return result;
    }

    // 判断：返回true、false
    public static <T> boolean isNull(T value) {
        return value == null;
    }

    public static <T> boolean isEmpty(Map<?, ?> map) {
        notNull(map);
        return map.isEmpty();
    }

    public static <T> boolean isEmpty(List<?> list) {
        notNull(list);
        return list.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean isEmpty(T... array) {
        notNull(array);
        return array.length == 0;
    }

    public static <T> boolean isEmpty(final CharSequence sequence) {
        notNull(sequence);
        return sequence.length() == 0;
    }

    /**
     * 针对字符串的特别校验
     * @param sequence
     * @return
     */
    public static boolean hasLength(final CharSequence sequence) {
        return (sequence != null && sequence.length() > 0);
    }

    public static boolean hasLength(final Object maybeCharSequence) {
        if (maybeCharSequence == null) return true;
        if (maybeCharSequence instanceof CharSequence) {
            CharSequence sequence = (CharSequence) maybeCharSequence;
            return sequence.length() > 0;
        }
        return false;
    }

    /**
     * 针对Map定制
     * @param map
     * @param key
     * @param message
     * @param <K>
     * @param <V>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, T, V> T validValue(Map<K, V> map, K key, Class<T> valueType, String message) {
        if (map.isEmpty() || !map.containsKey(key)) {
            throw new NullPointerException(String.format("the validated map does not contains the key[%s]", key));
        }
        V v = map.get(key);
        if (v == null) throw new IllegalArgumentException(message);
        // cast
        if (valueType == null) throw new IllegalArgumentException("the target type of value cannot be null");
        if (valueType == String.class) {
            String val = (String) v;
            if (val.length() > 0) return (T) val;
            throw new IllegalArgumentException(message);
        }
        try {
            return (T) v;
        } catch (ClassCastException exception) {
            throw new IllegalArgumentException(String.format("the value[%s] of validated map cannot be cast to the type [%s]", v.getClass(), valueType));
        }
    }

    public static <K, V> V existedNotNullValue(Map<K, V> map, K key, String message) {
        if (map.isEmpty() || !map.containsKey(key)) {
            throw new NullPointerException(String.format("the validated map does not contains the key[%s]", key));
        }
        V v = map.get(key);
        if (v == null)
            throw new IllegalArgumentException(String.format("the validated map contains null value of the key[%s]", key));
        return v;
    }

    /**
     * 非空值
     * @param key       key
     * @param valueType 目标类型
     * @param map       地图
     * @param message   消息
     * @return {@link T}
     */
    @SuppressWarnings("unchecked")
    public static <K, T, V> T notNullValue(Map<K, V> map, K key, Class<T> valueType, String message) {
        if (map.isEmpty() || !map.containsKey(key)) {
            throw new NullPointerException(String.format("the validated map does not contains the key[%s]", key));
        }
        V v = map.get(key);
        if (v == null) throw new IllegalArgumentException(message);
        // cast
        if (valueType == null) throw new IllegalArgumentException("the target type of value cannot be null");
        try {
            return (T) v;
        } catch (ClassCastException exception) {
            throw new IllegalArgumentException(String.format("the value[%s] cannot be cast to the type [%s]", v.getClass(), valueType));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean isNullOrEmpty(T... array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断字符序列是否为空
     * @param cs CharSequence
     * @return boolean
     */
    public static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean lengthBiggerThan(final CharSequence cs, int i) {
        if (cs == null) {
            return false;
        }
        return cs.length() > (i > 0 ? i : 1);
    }

    public static void whenTrue(boolean expression, String format, Object args) {
        if (expression) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    public static void whenFalse(boolean expression, String format, Object args) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }
}
