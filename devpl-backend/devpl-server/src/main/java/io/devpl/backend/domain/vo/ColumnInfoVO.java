package io.devpl.backend.domain.vo;

import lombok.Data;

import java.util.Set;

@Data
public class ColumnInfoVO {

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 数据类型
     */
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

    /**
     * 是否虚拟列
     */
    private Boolean virtual;

    /**
     * 是否是主键
     */
    private Boolean primaryKey;

    /**
     * 列备注信息
     */
    private String remarks;

    /**
     * 可选择的数据类型列表，和不同数据库平台相关
     */
    private Set<String> dataTypes;
}
