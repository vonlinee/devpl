package io.devpl.backend.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 文件生成结果
 */
@Data
public class FileGenerationResult {

    /**
     * 所有生成的根目录
     */
    private List<String> rootDirs;
}
