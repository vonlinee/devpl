package io.devpl.codegen.parser.sql;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.stat.TableStat;
import io.devpl.codegen.db.ColumnInfo;
import io.devpl.codegen.db.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DruidMySqlParser extends DruidSqlParser {
    @Override
    public CreateTableParseResult parseCreateTableSql(String dbType, String sql) {
        CreateTableParseResult result = new CreateTableParseResult();
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
        if (!(parser.parseStatement() instanceof MySqlCreateTableStatement stmt)) {
            return result;
        }
        TableInfo tableInfo = parseDDL(sql, DbType.of(dbType));
        result.setTableInfo(tableInfo);
        return result;
    }

    public TableInfo parseDDL(String ddl, DbType dbType) {
        TableInfo tableInfo = new TableInfo();
        // 创建解析器
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(ddl, dbType);
        if (!(parser.parseStatement() instanceof MySqlCreateTableStatement stmt)) {
            return tableInfo;
        }
        MySQLColumnVisitor columnVisitor = new MySQLColumnVisitor();
        // 解析
        stmt.accept(columnVisitor);
        // 表名称
        tableInfo.setName(SQLUtils.normalize(stmt.getTableName(), dbType));
        // 表备注
        tableInfo.setComment(SQLUtils.normalize(String.valueOf(stmt.getComment()), dbType));
        // 表配置信息
        List<Map.Entry<String, String>> options = new ArrayList<>();
        for (SQLAssignItem tableOption : stmt.getTableOptions()) {
            options.add(Map.entry(String.valueOf(tableOption.getTarget()), String.valueOf(tableOption.getValue())));
        }
        tableInfo.setOptions(options);
        // 列信息
        List<ColumnInfo> columns = new ArrayList<>();

        Map<String, ColumnInfo> columnInfoMap = new HashMap<>();
        for (TableStat.Column column : columnVisitor.getColumns()) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setName(column.getName());
            columnInfo.setFullName(column.getFullName());
            columnInfo.setTableName(column.getTable());
            columnInfo.setDataType(column.getDataType());
            // 字段注释信息
            Map<String, Object> attributes = column.getAttributes();
            if (attributes != null) {
                columnInfo.setComment(String.valueOf(attributes.getOrDefault("comment", "")));
                columnInfo.setAttributes(attributes);
            }
            columnInfoMap.put(column.getName(), columnInfo);
            columns.add(columnInfo);
        }

        List<SQLColumnDefinition> columnDefinitions = stmt.getColumnDefinitions();
        for (SQLColumnDefinition cd : columnDefinitions) {
            // 这里的 columnName 可能包括列名称的引号
            String columnName = cd.getColumnName();
            columnName = columnName.substring(1, columnName.length() - 1);

            ColumnInfo columnInfo = columnInfoMap.get(columnName);
            if (columnInfo != null) {
                // 完整的数据类型定义
                columnInfo.setDataTypeDefinition(String.valueOf(cd.getDataType()));
                columnInfo.setCharsetDefinition(String.valueOf(cd.getCharsetExpr()));
            }
        }

        tableInfo.setColumns(columns);
        // 索引信息
        tableInfo.setIndexes(columnVisitor.getIndices());
        return tableInfo;
    }

    /**
     * 单条 insert 语句
     *
     * @param dbType 数据库类型
     * @param sql    sql语句
     * @return 解析结果
     */
    @Override
    public InsertSqlParseResult parseInsertSql(String dbType, String sql) {
        InsertSqlParseResult result = new InsertSqlParseResult();

        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
        SQLStatement statement = parser.parseStatement();

        MySqlInsertStatement insertStmt = (MySqlInsertStatement) statement;

        // INSERT SQL的表信息
        SQLExprTableSource tableSource = insertStmt.getTableSource();
        SqlTable table = new SqlTable();
        table.setName(tableSource.getTableName());
        table.setCatalog(tableSource.getCatalog());
        table.setSchema(tableSource.getSchema());
        result.setTable(table);

        // INSERT SQL的列信息
        List<InsertColumn> insertColumns = new ArrayList<>();
        List<SQLExpr> columns = insertStmt.getColumns();
        for (SQLExpr column : columns) {
            if (column instanceof SQLIdentifierExpr identifierExpr) {
                InsertColumn insertColumn = new InsertColumn();
                insertColumn.setColumnName(identifierExpr.getName());
                insertColumn.setTableName(table.getName());
                insertColumns.add(insertColumn);
            }
        }
        result.setInsertColumns(insertColumns);

        // 插入语句的值
        List<List<String>> columnValues = new ArrayList<>();
        // 如果是批量插入的insert：insert into tab(id,name) values(1,'a'),(2,'b'),(3,'c');
        List<SQLInsertStatement.ValuesClause> assignValueClauses = insertStmt.getValuesList();
        if (assignValueClauses != null && assignValueClauses.size() > 1) {   // 批量插入
            for (int j = 0; j < assignValueClauses.size(); j++) {
                SQLInsertStatement.ValuesClause valueClause = assignValueClauses.get(j);
                List<SQLExpr> values = valueClause.getValues();
                List<String> columnValuesOfOneRow = new ArrayList<>();
                for (SQLExpr sqlExpression : values) {
                    // 处理不同的数据类型
                    columnValuesOfOneRow.add(sqlExpression.toString());
                }
                columnValues.add(columnValuesOfOneRow);
            }
        } else {
            // 非批量插入
            List<SQLExpr> sqlExpressions = insertStmt.getValues().getValues();
            List<String> columnValuesOfOneRow = new ArrayList<>();
            for (SQLExpr sqlExpression : sqlExpressions) {
                // 处理不同的数据类型
                columnValuesOfOneRow.add(sqlExpression.toString());
            }
            columnValues.add(columnValuesOfOneRow);
        }
        result.setColumnValues(columnValues);

        // 如果是 INTO INTO SELECT 语句，则可以获取 select查询
        if (insertStmt.getQuery() != null) {
            SQLSelect select = insertStmt.getQuery();
        }

        // ON DUPLICATE UPDATE 部分可以使用下面的语句获取
        List<SQLExpr> dku = insertStmt.getDuplicateKeyUpdate();
        if (dku != null && !dku.isEmpty()) {

        }
        return result;
    }

    @Override
    public UpdateSqlParseResult parseUpdateSql(String dbType, String sql) {
        UpdateSqlParseResult result = new UpdateSqlParseResult();
        try {
            SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
            SQLStatement stmt = parser.parseStatement();
            MySqlUpdateStatement updateStmt = (MySqlUpdateStatement) stmt;
            SQLTableSource ts = updateStmt.getTableSource();
            // 如果是多表 UPDATE 语句，可以使用下面的语句进行判断
            if (ts != null && ts.toString().contains(",")) {
                throw new UnsupportedOperationException("parsing multiple-table update sql is not supported");
            }

            SqlTable table = new SqlTable();
            table.setName(updateStmt.getTableName().getSimpleName().replace("`", ""));
            result.setTable(table);

            // 获得 UPDATE 语句的 WHERE 部分

            SQLExpr se = updateStmt.getWhere();
            // WHERE 中有子查询： update company set name='com' where id in (select id from xxx where ...)
            if (se instanceof SQLInSubQueryExpr) {

            }
            if (updateStmt.getWhere() != null) {
                result.setWhereCondition(updateStmt.getWhere().toString());
            }

            // 如果where 部分由 select 语句，由：se instanceof SQLInSubQueryExpr 来判断。

            List<SQLUpdateSetItem> items = updateStmt.getItems();
            // update 对应的列和值
            List<UpdateColumn> updateColumns = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                SQLUpdateSetItem item = items.get(i);
                UpdateColumn updateColumn = new UpdateColumn();
                updateColumn.setColumnName(item.getColumn().toString());
                updateColumn.setValue(item.getValue().toString());
                updateColumns.add(updateColumn);
            }
            result.setUpdateColumns(updateColumns);


            // order by 部分
            SQLOrderBy orderBy = updateStmt.getOrderBy();
            // limit 部分
            SQLLimit limit = updateStmt.getLimit();

            // TODO 待补充
            /*
            boolean flag = false;
            if (orderBy != null && orderBy.getItems() != null && !orderBy.getItems().isEmpty()) {
                for (int i = 0; i < orderBy.getItems().size(); i++) {
                    SQLSelectOrderByItem item = orderBy.getItems().get(i);
                    SQLOrderingSpecification os = item.getType();
                }
            }
            if (limit != null) {
                // 分为两种情况： limit 10;   limit 10,10;
            }
             */
        } catch (Exception exception) {

        }
        return result;
    }

    /**
     * 例如 alter table t add colomn name varchar(30)
     *
     * @param dbType 数据库类型
     * @param sql    sql语句
     * @return 解析结果
     */
    @Override
    public AlterSqlParseResult parseAlterSql(String dbType, String sql) {
        AlterSqlParseResult result = new AlterSqlParseResult();
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
        SQLStatement statement = parser.parseStatement();
        SQLAlterTableStatement alter = (SQLAlterTableStatement) statement;
        SQLExprTableSource source = alter.getTableSource();
        String tableName = source.toString();
        return result;
    }
}
