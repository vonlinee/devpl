package io.devpl.backend.mock;

import io.devpl.backend.mock.generator.StringMockValueGenerator;
import org.junit.Test;

public class TestMockUtils {

    @Test
    public void test1() {

        MockValueGenerator generator = new StringMockValueGenerator();

        String value = generator.getValue(new MockContextImpl());

        System.out.println(value);


    }
}
