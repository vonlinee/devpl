package io.devpl.codegen.template.enjoy;

import com.jfinal.template.Template;
import io.devpl.codegen.template.TemplateSource;
import org.jetbrains.annotations.NotNull;

class EnjoyTemplateSource implements TemplateSource {

    Template template;

    public EnjoyTemplateSource(Template template) {
        this.template = template;
    }

    @Override
    public @NotNull String getName() {
        return template.toString();
    }
}
