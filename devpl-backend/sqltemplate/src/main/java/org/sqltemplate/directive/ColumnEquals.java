package org.sqltemplate.directive;

import io.devpl.codegen.template.TemplateDirective;

import java.util.Arrays;

public class ColumnEquals implements TemplateDirective {
    @Override
    public String getName() {
        return "eq";
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return new Class[]{String.class, Object.class};
    }

    @Override
    public String render(Object[] params) {

        System.out.println(Arrays.toString(params));

        return "hello";
    }
}
