package io.devpl.backend.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据类型映射VO
 */
@Getter
@Setter
public class DataTypeMappingListVO {

    private Long id;

    private Long groupId;

    // 主类型
    private Long typeId;
    private String typeGroupId;
    private String typeName;
    private String typeKey;

    // 映射数据类型
    private Long anotherTypeId;
    private String anotherTypeKey;
    private String anotherTypeGroupId;
    private String anotherTypeName;
}
