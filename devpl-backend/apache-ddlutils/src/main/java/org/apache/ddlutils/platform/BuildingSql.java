package org.apache.ddlutils.platform;

/**
 * 保存 sql 结果，能进行调整
 */
public interface BuildingSql {

    String toString();

    String toString(int start, int end);

    void append(String text);
}
