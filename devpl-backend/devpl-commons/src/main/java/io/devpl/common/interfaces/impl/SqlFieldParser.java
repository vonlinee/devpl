package io.devpl.common.interfaces.impl;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import io.devpl.codegen.parser.sql.SelectColumn;
import io.devpl.codegen.parser.sql.SelectSqlParser;
import io.devpl.common.exception.FieldParseException;
import io.devpl.common.interfaces.FieldParser;
import io.devpl.sdk.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从查询Sql中解析字段
 * <a href="https://juejin.cn/post/7083280831602982919">...</a>
 */
public class SqlFieldParser implements FieldParser, SQLASTVisitor {

    private final String dbType;

    SelectSqlParser parser;

    public SqlFieldParser(String dbType) {
        this.dbType = dbType;
        parser = new SelectSqlParser(dbType);
    }

    @Override
    public List<Map<String, Object>> parse(String sql) throws FieldParseException {
        DbType dbTypeEnum = DbType.of(dbType);
        List<Map<String, Object>> list = new ArrayList<>();
        for (SelectColumn selectColumn : parser.getSelectColumns()) {
            Map<String, Object> field = new HashMap<>();
            field.put(FIELD_NAME, selectColumn.getName());
            // TODO 推断类型
            field.put(FIELD_TYPE, "String");
            field.put(FIELD_DESCRIPTION, "");
            list.add(field);

            // 添加别名字段
            if (StringUtils.hasText(selectColumn.getAlias())) {
                Map<String, Object> aliasField = new HashMap<>();
                field.put(FIELD_NAME, selectColumn.getAlias());
                // TODO 推断类型
                field.put(FIELD_TYPE, "String");
                field.put(FIELD_DESCRIPTION, "");
                list.add(aliasField);
            }
        }
        return list;
    }
}
