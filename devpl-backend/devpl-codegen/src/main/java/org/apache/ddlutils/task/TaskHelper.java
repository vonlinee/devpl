package org.apache.ddlutils.task;


import java.util.ArrayList;
import java.util.List;

/**
 * Contains some utility functions for the Ant tasks.
 */
public class TaskHelper {
    /**
     * Parses the given comma-separated string list. A comma within a string needs to be
     * escaped as '\,'. Also, the individual strings are not trimmed but returned as-is.
     *
     * @param stringList The comma-separated list of strings
     * @return The strings as an array
     */
    public String[] parseCommaSeparatedStringList(String stringList) {
        String[] tokens = stringList.split(",");
        List<String> values = new ArrayList<>();
        StringBuilder last = null;

        for (String str : tokens) {
            int strLen = str.length();
            boolean endsInSlash = (strLen > 0) && (str.charAt(strLen - 1) == '\\') &&
                                  ((strLen == 1) || (str.charAt(strLen - 2) != '\\'));

            if (last != null) {
                last.append(",").append(str);
                if (!endsInSlash) {
                    values.add(last.toString());
                    last = null;
                }
            } else if (endsInSlash) {
                last = new StringBuilder(str.substring(0, strLen - 1));
            } else {
                values.add(str);
            }
        }
        if (last != null) {
            values.add(last + ",");
        }
        return values.toArray(new String[0]);
    }
}
