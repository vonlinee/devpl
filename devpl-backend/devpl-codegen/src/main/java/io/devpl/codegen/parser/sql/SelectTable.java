package io.devpl.codegen.parser.sql;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询表
 */
@Getter
@Setter
public class SelectTable {

    /**
     * 数据库名称
     */
    private String databaseName;

    /**
     * 表名称
     */
    protected String name;

    /**
     * 查询表用到的别名
     */
    protected String alias;

    /**
     * 是否是临时表
     */
    private boolean temporary;


    public SelectTable(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }
}
