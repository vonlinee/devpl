package org.apache.ddlutils.platform;

public class DefaultBuildingSql implements BuildingSql {

    StringBuilder sql = new StringBuilder();

    @Override
    public String toString() {
        return sql.toString();
    }

    @Override
    public String toString(int start, int end) {
        return sql.substring(start, end);
    }

    @Override
    public void append(String text) {
        sql.append(text);
    }
}
