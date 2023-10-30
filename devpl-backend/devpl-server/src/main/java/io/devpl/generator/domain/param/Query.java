package io.devpl.generator.domain.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 查询公共参数
 */
@Data
public class Query {
    private String code;
    private String tableName;
    private String attrType;
    private String columnType;
    private String connName;
    private String dbType;
    private String projectName;

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    private Integer page;

    @NotNull(message = "每页条数不能为空")
    @Range(min = 1, max = 1000, message = "每页条数，取值范围 1-1000")
    private Integer limit;
}
