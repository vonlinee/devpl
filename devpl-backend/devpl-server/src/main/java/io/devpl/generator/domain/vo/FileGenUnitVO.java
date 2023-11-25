package io.devpl.generator.domain.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileGenUnitVO {

    private Long id;
    private Long parentId;

    private String itemName;

    private Long templateId;
    private String templateName;

    /**
     * 数据填充策略 1-数据库表 2-自定义、
     */
    private String fillStrategy;

    /**
     * 数据填充策略名称
     */
    private String fillStrategyName;
}
