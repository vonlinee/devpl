package org.apache.ddlutils.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @since 8
 */
public class Arguments {

    private final Map<String, Object> map = new HashMap<>();

    public Object set(String key, Object value) {
        return map.put(key, value);
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
}
