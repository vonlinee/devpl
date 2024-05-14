package io.devpl.codegen.utils;

import io.devpl.codegen.util.JavaBeansUtils;
import org.junit.Test;

public class JavaBeanUtilsTest {

    @Test
    public void test1() {

        String setterMethodName = JavaBeansUtils.getSetterMethodName("name");

        System.out.println(setterMethodName);
    }
}
