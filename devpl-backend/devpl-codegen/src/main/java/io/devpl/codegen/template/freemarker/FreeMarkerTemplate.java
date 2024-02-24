package io.devpl.codegen.template.freemarker;

import freemarker.template.TemplateException;
import io.devpl.codegen.template.Template;
import io.devpl.codegen.template.TemplateArguments;
import io.devpl.codegen.template.TemplateEngine;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

class FreeMarkerTemplate implements Template {

    freemarker.template.Template template;

    public FreeMarkerTemplate(freemarker.template.Template template) {
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
    public void render(TemplateEngine engine, Object arguments, Writer writer) {
        try {
            template.process(arguments, writer);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
