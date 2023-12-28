package io.devpl.codegen.template.freemarker;

import freemarker.template.Template;
import io.devpl.codegen.template.TemplateSource;
import org.jetbrains.annotations.NotNull;

class FreeMarkerTemplateSource implements TemplateSource {

    Template template;

    public FreeMarkerTemplateSource(Template template) {
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
}
