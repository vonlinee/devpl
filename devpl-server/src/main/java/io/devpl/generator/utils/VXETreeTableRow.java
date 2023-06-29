package io.devpl.generator.utils;

import lombok.Data;

/**
 * 适配VXE属性表格数据结构
 */
@Data
public class VXETreeTableRow {

    /**
     * 行ID
     */
    private String id;

    /**
     * 父ID
     */
    private String parentId;

    /**
     * 字段1
     */
    private Object label;

    /**
     * 字段1
     */
    private Object value1;
}
