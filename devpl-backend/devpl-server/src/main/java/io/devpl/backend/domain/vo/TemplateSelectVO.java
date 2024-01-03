package io.devpl.backend.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 模板选择列表VO
 */
@Getter
@Setter
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
