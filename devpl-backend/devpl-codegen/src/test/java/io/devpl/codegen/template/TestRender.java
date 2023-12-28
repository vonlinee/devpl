package io.devpl.codegen.template;

import io.devpl.codegen.template.velocity.VelocityTemplateEngine;

public class TestRender {

    public static void main(String[] args) {

        VelocityTemplateEngine engine = new VelocityTemplateEngine();

        TemplateSource templateSource = engine.getTemplate("Hello, ${foo}", false);

        TemplateArgumentsMap argumentsMap = new TemplateArgumentsMap();
        argumentsMap.setValue("foo", "xxxxxxxxxxx");

        String result = engine.render(templateSource, argumentsMap);

        System.out.println(result);
    }
}
