package io.devpl.generator.domain.vo;

import lombok.Data;

/**
 * 数据源列表
 */
@Data
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
