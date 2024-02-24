package io.devpl.backend.domain.param;

import lombok.Data;

import java.util.List;

/**
 * 文件生成参数
 */
@Data
public class FileGenerationParam {

    /**
     * 数据库表ID
     */
    private List<Long> tableIds;
}
