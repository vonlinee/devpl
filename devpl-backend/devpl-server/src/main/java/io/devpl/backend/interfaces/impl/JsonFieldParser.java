package io.devpl.backend.interfaces.impl;

import de.marhali.json5.*;
import io.devpl.backend.common.exception.FieldParseException;
import io.devpl.backend.interfaces.FieldParser;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class JsonFieldParser implements FieldParser {

    /**
     * 仅支持单层JSON对象
     *
     * @param content JSON字符串，支持JSON5格式
     * @return 字段列表
     */
    @Override
    public List<Map<String, Object>> parse(String content) throws FieldParseException {
        try {
            List<Map<String, Object>> res = new ArrayList<>();

            return res;
        } catch (Exception exception) {
            log.error("[字段解析 JSON] 解析失败", exception);
        }
        return Collections.emptyList();
    }
}
