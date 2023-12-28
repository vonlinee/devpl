package io.devpl.codegen.template.enjoy;

import com.jfinal.template.Template;
import io.devpl.codegen.template.TemplateSource;

class EnjoyTemplateSource implements TemplateSource {

    Template template;

    public EnjoyTemplateSource(Template template) {
        this.template = template;
    }
}
