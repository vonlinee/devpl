package io.devpl.codegen.template.velocity;

import io.devpl.codegen.template.TemplateSource;
import org.apache.velocity.Template;
import org.jetbrains.annotations.NotNull;

/**
 * 针对Velocity模板的包装
 *
 * @see org.apache.velocity.Template
 */
public class VelocityTemplateSource implements TemplateSource {

    Template template;

    public VelocityTemplateSource(Template template) {
        this.template = template;
    }

    @Override
    public @NotNull String getName() {
        return template.getName();
    }

    @Override
    public String getContent() {
        return template == null ? "" : template.getName();
    }

    @Override
    public void setContent(String content) {

    }
}
