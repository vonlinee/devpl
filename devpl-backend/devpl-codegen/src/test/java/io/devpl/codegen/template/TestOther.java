package io.devpl.codegen.template;

import io.devpl.codegen.core.CaseFormat;
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
            TYPE_NAME String => Type name
            DATA_TYPE int => SQL data type from java.sql.Types
            PRECISION int => maximum precision
            LITERAL_PREFIX String => prefix used to quote a literal (may be null)
            LITERAL_SUFFIX String => suffix used to quote a literal (may be null)
            CREATE_PARAMS String => parameters used in creating the type (may be null)
            NULLABLE short => can you use NULL for this type.
            typeNoNulls - does not allow NULL values
            typeNullable - allows NULL values
            typeNullableUnknown - nullability unknown
            CASE_SENSITIVE boolean=> is it case sensitive.
            SEARCHABLE short => can you use "WHERE" based on this type:
            typePredNone - No support
            typePredChar - Only supported with WHERE .. LIKE
            typePredBasic - Supported except for WHERE .. LIKE
            typeSearchable - Supported for all WHERE ..
            UNSIGNED_ATTRIBUTE boolean => is it unsigned.
            FIXED_PREC_SCALE boolean => can it be a money value.
            AUTO_INCREMENT boolean => can it be used for an auto-increment value.
            LOCAL_TYPE_NAME String => localized version of type name (may be null)
            MINIMUM_SCALE short => minimum scale supported
            MAXIMUM_SCALE short => maximum scale supported
            SQL_DATA_TYPE int => unused
            SQL_DATETIME_SUB int => unused
            NUM_PREC_RADIX int => usually 2 or 10
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
        test1();
    }
}
