package io.devpl.backend.interfaces.impl;

import io.devpl.backend.common.exception.FieldParseException;
import io.devpl.backend.interfaces.FieldParser;
import io.devpl.backend.utils.Utils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 从URL解析字段
 */
public class URLFieldParser implements FieldParser {
    @Override
    public List<Map<String, Object>> parse(String content) throws FieldParseException {
        content = Utils.removeInvisibleCharacters(content);
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(content);
            UriComponents components = builder.build();

            MultiValueMap<String, String> queryParams = components.getQueryParams();

            for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
                FieldInfoMap fieldInfo = new FieldInfoMap();
                fieldInfo.setFieldKey(entry.getKey());
                fieldInfo.setFieldName(entry.getKey());
                fieldInfo.setFieldDescription(entry.getKey());

                if (entry.getValue() == null || entry.getValue().isEmpty() || entry.getValue().size() == 1) {
                    fieldInfo.setFieldDataType("String");
                } else {
                    fieldInfo.setFieldDataType("Collection");
                }
                result.add(fieldInfo.asMap());
            }

        } catch (Exception exception) {
            throw new FieldParseException("不是有效的URL路径");
        }
        return result;
    }
}
