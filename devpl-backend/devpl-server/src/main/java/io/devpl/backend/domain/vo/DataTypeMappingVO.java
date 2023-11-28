package io.devpl.backend.domain.vo;

import lombok.Data;

/**
 * 数据类型映射VO
 */
@Data
public class DataTypeMappingVO {

    private Long typeId;
    private Long groupId;
    private String typeGroupId;
    private String typeKey;
    private String typeName;

    private boolean mapped;
}
