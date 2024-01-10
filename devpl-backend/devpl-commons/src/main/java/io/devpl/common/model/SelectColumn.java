package io.devpl.common.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询列
 */
@Getter
@Setter
public class SelectColumn {
    protected String table;
    protected String name;
    protected String alias;

    public SelectColumn(String table, String name, String alias) {
        this.table = table;
        this.name = name;
        this.alias = alias;
    }

    public boolean isSelectAll() {
        return "*".equals(name);
    }
}
