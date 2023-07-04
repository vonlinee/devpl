package io.devpl.generator.common;

import io.devpl.sdk.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 本地文件存储
 */
@Component
public class LocalFileStorageStrategy implements FileStorageStrategy {

    @Override
    public String getPathSeparator() {
        return File.separator;
    }

    @Override
    public boolean isPathValid(String path) {
        return true;
    }

    @Override
    public String normalize(String path) {
        return null;
    }

    @Override
    public boolean exists(String path) {
        return false;
    }

    @Override
    public void write(String path, InputStream inputStream) {
        Path filePath = Path.of(path);
        if (!Files.exists(filePath.getParent())) {
            try {
                Files.createDirectories(filePath.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (OutputStream os = Files.newOutputStream(filePath)) {
            IOUtils.copy(inputStream, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
