package io.devpl.common.interfaces;

import io.devpl.common.exception.FieldParseException;

import java.util.List;
import java.util.Map;

/**
 * 从文本解析字段信息
 */
public interface FieldParser {

    String FIELD_KEY = "key";
    String FIELD_NAME = "name";
    String FIELD_TYPE = "type";
    String FIELD_DESCRIPTION = "description";

    /**
     * 从文本解析字段信息
     *
     * @param content 文本内容，任意内容，可以是SQL
     * @return 字段列表，每个Map代表一个字段信息，包括3个字段：name, type, description
     */
    List<Map<String, Object>> parse(String content) throws FieldParseException;

    /**
     * 默认空实现
     */
    FieldParser EMPTY = content -> List.of();
}