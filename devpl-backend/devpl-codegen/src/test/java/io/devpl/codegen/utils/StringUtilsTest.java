package io.devpl.codegen.utils;

import org.junit.Test;

import java.util.Objects;

public class StringUtilsTest {

    @Test
    public void toMultiline() {

        String s = """
            "SELECT A.COLUMN_NAME, A.DATA_TYPE, B.COMMENTS,DECODE(C.POSITION, '1', 'PRI') KEY FROM ALL_TAB_COLUMNS A "
                        + " INNER JOIN ALL_COL_COMMENTS B ON A.TABLE_NAME = B.TABLE_NAME AND A.COLUMN_NAME = B.COLUMN_NAME AND B.OWNER = '#schema'"
                        + " LEFT JOIN ALL_CONSTRAINTS D ON D.TABLE_NAME = A.TABLE_NAME AND D.CONSTRAINT_TYPE = 'P' AND D.OWNER = '#schema'"
                        + " LEFT JOIN ALL_CONS_COLUMNS C ON C.CONSTRAINT_NAME = D.CONSTRAINT_NAME AND C.COLUMN_NAME=A.COLUMN_NAME AND C.OWNER = '#schema'"
                        + "WHERE A.OWNER = '#schema' AND A.TABLE_NAME = '%s' ORDER BY A.COLUMN_ID "
            """;

        String s1 = """
                SELECT A.COLUMN_NAME, A.DATA_TYPE, B.COMMENTS,
                       DECODE(C.POSITION, '1', 'PRI') AS KEY
                FROM ALL_TAB_COLUMNS A
                INNER JOIN ALL_COL_COMMENTS B ON A.TABLE_NAME = B.TABLE_NAME AND A.COLUMN_NAME = B.COLUMN_NAME AND B.OWNER = '#schema'
                LEFT JOIN ALL_CONSTRAINTS D ON D.TABLE_NAME = A.TABLE_NAME AND D.CONSTRAINT_TYPE = 'P' AND D.OWNER = '#schema'
                LEFT JOIN ALL_CONS_COLUMNS C ON C.CONSTRAINT_NAME = D.CONSTRAINT_NAME AND C.COLUMN_NAME = A.COLUMN_NAME AND C.OWNER = '#schema'
                WHERE A.OWNER = '#schema' AND A.TABLE_NAME = '%s'
                ORDER BY A.COLUMN_ID
            """;

        System.out.println(Objects.equals(s, s1));
    }

    public String toMultilineString(String concatString) {
        char[] charArray = concatString.toCharArray();

        StringBuilder result = new StringBuilder();

        int l = 0, r = 0;

        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];

            if ("\"".equals(String.valueOf(c))) {
                l = i + 1;
                boolean plusFound = false;
                while (r < charArray.length) {
                    if (charArray[r] == '+') {
                        plusFound = true;
                        break;
                    }
                    r++;
                }
                int ll = r + 1;
                if (plusFound) {
                    while (ll < charArray.length) {
                        if ("\"".equals(String.valueOf(charArray[ll]))) {
                            break;
                        }
                        ll++;
                    }
                }
            }
        }
        return result.toString();
    }
}
