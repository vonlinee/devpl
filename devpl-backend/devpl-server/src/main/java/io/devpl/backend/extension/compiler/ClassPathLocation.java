package io.devpl.backend.extension.compiler;

import javax.tools.JavaFileManager;

/**
 * 类路径
 */
public class ClassPathLocation implements JavaFileManager.Location {

    private final String path;

    public ClassPathLocation(String path) {
        this.path = path;
    }

    @Override
    public String getName() {
        return path;
    }

    @Override
    public boolean isOutputLocation() {
        return false;
    }
}
