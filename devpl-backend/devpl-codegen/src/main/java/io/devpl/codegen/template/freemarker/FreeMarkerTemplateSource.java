package io.devpl.codegen.template.freemarker;

import freemarker.template.Template;
import io.devpl.codegen.template.TemplateSource;

class FreeMarkerTemplateSource implements TemplateSource {

    Template template;

    public FreeMarkerTemplateSource(Template template) {
        this.template = template;
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public void setContent(String content) {

    }
}
