package io.devpl.codegen.parser.sql;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLIndex;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLCharacterDataType;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLNotNullConstraint;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import lombok.Getter;

import java.util.List;

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

}
