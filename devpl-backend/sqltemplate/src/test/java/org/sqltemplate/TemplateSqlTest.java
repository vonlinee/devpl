package org.sqltemplate;

import io.devpl.codegen.template.Template;
import io.devpl.codegen.template.TemplateArgumentsMap;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.velocity.VelocityTemplateEngine;
import org.junit.jupiter.api.Test;
import org.sqltemplate.directive.ColumnEquals;
import org.sqltemplate.directive.Where;

import java.io.StringWriter;

public class TemplateSqlTest {

    /**
     * @see io.devpl.codegen.template.velocity.CamelCaseDirective
     */
    @Test
    public void test1() {
        TemplateEngine templateEngine = new VelocityTemplateEngine();

        templateEngine.registerDirective(new ColumnEquals());
        templateEngine.registerDirective(new Where());

        TemplateArgumentsMap argumentsMap = new TemplateArgumentsMap();
        argumentsMap.setValue("name", "zs");

        Template template = templateEngine.getTemplate("""
            #msg('a_resource','TODAY', #date(1234567890900,'date')))!
            """, true);

        StringWriter sw = new StringWriter();
        template.render(templateEngine, argumentsMap, sw);
        System.out.println(sw);
    }
}
