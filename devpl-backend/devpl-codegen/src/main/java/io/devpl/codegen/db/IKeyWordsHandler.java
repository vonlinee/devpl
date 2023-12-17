package io.devpl.codegen.db;

import java.util.Collection;

/**
 * 关键字处理接口
 */
public interface IKeyWordsHandler {

    /**
     * 获取关键字
     *
     * @return 关键字集合
     */
    Collection<String> getKeyWords();

    /**
     * 格式化关键字格式
     *
     * @return 格式
     */
    String formatStyle();

    /**
     * 是否为关键字
     *
     * @param columnName 字段名称
     * @return 是否为关键字
     */
    boolean isKeyWords(String columnName);

    /**
     * 格式化字段
     *
     * @param columnName 字段名称
     * @return 格式化字段
     */
    default String formatColumn(String columnName) {
        return String.format(formatStyle(), columnName);
    }
}
