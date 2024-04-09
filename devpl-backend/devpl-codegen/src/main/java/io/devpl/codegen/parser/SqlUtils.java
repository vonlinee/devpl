package io.devpl.codegen.parser;

public class SqlUtils {

    public static String removeBackQuote(String identifier) {
        if (identifier == null) {
            return "";
        }
        return identifier.replace("`", "");
    }
}
