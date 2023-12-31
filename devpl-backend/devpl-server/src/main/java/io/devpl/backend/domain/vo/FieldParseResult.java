package io.devpl.backend.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.List;

@Getter
@Setter
public class FieldParseResult {

    /**
     * 解析错误信息
     */
    private String errorMsg;

    /**
     * 解析字段信息
     */
    private List<Map<String, Object>> fields;
}
