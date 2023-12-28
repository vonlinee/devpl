package io.devpl.codegen.template;

import io.devpl.codegen.template.velocity.VelocityTemplateEngine;

public class TestRender {

    public static void main(String[] args) {

        TemplateEngine engine = new VelocityTemplateEngine();

        TemplateSource templateSource = engine.getTemplate("""
            ##logical NOT
            #if( !$foo )
              <strong>NOT that</strong>
            #end
            """);


        TemplateArgumentsMap argumentsMap = new TemplateArgumentsMap();
        argumentsMap.setValue("foo", "bar");

        templateSource.render(engine, argumentsMap, System.out);

    }
}
