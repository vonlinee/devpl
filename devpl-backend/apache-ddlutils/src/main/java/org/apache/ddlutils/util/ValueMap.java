package org.apache.ddlutils.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * @since 8
 */
@SuppressWarnings("unused")
public class ValueMap implements Map<String, Object>, Cloneable {

    private Map<String, Object> map;

    public ValueMap() {
        this(10);
    }

    public ValueMap(int initialCapacity) {
        this.map = new HashMap<>();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ValueMap cloneMap = (ValueMap) super.clone();
        cloneMap.putAll(this.map);
        return cloneMap;
    }

    public ValueMap copy() {
        try {
            return (ValueMap) clone();
        } catch (CloneNotSupportedException e) {
            return new ValueMap(0);
        }
    }

    public Object set(String key, Object value) {
        return map.put(key, value);
    }

    public Map<String, Object> replace(Map<String, Object> newValue) {
        Map<String, Object> oldMap = this.map;
        this.map = newValue;
        return oldMap;
    }

    public Map<String, Object> asMap() {
        return map;
    }

    // ============================== UNSAFE　getter ==============================

    /**
     * 获取Object属性值
     * @param key 属性名
     * @return 属性值
     */
    public Object getObject(String key) {
        return map.get(key);
    }

    /**
     * 获取Object属性值
     * @param key         属性名
     * @param placeholder 默认值
     * @return 属性值
     */
    public Object getObject(String key, Object placeholder) {
        Object val = map.get(key);
        return val == null ? placeholder : val;
    }

    /**
     * 获取Object属性值
     * @param key         属性名
     * @param placeholder 默认值
     * @param condition   取默认值的条件
     * @return 属性值
     */
    public Object getObject(String key, Object placeholder, Predicate<Object> condition) {
        return getTypedValue(key, placeholder, condition);
    }

    /**
     * 获取字符串型属性值
     * @param key 属性名
     * @return 属性值
     */
    public String getString(String key) throws ClassCastException {
        return (String) map.get(key);
    }

    /**
     * 获取字符串型属性值
     * @param key 属性名
     * @return 属性值
     */
    public String getString(String key, String placeholder) throws ClassCastException {
        Object val = map.get(key);
        return val == null ? placeholder : (String) val;
    }

    /**
     * 获取字符串型属性值
     * @param key 属性名
     * @return 属性值
     */
    public String getString(String key, String placeholder, Predicate<String> condition) throws ClassCastException {
        return getTypedValue(key, placeholder, condition);
    }

    /**
     * 获取int型属性值
     * @param key 属性名
     * @return 属性值
     */
    public Integer getInteger(String key) throws ClassCastException {
        return (Integer) map.get(key);
    }

    /**
     * 获取int型属性值
     * @param key 属性名
     * @return 属性值
     */
    public int getInt(String key, int placeholder) throws ClassCastException {
        Object val = map.get(key);
        return val == null ? placeholder : (int) val;
    }

    /**
     * 获取Integer型属性值
     * @param key 属性名
     * @return 属性值
     */
    public Integer getInteger(String key, Integer placeholder) throws ClassCastException {
        Object val = map.get(key);
        return val == null ? placeholder : (Integer) val;
    }

    /**
     * 获取short型属性值
     * @param key 属性名
     * @return 属性值
     */
    public Short getShort(String key) throws ClassCastException {
        return (Short) map.get(key);
    }

    /**
     * 获取boolean型属性值
     * @param key 属性名
     * @return 属性值
     */
    public Boolean getBoolean(String key) throws ClassCastException {
        return (Boolean) map.get(key);
    }

    /**
     * 获取boolean型属性值
     * @param key 属性名
     * @return 属性值
     */
    public Boolean getBoolean(String key, boolean placeholder) throws ClassCastException {
        Object val = map.get(key);
        return val == null ? placeholder : (Boolean) val;
    }

    /**
     * 获取兼容的boolean型属性值，比如 如果是字符串，那么自动转换 "true" -> true
     * @param key 属性名
     * @return 属性值
     */
    public Boolean getCompatiableBoolean(String key) throws ClassCastException {
        Object val = map.get(key);
        if (val instanceof String) {
            try {
                return Boolean.parseBoolean((String) val);
            } catch (Exception exception) {
                return null;
            }
        }
        return (Boolean) val;
    }

    /**
     * 获取long型属性值
     * @param key 属性名
     * @return 属性值
     */
    public Long getLong(String key) throws ClassCastException {
        return (Long) map.get(key);
    }

    /**
     * 获取char型属性值
     * @param key 属性名
     * @return 属性值
     */
    public Character getCharacter(String key) throws ClassCastException {
        return (Character) map.get(key);
    }

    /**
     * 获取float型属性值<br>
     * @param key 属性名
     * @return 属性值
     */
    public Float getFloat(String key) throws ClassCastException {
        return (Float) map.get(key);
    }

    /**
     * 获取double型属性值
     * @param key 属性名
     * @return 属性值
     */
    public Double getDouble(String key) throws ClassCastException {
        return (Double) map.get(key);
    }

    /**
     * 获取byte型属性值
     * @param key 属性名
     * @return 属性值
     */
    public Byte getByte(String key) throws ClassCastException {
        return (Byte) map.get(key);
    }

    /**
     * 获取BigDecimal型属性值
     * @param key 属性名
     * @return 属性值
     */
    public BigDecimal getBigDecimal(String key) throws ClassCastException {
        return (BigDecimal) map.get(key);
    }

    /**
     * 获取Number型属性值
     * @param key 属性名
     * @return 属性值
     */
    public Number getNumber(String key) throws ClassCastException {
        return (Number) map.get(key);
    }

    /**
     * 获取BigInteger型属性值
     * @param key 属性名
     * @return 属性值
     */
    public BigInteger getBigInteger(String key) throws ClassCastException {
        return (BigInteger) map.get(key);
    }

    @SuppressWarnings("unchecked")
    public <V> V getTypedValue(String key, V placeholder, Predicate<V> condition) throws ClassCastException {
        Object val = map.get(key);
        if (condition != null && condition.test((V) val)) {
            return placeholder;
        }
        return (V) val;
    }

    @Override
    public String toString() {
        return String.valueOf(map);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    /**
     * 指定的key是否有相等的值
     * @param key   key
     * @param value 比较的值
     * @return 是否有相等的值
     */
    public boolean containsValue(String key, Object value) {
        return Objects.equals(map.get(key), value);
    }

    /**
     * 指定的key是否有相等的值
     * @param key   key
     * @param value 比较的值
     * @return 是否有相等的值
     */
    public <T> boolean containsValue(String key, T value, BiPredicate<Object, T> condition) {
        Object val = map.get(key);
        return condition.test(val, value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        if (m != null) {
            map.putAll(m);
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }
}
