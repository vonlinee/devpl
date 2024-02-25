package io.devpl.sdk;

import io.devpl.sdk.util.StringUtils;
import org.junit.Test;

public class TestStringUtils {

    @Test
    public void test1() {

        System.out.println(StringUtils.isBlank("   s    "));

        StringBuilder sb = new StringBuilder();

        sb.append("Hello World");

        String result = StringUtils.lastSubstring(sb, 0);

        System.out.println(result);

        DataObject obj = DataClass.newObject();

        obj.set("name", "zs");
        obj.set("age", 12);
    }

}
