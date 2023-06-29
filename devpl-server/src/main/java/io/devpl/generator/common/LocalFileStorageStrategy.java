package io.devpl.generator.common;

import org.springframework.stereotype.Component;

/**
 * 本地文件存储
 */
@Component
public class LocalFileStorageStrategy implements FileStorageStrategy {

    @Override
    public boolean isPathValid(String path) {
        return false;
    }

    @Override
    public String normalize(String path) {
        return null;
    }

    @Override
    public boolean exists(String path) {
        return false;
    }
}
