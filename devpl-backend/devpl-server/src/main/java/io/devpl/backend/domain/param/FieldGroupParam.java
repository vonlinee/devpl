package io.devpl.backend.domain.param;

import io.devpl.backend.entity.FieldGroup;
import io.devpl.backend.entity.FieldInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FieldGroupParam {

    /**
     * 组信息
     */
    private FieldGroup group;

    /**
     * 字段信息
     */
    private List<FieldInfo> fields;

    /**
     * 批量添加参数
     */
    private List<FieldGroupParam> groups;
}
