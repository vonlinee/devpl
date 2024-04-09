package io.devpl.codegen.template.rocker;

import io.devpl.codegen.template.Template;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.TemplateException;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.io.Writer;

/**
 * <a href="https://github.com/fizzed/rocker/tree/master">...</a>
 * 使用模板编译提升性能
 */
public class RockerTemplateEngine implements TemplateEngine {

    @Override
    public void evaluate(String template, Object arguments, Writer writer) {

    }

    @Override
    public void render(Template template, Object arguments, OutputStream outputStream) throws TemplateException {

    }

    @Override
    public @NotNull String getTemplateFileExtension() {
        return null;
    }
}
