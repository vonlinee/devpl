package io.devpl.generator.domain.param;

import lombok.Data;

/**
 * 字段解析参数
 */
@Data
public class FieldParseParam {

    /**
     * 输入类型
     */
    private String type;

    /**
     * 待解析的文本
     */
    private String content;
}
