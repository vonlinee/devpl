package io.devpl.generator.domain.vo;

import lombok.Data;

/**
 * 数据类型映射VO
 */
@Data
public class DataTypeMappingListVO {

    private Long typeId;
    private Long typeGroupId;
    private String typeName;

    private Long anotherTypeId;
    private Long anotherGroupId;
    private String anotherTypeName;
}
