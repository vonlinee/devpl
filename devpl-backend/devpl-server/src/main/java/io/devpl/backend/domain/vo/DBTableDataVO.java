package io.devpl.backend.domain.vo;

import io.devpl.codegen.jdbc.meta.ResultSetColumnMetadata;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 数据库表的数据
 */
@Getter
@Setter
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
