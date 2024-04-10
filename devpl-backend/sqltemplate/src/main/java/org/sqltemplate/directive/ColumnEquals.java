package org.sqltemplate.directive;

import io.devpl.codegen.template.TemplateDirective;

public class ColumnEquals implements TemplateDirective {
    @Override
    public String getName() {
        return "eq";
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return new Class[0];
    }

    @Override
    public String render(Object[] params) {
        return "hello";
    }
}
