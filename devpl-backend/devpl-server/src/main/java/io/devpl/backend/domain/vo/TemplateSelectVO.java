package io.devpl.backend.domain.vo;

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

    /**
     * 备注信息
     */
    private String remark;
}
