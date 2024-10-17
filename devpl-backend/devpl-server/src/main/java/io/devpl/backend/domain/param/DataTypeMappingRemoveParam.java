package io.devpl.backend.domain.param;

import io.devpl.backend.entity.DataTypeItem;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 数据类型映射添加参数
 */
@Data
public class DataTypeMappingRemoveParam {

    /**
     * 类型映射规则分组
     */
    @NotEmpty(message = "类型映射规则分组不能为空")
    private Long groupId;

    /**
     * 主数据类型
     *
     * @see DataTypeItem#getId()
     */
    private Long typeId;

    /**
     * 映射数据类型
     *
     * @see DataTypeItem#getId()
     */
    private List<Long> anotherTypeIds;

    /**
     * 主数据类型分组
     *
     * @see DataTypeItem#getTypeGroupId()
     */
    private String typeGroupId;

    /**
     * 映射数据类型分组
     *
     * @see DataTypeItem#getTypeGroupId()
     */
    private String anotherTypeGroupId;
}
