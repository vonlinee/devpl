package io.devpl.backend.domain.param;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * 模型转DDL参数
 */
@Setter
@Getter
public class Model2DDLParam {

    /**
     * 文本，实体类java源码
     */
    private String content;

    /**
     * 去除的字段,使用逗号分割
     */
    private String excludeFields;

    /**
     * 数据库类型
     */
    @NotEmpty(message = "数据库类型不能为空")
    private String targetDbType;
}
