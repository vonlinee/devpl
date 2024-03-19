package io.devpl.codegen.parser.sql;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLIndex;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.stat.TableStat;
import io.devpl.codegen.db.ColumnInfo;
import io.devpl.codegen.db.TableInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 只针对MySQL
 */
public class DruidSqlParser {

    @Getter
    private final String rawSql;
    @Getter
    private final DbType dbType;
    private final List<SQLStatement> statements;

    /**
     * 解析单条sql
     *
     * @param sql    sql
     * @param dbType 数据库类型
     */
    public DruidSqlParser(String sql, DbType dbType) {
        this.rawSql = sql;
        this.dbType = dbType;
        this.statements = SQLUtils.parseStatements(rawSql, dbType);
    }

    public void parseCreate() {
        for (SQLStatement sqlStatement : statements) {
            MySqlCreateTableStatement ddlCreate = (MySqlCreateTableStatement) sqlStatement;
            SQLColumnDefinition newColDef = new SQLColumnDefinition();
            newColDef.setName("create_time");
            newColDef.setComment("创建时间");
            final SQLCharacterDataType dataType = new SQLCharacterDataType("VARCHAR");
            dataType.addArgument(new SQLIntegerExpr(36));
            newColDef.setDataType(dataType);
            newColDef.addConstraint(new SQLNotNullConstraint());
            ddlCreate.addColumn(newColDef);
            System.out.println(ddlCreate);
        }
    }

    /**
     * 新增一列的定义信息
     *
     * @return SQLColumnDefinition
     */
    public SQLColumnDefinition newColumnDefinition(String columnName) {
        return null;
    }

    public void sort(List<SQLTableElement> tableElementList) {
        for (SQLTableElement element : tableElementList) {
            if (element instanceof SQLIndex) {

            }
        }
    }

    public static TableInfo parseDDL(String ddl, DbType dbType) {
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
}
