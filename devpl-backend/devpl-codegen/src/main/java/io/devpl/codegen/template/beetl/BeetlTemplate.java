package io.devpl.codegen.template.beetl;

import io.devpl.codegen.template.Template;
import io.devpl.codegen.template.TemplateEngine;
import org.jetbrains.annotations.NotNull;

import java.io.Writer;

class BeetlTemplate implements Template {

    org.beetl.core.Template template;

    public BeetlTemplate(org.beetl.core.Template template) {
        this.template = template;
    }

    @Override
    public @NotNull String getName() {
        return template.toString();
    }

    @Override
    public void render(TemplateEngine engine, Object arguments, Writer writer) {
        template.renderTo(writer);
    }
}
