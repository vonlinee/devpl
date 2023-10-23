package io.devpl.generator.domain.vo;

import lombok.Data;

/**
 * 数据类型映射VO
 */
@Data
public class DataTypeMappingListVO {

    private Long typeId;
    private String typeGroupId;
    private String typeName;

    private Long anotherTypeId;
    private String anotherGroupId;
    private String anotherTypeName;
}
