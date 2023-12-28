package io.devpl.codegen.template.beetl;

import io.devpl.codegen.template.TemplateSource;
import org.beetl.core.Template;

class BeetlTemplateSource implements TemplateSource {

    Template template;

    public BeetlTemplateSource(Template template) {
        this.template = template;
    }
}
