package io.devpl.codegen.template;

import io.devpl.codegen.lang.LanguageMode;
import org.junit.Test;

public class TestLanguageMode {

    @Test
    public void test1() {
        System.out.println(LanguageMode.getGetterMethodName("name", false));
        System.out.println(LanguageMode.getGetterMethodName("sex", true));
        System.out.println(LanguageMode.getSetterMethodName("name"));
        System.out.println(LanguageMode.getSetterMethodName("age"));
    }
}
