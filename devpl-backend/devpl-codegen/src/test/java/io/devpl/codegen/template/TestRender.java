package io.devpl.codegen.template;

import io.devpl.codegen.lang.LanguageMode;
import io.devpl.codegen.template.model.FieldData;
import io.devpl.codegen.template.model.MethodData;
import io.devpl.codegen.template.model.TypeData;
import io.devpl.codegen.template.velocity.VelocityTemplateEngine;
import io.devpl.sdk.util.Arrays;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

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
        TypeData model = new TypeData();

        model.setPackageName("io.devpl.main");
        model.addImport("java.util.List");
        model.addImport("java.util.Map");

        model.setSuperClass(Serializable.class.getName());
        model.setSuperClass(HashMap.class.getName());
        model.addSuperInterfaces(Serializable.class.getName());
        model.setClassName("Main");

        // 字段信息
        FieldData nameField = new FieldData();
        nameField.setName("name");
        nameField.setDataType("String");
        nameField.setModifier(LanguageMode.JAVA.getModifierName(Modifier.PRIVATE));

        FieldData ageField = new FieldData();
        ageField.setName("age");
        ageField.setDataType("String");
        ageField.setModifier(LanguageMode.JAVA.getModifierName(Modifier.PRIVATE));

        model.setFields(Arrays.asArrayList(nameField, ageField));

        model.addField(nameField);

        // 方法
        MethodData setNameMethod = new MethodData();
        setNameMethod.setName("setName");

        Map<String, Object> args = new HashMap<>();

        model.fill(args);

        engine.render("newtemplates/java.pojo.vm", args, System.out);
    }
}
