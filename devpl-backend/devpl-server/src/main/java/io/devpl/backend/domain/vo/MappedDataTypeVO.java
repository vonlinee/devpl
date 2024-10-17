package io.devpl.backend.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 映射数据类型VO
 */
@Getter
@Setter
public class MappedDataTypeVO {

    /**
     * 类型映射配置分组ID
     */
    private Long groupId;

    private Long typeId;
    private String typeGroupId;
    private String typeKey;
    private String typeName;
}
