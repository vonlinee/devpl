package io.devpl.backend.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据源列表
 */
@Getter
@Setter
public class DataSourceVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    public DataSourceVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
