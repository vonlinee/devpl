package io.devpl.codegen.template;

import io.devpl.codegen.generator.config.CaseFormat;
import io.devpl.codegen.jdbc.meta.TypeInfoMetadata;

import java.lang.reflect.Field;

public class TestOther {

    public static void test1() {
        StringBuilder sb = new StringBuilder();
        sb.append("try {\n");
        int i = 1;
        for (Field declaredField : TypeInfoMetadata.class.getDeclaredFields()) {
            String method = "getString";
            if (declaredField.getType() == Boolean.class || declaredField.getType() == boolean.class) {
                method = "getBoolean";
            } else if (declaredField.getType() == short.class || declaredField.getType() == Short.class) {
                method = "getShort";
            } else if (declaredField.getType() == long.class || declaredField.getType() == Long.class) {
                method = "getLong";
            } else if (declaredField.getType() == int.class || declaredField.getType() == Integer.class) {
                method = "getInt";
            }
            sb.append("\tthis.").append(declaredField.getName()).append(" = resultSet.").append(method).append("(").append(i++).append(");\n");
        }
        sb.append("} catch (SQLException exception) {\n").append("\tthrow new RuntimeException(exception);\n}");
        System.out.println(sb);
    }

    public static void test2() {
        String content = """
            FUNCTION_CAT String => function catalog (may be null)
            FUNCTION_SCHEM String => function schema (may be null)
            FUNCTION_NAME String => function name. This is the name used to invoke the function
            REMARKS String => explanatory comment on the function
            FUNCTION_TYPE short => kind of function:
            functionResultUnknown - Cannot determine if a return value or table will be returned
            functionNoTable- Does not return a table
            functionReturnsTable - Returns a table
            SPECIFIC_NAME String => the name which uniquely identifies this function within its schema. This is a user specified, or DBMS generated, name that may be different then the FUNCTION_NAME for example with overload functions
                                    """;

        String[] lines = content.split("\n");

        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            int i = line.indexOf(" ");

            if (i <= 0) {
                continue;
            }
            String fieldName = line.substring(0, i);

            int j = line.indexOf("=>");

            if (j > 0) {
                sb.append("\t/**\n").append("\t * ").append(line).append("\n\t **/").append("\n");
                sb.append("private ").append(line, i, j - 1).append(" ").append(CaseFormat.CAMEL.normalize(fieldName)).append(";\n");
            }

        }

        System.out.println(sb);
    }

    public static void main(String[] args) {
        test2();
    }
}
