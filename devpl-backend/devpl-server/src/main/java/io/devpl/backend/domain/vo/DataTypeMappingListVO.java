package io.devpl.backend.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据类型映射VO
 */
@Getter
@Setter
public class DataTypeMappingListVO {

    private Long typeId;
    private String typeGroupId;
    private String typeName;

    private Long anotherTypeId;
    private String anotherGroupId;
    private String anotherTypeName;
}
