package io.devpl.backend.domain.param;

import lombok.Getter;
import lombok.Setter;

/**
 * 模型转DDL参数
 */
@Setter
@Getter
public class Model2DDLParam {

    /**
     * 文本
     */
    private String content;

    /**
     * 去除的字段
     */
    private String excludeFields;
}
