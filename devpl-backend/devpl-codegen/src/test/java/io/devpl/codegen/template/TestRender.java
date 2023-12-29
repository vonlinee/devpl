package io.devpl.codegen.template;

import io.devpl.codegen.template.velocity.VelocityTemplateEngine;
import org.junit.Before;
import org.junit.Test;

public class TestRender {

    VelocityTemplateEngine engine;

    @Before
    public void createTemplateEngine() {
        engine = new VelocityTemplateEngine();
    }

    @Test
    public void test1() {
        TemplateArgumentsMap argumentsMap = new TemplateArgumentsMap();
        argumentsMap.setValue("name", "zs");
        String result = engine.evaluate("Hello, ${name}", argumentsMap);
        System.out.println(result);
    }

    @Test
    public void test2() {

    }
}
