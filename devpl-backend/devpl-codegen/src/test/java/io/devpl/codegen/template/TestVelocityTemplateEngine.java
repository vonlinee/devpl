package io.devpl.codegen.template;

import io.devpl.codegen.template.velocity.VelocityTemplateEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.junit.Before;
import org.junit.Test;

import java.io.Writer;

public class TestVelocityTemplateEngine {

    VelocityTemplateEngine engine;

    @Before
    public void createTemplateEngine() {
        engine = new VelocityTemplateEngine();
    }

    /**
     * @see SimpleNode#render(InternalContextAdapter, Writer)
     */
    @Test
    public void test1() {
        TemplateArgumentsMap argumentsMap = new TemplateArgumentsMap();
        argumentsMap.setValue("name", "table_info");

        String template = "Hello, #toCamelCase(${name})";

        Template ts = engine.getTemplate(template, true);

        String result = engine.render(ts, argumentsMap);
        System.out.println(result);
    }

    /**
     * @see org.apache.velocity.runtime.RuntimeInstance#evaluate(Context, Writer, String, String)
     * @see SimpleNode#render(InternalContextAdapter, Writer)
     * @see org.apache.velocity.runtime.parser.node.ASTDirective#render(InternalContextAdapter, Writer)
     */
    @Test
    public void test2() {
        TemplateArgumentsMap argumentsMap = new TemplateArgumentsMap();
        argumentsMap.setValue("name", "table_info");

        String template = "Hello, #toCamelCase(${name})";

        Template ts = engine.getTemplate(template, false);

        String result = engine.render(ts, argumentsMap);
        System.out.println(result);
    }
}
