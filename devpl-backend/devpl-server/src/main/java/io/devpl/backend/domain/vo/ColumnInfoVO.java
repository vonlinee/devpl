package io.devpl.backend.domain.vo;

import lombok.Data;

import java.util.Set;

@Data
public class ColumnInfoVO {

    private String columnName;
    private String dataType;

    /**
     * 列大小
     */
    private Integer columnSize;

    /**
     * 小数点位数
     */
    private Integer decimalDigits;
    /**
     * 是否可空
     */
    private Boolean nullable;
    private Boolean virtual;
    private Boolean primaryKey;
    private String remarks;

    private Set<String> dataTypes;
}
