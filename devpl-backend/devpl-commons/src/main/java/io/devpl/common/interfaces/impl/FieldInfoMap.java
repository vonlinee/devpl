package io.devpl.common.interfaces.impl;

import io.devpl.common.interfaces.FieldParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字段信息Map
 */
public class FieldInfoMap {

    private final Map<String, Object> map;

    public FieldInfoMap() {
        this(new HashMap<>());
    }

    public FieldInfoMap(Map<String, Object> map) {
        this.map = map;
    }

    public void setFieldName(String fieldName) {
        map.put(FieldParser.FIELD_NAME, fieldName);
    }

    public String getFieldName() {
        return (String) map.get(FieldParser.FIELD_NAME);
    }

    public void setFieldKey(String fieldKey) {
        map.put(FieldParser.FIELD_KEY, fieldKey);
    }

    public String getFieldKey() {
        return (String) map.get(FieldParser.FIELD_KEY);
    }

    public void setFieldDescription(String description) {
        map.put(FieldParser.FIELD_DESCRIPTION, description);
    }

    public String getFieldDescription() {
        return (String) map.get(FieldParser.FIELD_DESCRIPTION);
    }

    public void setFieldDataType(String dataType) {
        map.put(FieldParser.FIELD_TYPE, dataType);
    }

    public String getFieldDataType() {
        return (String) map.get(FieldParser.FIELD_TYPE);
    }

    public Map<String, Object> asMap() {
        return map;
    }

    public static List<FieldInfoMap> wrap(List<Map<String, Object>> list) {
        return list.stream().map(FieldInfoMap::new).toList();
    }
}
