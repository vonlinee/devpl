package io.devpl.codegen.template;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

/**
 * 文件模板
 */
public class FileTemplateSource implements TemplateSource {

    File file;

    public FileTemplateSource(File file) {
        this.file = file;
    }

    @Override
    public @NotNull String getName() {
        return file.getAbsolutePath();
    }

    @Override
    public String getContent() {
        try {
            return Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setContent(String template) {
        try {
            Files.writeString(file.toPath(), template);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(TemplateEngine engine, TemplateArguments arguments, Writer writer) {
        throw new UnsupportedOperationException("file template is not supported yet");
    }
}
