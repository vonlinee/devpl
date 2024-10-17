package io.devpl.backend.domain.vo;

import io.devpl.backend.entity.DataTypeItem;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

/**
 * 数据类型映射VO
 */
@Getter
@Setter
public class DataTypeMappingByTypeGroup {

    /**
     * 配置ID
     */
    private Long groupId;

    /**
     * 主类型分组
     */
    private String typeGroupId;

    /**
     * 映射类型分组
     */
    private String anotherTypeGroupId;

    /**
     * 主类型列表
     */
    private Collection<DataTypeItem> types;

    /**
     * 映射类型列表，和主类型列表索引一一对应
     */
    private List<Collection<DataTypeItem>> mappedDataTypes;
}
