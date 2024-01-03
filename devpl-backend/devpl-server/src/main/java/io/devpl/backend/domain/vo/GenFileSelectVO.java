package io.devpl.backend.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 文件生成类型 下拉列表
 */
@Getter
@Setter
public class GenFileSelectVO {

    private String typeName;
    private Integer templateId;
    private String templateName;
}
