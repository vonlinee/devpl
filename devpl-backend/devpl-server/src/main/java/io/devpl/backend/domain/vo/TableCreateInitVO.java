package io.devpl.backend.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 字段转数据库字段
 */
@Data
public class TableCreateInitVO {

    /**
     * 可选的数据类型列表
     */
    private List<SelectOptionVO> dataTypes;

    /**
     * 初始化的列信息
     */
    private List<ColumnInfoVO> columns;
}
