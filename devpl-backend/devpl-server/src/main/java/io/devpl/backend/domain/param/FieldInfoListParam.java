package io.devpl.backend.domain.param;

import io.devpl.backend.common.query.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldInfoListParam extends PageParam {

    /**
     * 根据fieldKey过滤，多个逗号拼接
     */
    private String excludedKeys;

    /**
     * 通过字段key，字段名称或者字段描述信息查找
     */
    private String keyword;

    /**
     * 数据类型ID
     */
    private String dataType;
}
