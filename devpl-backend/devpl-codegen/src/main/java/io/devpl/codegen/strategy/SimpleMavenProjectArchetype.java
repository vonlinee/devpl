package io.devpl.codegen.strategy;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 适用于Maven的项目结构
 */
public class SimpleMavenProjectArchetype implements ProjectArchetype {

    /**
     * 项目根目录
     */
    private String rootDir;

    /**
     * 模块名称
     */
    private String moduleName;

    @Override
    public void setRootDirectory(String rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    public void setModuleName(String module) {
        this.moduleName = module;
    }

    @Override
    public String locate(String filename, String ext) {
        Path path;
        if ("java".equals(ext)) {
            path = Paths.get(rootDir, moduleName, "src/main/java", filename + ext);
        } else if ("xml".equals(ext)) {
            path = Paths.get(rootDir, moduleName, "src/main/resources", filename + ext);
        } else {
            path = Paths.get(rootDir, moduleName);
        }
        return path.toAbsolutePath().toString();
    }
}
