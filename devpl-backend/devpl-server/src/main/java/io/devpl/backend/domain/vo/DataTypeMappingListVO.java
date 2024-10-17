package io.devpl.backend.domain.vo;

import io.devpl.backend.entity.DataTypeItem;
import io.devpl.sdk.annotations.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @NotNull
    private List<DataTypeItem> types;

    @NotNull
    private List<List<DataTypeItem>> mappedDataTypes;
}
