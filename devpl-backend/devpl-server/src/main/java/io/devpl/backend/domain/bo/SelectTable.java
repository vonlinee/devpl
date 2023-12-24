package io.devpl.backend.domain.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询表
 */
@Getter
@Setter
public class SelectTable {

    protected String name;
    protected String alias;

    public SelectTable(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }
}
