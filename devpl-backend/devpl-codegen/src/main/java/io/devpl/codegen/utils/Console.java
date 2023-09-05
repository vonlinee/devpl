package io.devpl.codegen.utils;

import java.util.Arrays;

public class Console {

    public static void println(Object... args) {
        String result = Arrays.toString(args);
        System.out.println(result.substring(1, result.length() - 1));
    }
}
