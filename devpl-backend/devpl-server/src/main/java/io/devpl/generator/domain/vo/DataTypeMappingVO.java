package io.devpl.generator.domain.vo;

import lombok.Data;

/**
 * 数据类型映射VO
 */
@Data
public class DataTypeMappingVO {

    private Long typeId;
    private Long typeGroupId;
    private String typeKey;
    private String typeName;
}
