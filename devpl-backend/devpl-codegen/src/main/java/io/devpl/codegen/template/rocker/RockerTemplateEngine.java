package io.devpl.codegen.template.rocker;

import io.devpl.codegen.template.TemplateArguments;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.TemplateException;
import io.devpl.codegen.template.TemplateSource;

import java.io.OutputStream;
import java.io.Writer;

/**
 * <a href="https://github.com/fizzed/rocker/tree/master">...</a>
 * 使用模板编译提升性能
 */
public class RockerTemplateEngine implements TemplateEngine {

    @Override
    public void evaluate(String template, TemplateArguments arguments, Writer writer) {

    }

    @Override
    public void render(TemplateSource templateSource, TemplateArguments arguments, OutputStream outputStream) throws TemplateException {

    }

    @Override
    public String getTemplateFileExtension() {
        return null;
    }
}
