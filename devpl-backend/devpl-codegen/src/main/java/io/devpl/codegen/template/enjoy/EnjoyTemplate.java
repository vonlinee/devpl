package io.devpl.codegen.template.enjoy;

import io.devpl.codegen.template.Template;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.sdk.util.BeanUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

class EnjoyTemplate implements Template {

    com.jfinal.template.Template template;

    public EnjoyTemplate(com.jfinal.template.Template template) {
        this.template = template;
    }

    @Override
    public @NotNull String getName() {
        return template.toString();
    }

    @Override
    public void render(TemplateEngine engine, Object arguments, Writer writer) {
        String result = template.renderToString(BeanUtils.toMap(arguments));
        try {
            writer.append(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
