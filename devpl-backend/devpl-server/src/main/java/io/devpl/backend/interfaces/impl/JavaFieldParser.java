package io.devpl.backend.interfaces.impl;

import io.devpl.backend.common.exception.FieldParseException;
import io.devpl.backend.interfaces.FieldParser;
import io.devpl.codegen.parser.java.JavaASTUtils;
import io.devpl.codegen.parser.java.MetaField;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 支持单文件多个类型定义
 */
@Slf4j
public class JavaFieldParser implements FieldParser {
    @Override
    public List<Map<String, Object>> parse(String content) throws FieldParseException {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            List<MetaField> metaFields = JavaASTUtils.parseFields(content);
            for (MetaField metaField : metaFields) {
                FieldInfoMap fieldInfo = new FieldInfoMap();
                fieldInfo.setFieldKey(metaField.getIdentifier());
                fieldInfo.setFieldName(metaField.getName());
                fieldInfo.setFieldDescription(metaField.getDescription());
                fieldInfo.setFieldDataType(metaField.getDataType());
                result.add(fieldInfo.asMap());
            }
        } catch (IOException e) {
            log.error("[字段解析 JAVA] 解析失败", e);
        }
        return result;
    }
}
