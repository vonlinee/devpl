package io.devpl.generator.domain.vo;

import lombok.Data;

/**
 * 文件生成类型 下拉列表
 */
@Data
public class GenFileSelectVO {

    private String typeName;
    private Integer templateId;
    private String templateName;
}
