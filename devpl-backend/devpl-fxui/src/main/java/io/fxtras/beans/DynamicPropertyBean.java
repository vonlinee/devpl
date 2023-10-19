package io.fxtras.beans;

import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.Map;

public class DynamicPropertyBean {

    final Map<String, Object> rawMap = new HashMap<>();
    final ObservableMap<String, Object> observableMap = FXCollections.observableMap(rawMap);
    // 用于限制字段类型
    final Map<String, Class<?>> fieldTypes = new HashMap<>();
    // 单向绑定的属性集合，注意这里只是存引用，Property对象是共用的
    final Map<String, Property<?>> bindedProperties = new HashMap<>();

    public DynamicPropertyBean() {
        observableMap.addListener((MapChangeListener<String, Object>) change -> {
            final Object newValue = change.getValueAdded();
            final String key = change.getKey();
            @SuppressWarnings("unchecked") Property<Object> property = (Property<Object>) bindedProperties.get(key);
            if (property != null) {
                final Object value = property.getValue();
                if (value != null) {
                    if (!value.getClass()
                            .isAssignableFrom(newValue.getClass())) {
                        throw new RuntimeException(String.format("filed type mismatch: [%s]\nthe newvalue [%s %s] cannot be updated to value of binded property [%s]", key, newValue, newValue.getClass(), value.getClass()));
                    } else {
                        property.setValue(change.getValueAdded());
                    }
                }
            }
        });
    }

    public final <T> void bind(String filed, Property<T> property) {
        bindedProperties.put(filed, property);
    }

    public final <T> void bindBidirectional(String filed, Property<T> other) {
        bindedProperties.put(filed, other);
        other.addListener((observable, oldValue, newValue) -> {
            rawMap.put(filed, newValue); // 防止递归回调
        });
    }

    public void put(String key, Object value) {
        if (value != null) {
            fieldTypes.put(key, value.getClass());
        }
        if (bindedProperties.containsKey(key)) {
            observableMap.put(key, value);
        } else {
            rawMap.put(key, value);
        }
    }

    public boolean hasBindings(String key) {
        return bindedProperties.containsKey(key);
    }

    public Object get(String key) {
        return rawMap.get(key);
    }
}
