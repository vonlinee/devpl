package io.devpl.backend.common;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * 处理JSON类型
 */
@Component
public class JsonTypeHandler extends AbstractJsonTypeHandler<Map<String, Object>> {

    @Resource
    ObjectMapper objectMapper;

    @Override
    @SuppressWarnings("unchecked")
    protected Map<String, Object> parse(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            return Collections.emptyMap();
        }
    }

    @Override
    protected String toJson(Map<String, Object> obj) {
        try {
            if (obj == null) {
                return "{}";
            }
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
