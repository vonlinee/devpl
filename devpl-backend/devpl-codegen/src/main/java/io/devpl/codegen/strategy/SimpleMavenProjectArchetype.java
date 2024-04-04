package io.devpl.codegen.strategy;

import io.devpl.codegen.generator.GeneratedFile;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 适用于Maven的项目结构
 */
public class SimpleMavenProjectArchetype extends ProjectArchetype {

    @Override
    public String locate(GeneratedFile file) {
        String ext = file.getExtension();
        String filename = file.getFilename();
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
