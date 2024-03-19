package io.devpl.backend.domain.param;

import io.devpl.backend.common.query.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 生成表列表查询参数
 */
@Setter
@Getter
public class GenTableListParam extends PageParam {

    /**
     * 表名模糊匹配
     */
    private String tableName;
}
