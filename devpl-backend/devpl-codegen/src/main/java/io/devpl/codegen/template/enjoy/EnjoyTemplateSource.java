package io.devpl.codegen.template.enjoy;

import com.jfinal.template.Template;
import io.devpl.codegen.template.TemplateArguments;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.TemplateSource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

class EnjoyTemplateSource implements TemplateSource {

    Template template;

    public EnjoyTemplateSource(Template template) {
        this.template = template;
    }

    @Override
    public @NotNull String getName() {
        return template.toString();
    }

    @Override
    public void render(TemplateEngine engine, TemplateArguments arguments, Writer writer) {
        String result = template.renderToString(arguments.asMap());
        try {
            writer.append(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
