package io.devpl.backend.domain.vo;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 文件生成结果
 */
@Data
public class FileGenerationResult {

    /**
     * 所有生成的根目录
     */
    private Set<String> rootDirs = new HashSet<>(1);

    public FileGenerationResult() {
    }

    public FileGenerationResult(Set<String> rootDirs) {
        this.rootDirs = rootDirs;
    }

    public void addRootDir(String rootDir) {
        if (rootDirs == null) {
            rootDirs = new HashSet<>();
        }
        rootDirs.add(rootDir);
    }
}
