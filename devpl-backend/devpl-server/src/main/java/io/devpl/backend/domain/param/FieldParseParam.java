package io.devpl.backend.domain.param;

import lombok.Data;

/**
 * 字段解析参数
 */
@Data
public class FieldParseParam {

    /**
     * 输入类型 JSON/SQL/
     */
    private String type;

    /**
     * 待解析的文本
     */
    private String content;
}