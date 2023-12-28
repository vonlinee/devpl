package io.devpl.codegen.template.velocity;

import io.devpl.codegen.template.TemplateArguments;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.TemplateSource;
import org.apache.velocity.Template;

import java.io.OutputStream;

/**
 * @see org.apache.velocity.Template
 */
public class VelocityTemplateSource implements TemplateSource {

    Template template;

    public VelocityTemplateSource(Template template) {
        this.template = template;
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public void setContent(String content) {

    }

    @Override
    public void render(TemplateEngine engine, TemplateArguments arguments, OutputStream output) {
        engine.render(this, arguments, output);
    }
}
