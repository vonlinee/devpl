package io.devpl.sdk.collection;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

/**
 * 专门用作参数封装
 */
public final class ParamMap extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public String getString(String paramName) {
        return (String) get(paramName);
    }

    public int getInt(String paramName) {
        return (int) get(paramName);
    }

    public Number getNumber(String paramName) {
        return (int) get(paramName);
    }

    public long getLong(String paramName) {
        return (long) get(paramName);
    }

    public double getDouble(String paramName) {
        return (double) get(paramName);
    }

    public Date getDate(String paramName) {
        return (Date) get(paramName);
    }

    public LocalDateTime getLocalDateTime(String paramName) {
        return (LocalDateTime) get(paramName);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String paramName, Class<T> typeClass) {
        Object value = get(paramName);
        if (value == null)
            return null;
        if (value.getClass() == typeClass) {
            return (T) value;
        }
        throw new ClassCastException(String.format("value[%s] cannot be casted to type[%s]", value.getClass().getName(),
            typeClass.getName()));
    }
}
