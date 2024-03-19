package io.devpl.common.interfaces.impl;

import com.alibaba.druid.DbType;
import io.devpl.codegen.db.ColumnInfo;
import io.devpl.codegen.db.TableInfo;
import io.devpl.codegen.parser.sql.DruidSqlParser;
import io.devpl.common.exception.FieldParseException;
import io.devpl.common.interfaces.FieldParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从DDL中解析字段
 * <a href="https://juejin.cn/post/7083280831602982919">...</a>
 */
public class DDLFieldParser implements FieldParser {

    private final String dbType;

    public DDLFieldParser(String dbType) {
        this.dbType = dbType;
    }

    @Override
    public List<Map<String, Object>> parse(String sql) throws FieldParseException {

        DbType dbTypeEnum = DbType.of(dbType);
        TableInfo tableInfo = DruidSqlParser.parseDDL(sql, dbTypeEnum);

        // SQL 注入
//        WallProvider provider = new MySqlWallProvider();
//        WallCheckResult result = provider.check(sql);
//        if (result.getViolations().isEmpty()) {
//            // 无SQL注入风险和错误, 可执行查询
//            List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);
//            for (SQLStatement stmt : sqlStatements) {
//                stmt.accept(this);
//            }
//        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (ColumnInfo columnInfo : tableInfo.getColumns()) {
            Map<String, Object> field = new HashMap<>();
            field.put(FIELD_NAME, columnInfo.getName());
            field.put(FIELD_TYPE, columnInfo.getDataType());
            field.put(FIELD_DESCRIPTION, columnInfo.getComment());
            list.add(field);
        }
        return list;
    }
}
