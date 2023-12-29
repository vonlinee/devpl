package io.devpl.codegen.template;

import io.devpl.codegen.template.velocity.VelocityTemplateEngine;

public class TestRender {

    public static void main(String[] args) {

        VelocityTemplateEngine engine = new VelocityTemplateEngine();
        TemplateArgumentsMap argumentsMap = new TemplateArgumentsMap();
        argumentsMap.setValue("name", "zs");
        String result = engine.evaluate("Hello, ${name}", argumentsMap);

        System.out.println(result);
    }
}
