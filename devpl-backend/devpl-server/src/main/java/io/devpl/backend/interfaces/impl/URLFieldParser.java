package io.devpl.backend.interfaces.impl;

import io.devpl.backend.interfaces.FieldParser;
import io.devpl.codegen.parser.java.MetaField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 从URL解析字段
 */
public class URLFieldParser implements FieldParser {
    @Override
    public List<Map<String, Object>> parse(String content) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (MetaField metaField : List.<MetaField>of()) {
            FieldInfoMap fieldInfo = new FieldInfoMap();
            fieldInfo.setFieldKey(metaField.getIdentifier());
            fieldInfo.setFieldName(metaField.getName());
            fieldInfo.setFieldDescription(metaField.getDescription());
            fieldInfo.setFieldDataType(metaField.getDataType());
            result.add(fieldInfo.asMap());
        }

        return result;
    }
}
