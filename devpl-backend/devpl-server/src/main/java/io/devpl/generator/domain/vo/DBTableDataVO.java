package io.devpl.generator.domain.vo;

import io.devpl.generator.jdbc.metadata.ResultSetColumnMetadata;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 数据库表的数据
 */
@Data
public class DBTableDataVO {

    /**
     * 表头
     */
    private List<ResultSetColumnMetadata> headers;

    /**
     * 表行数据列表
     */
    private List<String[]> rows;

    /**
     * 表行数据列表 - 另一种数据格式 表头和表行不分开
     */
    private List<Map<String, Object>> rows1;
}
