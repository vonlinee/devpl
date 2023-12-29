package io.devpl.codegen.template.beetl;

import io.devpl.codegen.template.TemplateArguments;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.TemplateSource;
import org.beetl.core.Template;
import org.jetbrains.annotations.NotNull;

import java.io.Writer;

class BeetlTemplateSource implements TemplateSource {

    Template template;

    public BeetlTemplateSource(Template template) {
        this.template = template;
    }

    @Override
    public @NotNull String getName() {
        return template.toString();
    }

    @Override
    public void render(TemplateEngine engine, TemplateArguments arguments, Writer writer) {
        template.renderTo(writer);
    }
}
