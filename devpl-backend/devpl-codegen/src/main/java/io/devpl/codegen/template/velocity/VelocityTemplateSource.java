package io.devpl.codegen.template.velocity;

import io.devpl.codegen.template.TemplateArguments;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.TemplateSource;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.jetbrains.annotations.NotNull;

import java.io.Writer;

/**
 * 针对Velocity模板的包装
 *
 * @see org.apache.velocity.Template
 */
class VelocityTemplateSource implements TemplateSource {

    @NotNull
    Template template;

    public VelocityTemplateSource(@NotNull Template template) {
        this.template = template;
    }

    @Override
    public @NotNull String getName() {
        return template.getName();
    }

    @Override
    public String getContent() {
        return template.getName();
    }

    @Override
    public void setContent(String content) {
    }

    @Override
    public void render(TemplateEngine engine, TemplateArguments arguments, Writer writer) {
        template.merge(new VelocityContext(arguments.asMap()), writer);
    }
}
