package io.devpl.backend.domain.param;

import io.devpl.backend.common.query.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataTypeMappingListParam extends PageParam {

    /**
     * 类型映射分组ID
     */
    private Long groupId;

    /**
     * 类型ID
     */
    private Long typeId;

    /**
     * 主类型key
     */
    private String typeKeyPattern;

    /**
     * 类型分组ID
     */
    private String typeGroupId;

    /**
     * 映射的类型ID
     */
    private Long anotherTypeId;
}
