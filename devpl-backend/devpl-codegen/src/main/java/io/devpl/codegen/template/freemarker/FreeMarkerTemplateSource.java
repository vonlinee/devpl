package io.devpl.codegen.template.freemarker;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.devpl.codegen.template.TemplateArguments;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.TemplateSource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

class FreeMarkerTemplateSource implements TemplateSource {

    Template template;

    public FreeMarkerTemplateSource(Template template) {
        this.template = template;
    }

    @Override
    public @NotNull String getName() {
        return template.getName();
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public void setContent(String content) {

    }

    @Override
    public void render(TemplateEngine engine, TemplateArguments arguments, Writer writer) {
        try {
            template.process(arguments.asMap(), writer);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
