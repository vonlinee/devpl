package io.devpl.generator.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 数据库表的数据
 */
@Data
public class DBTableDataVO {

    /**
     * 表头
     */
    private List<String> headers;


}
