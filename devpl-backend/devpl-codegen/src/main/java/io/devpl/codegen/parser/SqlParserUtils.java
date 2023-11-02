package io.devpl.codegen.parser;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;

import java.util.*;

/**
 * Druid Sql Parser
 */
public class SqlParserUtils {

    /**
     * 获取sql查询的字段，不会进行实际的数据库查询，只解析sql，得到真实的表名，字段名，不包含sql中的自定义别名
     * TODO 如果有子查询，别名，需要得到对应的查询真实的表字段
     *
     * @param sql 查询SQL语句
     * @return 返回格式：{tableName} -> {columnName} 每张表查询的字段
     */
    public static Map<String, Set<String>> getSelectColumns(String sql) {
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, DbType.mysql, true);
        List<SQLStatement> sqlStatements = parser.parseStatementList();
        SQLStatement sqlStatement = sqlStatements.get(0);
        if (sqlStatement instanceof SQLSelectStatement sst) {
            return getSelectedColumns(sst);
        }
        return Collections.emptyMap();
    }

    /**
     * 获取SQL中查询的所有列
     *
     * @param sqlStatement SQLStatement
     * @return 查询的所有列
     */
    private static Map<String, Set<String>> getSelectedColumns(SQLSelectStatement sqlStatement) {
        // 获取查询sql
        SQLSelect select = sqlStatement.getSelect();
        SQLSelectQuery query = select.getQuery();
        Map<String, Set<String>> columnMap = new HashMap<>();

        if (query instanceof SQLSelectQueryBlock queryBlock) {
            List<SQLSelectItem> selectList = queryBlock.getSelectList();
            // FROM 子句 可能包含临时表
            SQLTableSource from = queryBlock.getFrom();
            // 找到所有的别名和表名映射
            Map<String, String> tableNames = findAllTalbeNames(from);

            // 将查询的字段别名转换为真实的表名
            for (SQLSelectItem sqlSelectItem : selectList) {
                SQLExpr expr = sqlSelectItem.getExpr();
                String selectColumn = expr.toString();
                String[] tableColumnNames = selectColumn.split("\\.");
                if (tableColumnNames.length == 2) {
                    String tableAlias = tableColumnNames[0];
                    String tableName = tableNames.get(tableAlias);
                    fillColumnMap(columnMap, tableName, tableColumnNames[1]);
                }
            }
        }
        return columnMap;
    }

    private static void fillColumnMap(Map<String, Set<String>> map, String tableName, String columnName) {
        Set<String> columns = map.computeIfAbsent(tableName, k -> new HashSet<>());
        columns.add(columnName);
    }

    /**
     * 解析SQL中所有表名和别名
     *
     * @param from 根SQL
     * @return 表.字段
     */
    private static Map<String, String> findAllTalbeNames(SQLTableSource from) {
        Map<String, String> tableAliasMapping = new LinkedHashMap<>();
        // 存在表连接
        if (from instanceof SQLJoinTableSource) {
            SQLTableSource tmp = from;
            for (; ; ) {
                if (!(tmp instanceof SQLJoinTableSource currentTableSource)) {
                    // 连接查询的最后一张表
                    if (tmp instanceof SQLExprTableSource tableSource) {
                        final String alias = tableSource.getAlias();
                        tableAliasMapping.put(alias, tableSource.getTableName());
                    }
                    break;
                }
                // 当前表：倒数第二个表
                tmp = currentTableSource.getLeft();
                // 获取右连接表
                SQLTableSource right = currentTableSource.getRight();

                String currTableName = null;
                if (right instanceof SQLExprTableSource sqlExprTableSource) {
                    currTableName = sqlExprTableSource.getTableName();
                } else if (right instanceof SQLSubqueryTableSource sqlSubqueryTableSource) {
                    // 子查询
                    currTableName = sqlSubqueryTableSource.getAlias();

                    SQLSelect select = sqlSubqueryTableSource.getSelect();
                    // 获取查询语句
                    SQLSelectQueryBlock queryBlock = select.getFirstQueryBlock();

                    // 查询的字段
                    List<SQLSelectItem> selectList = queryBlock.getSelectList();
                    for (SQLSelectItem sqlSelectItem : selectList) {


                    }
                    // FROM 部分
                    SQLTableSource from1 = queryBlock.getFrom();

                    System.out.println(from1);

                }

                String alias = right.getAlias();
                // 没有别名
                if (alias == null) {
                    alias = currTableName;
                }
                tableAliasMapping.put(alias, currTableName);
            }
        } else if (from instanceof SQLExprTableSource exprTableSource) {
            // 单表
            final String alias = exprTableSource.getAlias();
            final String tableName = exprTableSource.getTableName();
            tableAliasMapping.put(String.valueOf(alias), tableName);
        }
        return tableAliasMapping;
    }

    /**
     * 使用反单引号进行包裹 '`'
     *
     * @return ``
     */
    public static String wrapWithBackquote(String columnName) {
        if (columnName == null || columnName.isEmpty()) {
            return columnName;
        }
        if (!columnName.startsWith("`")) {
            columnName = "`" + columnName;
        }
        if (!columnName.endsWith("`")) {
            columnName = columnName + "`";
        }
        return columnName;
    }
}
