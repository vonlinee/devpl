package io.devpl.backend.domain.param;

import io.devpl.backend.common.query.PageParam;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Setter
@Getter
public class DataTypeListParam extends PageParam {

    private Long typeId;

    /**
     * 类型映射分组 ID
     */
    private Long groupId;

    /**
     * 排除的类型ID
     */
    private Long excludeTypeId;

    private String typeKey;

    private String typeName;

    private String typeGroupId;

    private String typeKeyPattern;

    private String typeNamePattern;

    /**
     * 排除的类型分组 后端填充
     */
    private String excludeTypeGroupId;

    /**
     * 需要排除的类型ID列表 后端填充
     */
    private Collection<Long> excludeIds;
}
