package io.devpl.codegen.config;

import io.devpl.codegen.core.TableGeneration;
import io.devpl.codegen.core.TargetFile;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 表生成目标文件
 */
@Getter
@Setter
public abstract class TableGenerationTargetFile implements TargetFile {

    private String filename;

    protected abstract void initialize(TableGeneration table);

    @Override
    public void write(OutputStream stream) throws IOException {

    }

    @Override
    public String getFilename() {
        return null;
    }

    @Override
    public String getExtension() {
        return null;
    }
}
