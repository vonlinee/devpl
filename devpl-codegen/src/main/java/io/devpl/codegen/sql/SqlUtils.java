package io.devpl.codegen.sql;

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
public class SqlUtils {

    /**
     * @param sql 查询SQL语句
     * @return 返回格式：{tableName}{columnName}
     */
    public static Map<String, Set<String>> getSelectColumns(String sql) {
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, DbType.mysql, true);
        List<SQLStatement> sqlStatements = parser.parseStatementList();
        SQLStatement sqlStatement = sqlStatements.get(0);
        if (sqlStatement instanceof SQLSelectStatement) {
            return getSelectedColumns((SQLSelectStatement) sqlStatement);
        }
        return Collections.emptyMap();
    }

    /**
     * 获取SQL中查询的所有列
     * @param sqlStatement SQLStatement
     * @return 查询的所有列
     */
    public static Map<String, Set<String>> getSelectedColumns(SQLSelectStatement sqlStatement) {
        SQLSelect select = sqlStatement.getSelect();
        SQLSelectQuery query = select.getQuery();
        Map<String, Set<String>> columnMap = new HashMap<>();

        if (query instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) query;
            List<SQLSelectItem> selectList = queryBlock.getSelectList();
            SQLTableSource from = queryBlock.getFrom();
            // 找到所有的别名和表明映射
            Map<String, String> tableNames = findAllTalbeNames(from);
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
     * @param from 根SQL
     * @return
     */
    private static Map<String, String> findAllTalbeNames(SQLTableSource from) {
        Map<String, String> tableAliasMapping = new LinkedHashMap<>();
        // 存在表连接
        if (from instanceof SQLJoinTableSource) {
            SQLTableSource tmp = from;
            for (; ; ) {
                if (!(tmp instanceof SQLJoinTableSource)) {
                    if (tmp instanceof SQLExprTableSource) {
                        SQLExprTableSource tableSource = (SQLExprTableSource) tmp;
                        final String alias = tableSource.getAlias();
                        tableAliasMapping.put(alias, tableSource.getTableName());
                    }
                    break;
                }
                // 当前表：倒数第二个表
                SQLJoinTableSource currentTableSource = (SQLJoinTableSource) tmp;
                tmp = currentTableSource.getLeft();
                // 获取右连接表
                SQLTableSource right = currentTableSource.getRight();

                String currTableName = null;
                if (right instanceof SQLExprTableSource) {
                    SQLExprTableSource sqlExprTableSource = (SQLExprTableSource) right;
                    currTableName = sqlExprTableSource.getTableName();
                }
                String alias = right.getAlias();
                // 没有别名
                if (alias == null) {
                    alias = currTableName;
                }
                tableAliasMapping.put(alias, currTableName);
            }
        } else if (from instanceof SQLExprTableSource) {
            // 单表
            SQLExprTableSource exprTableSource = (SQLExprTableSource) from;
            final String alias = exprTableSource.getAlias();
            final String tableName = exprTableSource.getTableName();
            tableAliasMapping.put(String.valueOf(alias), tableName);
        }
        return tableAliasMapping;
    }

    /**
     * 使用反单引号进行包裹 '`'
     * @return ``
     */
    public static String wrapWithBackquote(String columnName) {
        if (!columnName.startsWith("`")) {
            columnName = "`" + columnName;
        }
        if (!columnName.endsWith("`")) {
            columnName = columnName + "`";
        }
        return columnName;
    }
}
