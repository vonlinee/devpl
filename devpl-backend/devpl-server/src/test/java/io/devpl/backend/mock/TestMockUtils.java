package io.devpl.backend.mock;

import io.devpl.backend.mock.generator.StringMockValueGenerator;

public class TestMockUtils {

    public void test1() {

        MockValueGenerator generator = new StringMockValueGenerator();

        String value = generator.getValue(new MockContextImpl());

        System.out.println(value);
    }
}
