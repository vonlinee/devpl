package io.devpl.sdk;

import io.devpl.sdk.util.StringUtils;

public class TestStringUtils {

    public static void main(String[] args) {

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
