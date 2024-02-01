package io.devpl.backend.extension.template;

import io.devpl.codegen.template.TemplateDirective;

public class CustomTemplateDirective implements TemplateDirective {

    /**
     * 指令名称，该指令在模板中的名称
     *
     * @return 指令名称
     */
    @Override
    public String getName() {
        return "指令名称";
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return new Class[0];
    }

    @Override
    public String render(Object[] params) {
        return "渲染结果";
    }
}
