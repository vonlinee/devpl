package io.devpl.codegen.strategy;

import io.devpl.codegen.config.ConfigurationHolder;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 适用于Maven的项目结构
 */
public class MavenProjectFileLocator extends ConfigurationHolder implements ProjectFileLocator {

    private String rootDir;
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
    public void addContext(String name, Object value) {
        super.setValue(name, value);
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
