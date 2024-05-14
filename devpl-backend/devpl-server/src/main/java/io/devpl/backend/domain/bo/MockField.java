package io.devpl.backend.domain.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MockField {
    /**
     * 数据库字段名
     */
    private String fieldName;
    /**
     * 列数据类型
     */
    private Integer dataType;
    /**
     * 生成器ID
     */
    private String generatorId;
    /**
     * 生成器名称
     */
    private String generatorName;
}
