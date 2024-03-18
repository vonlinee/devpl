package io.devpl.codegen.template.freemarker;

import io.devpl.codegen.template.Template;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.TemplateException;
import org.jetbrains.annotations.NotNull;

import java.io.Writer;

/**
 * FreeMarker 模板
 */
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
    public void render(TemplateEngine engine, Object arguments, Writer writer) throws TemplateException {
        try {
            template.process(arguments, writer);
        } catch (Exception e) {
            throw new TemplateException(e);
        }
    }
}
