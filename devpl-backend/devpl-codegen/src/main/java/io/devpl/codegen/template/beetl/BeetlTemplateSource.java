package io.devpl.codegen.template.beetl;

import io.devpl.codegen.template.TemplateSource;
import org.beetl.core.Template;
import org.jetbrains.annotations.NotNull;

class BeetlTemplateSource implements TemplateSource {

    Template template;

    public BeetlTemplateSource(Template template) {
        this.template = template;
    }

    @Override
    public @NotNull String getName() {
        return template.toString();
    }
}
