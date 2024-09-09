package io.devpl.backend.domain.param;

import io.devpl.backend.entity.FieldGroup;
import lombok.Data;

/**
 * 字段拷贝生成参数
 */
@Data
public class FieldCopyGenParam {

    /**
     * 是否生成方法
     */
    private Boolean generateMethod = true;

    /**
     * 方法名
     */
    private String methodName;

    private String sourceObjectName;
    private String targetObjectName;

    /**
     * 源模型
     */
    private FieldGroup fromGroup;

    /**
     * 目标模型
     */
    private FieldGroup toGroup;
}
