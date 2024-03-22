package io.devpl.backend.domain.param;

import io.devpl.backend.entity.DataTypeItem;
import lombok.Data;

import java.util.List;

/**
 * 数据类型映射添加参数
 */
@Data
public class DataTypeMappingAddParam {

    /**
     * 类型映射规则分组
     */
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
}
