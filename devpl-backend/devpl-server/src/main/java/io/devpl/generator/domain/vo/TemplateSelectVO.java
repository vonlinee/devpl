package io.devpl.generator.domain.vo;

import lombok.Data;

/**
 * 模板选择列表VO
 */
@Data
public class TemplateSelectVO {

    /**
     * 模板ID
     */
    private Integer templateId;

    /**
     * 模板名称
     */
    private String templateName;
}
