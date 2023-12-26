package io.devpl.backend.interfaces.impl;

import io.devpl.backend.interfaces.FieldParser;

import java.util.HashMap;
import java.util.Map;

/**
 * 字段信息Map
 */
class FieldInfoMap extends HashMap<String, Object> {

    public void setFieldName(String fieldName) {
        put(FieldParser.FIELD_NAME, fieldName);
    }

    public void setFieldKey(String fieldKey) {
        put(FieldParser.FIELD_KEY, fieldKey);
    }

    public void setFieldDescription(String description) {
        put(FieldParser.FIELD_DESCRIPTION, description);
    }

    public void setFieldDataType(String dataType) {
        put(FieldParser.FIELD_TYPE, dataType);
    }

    public Map<String, Object> asMap() {
        return this;
    }
}
