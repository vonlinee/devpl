package io.devpl.codegen.parser.sql;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.wall.WallCheckResult;
import com.alibaba.druid.wall.WallProvider;
import com.alibaba.druid.wall.spi.MySqlWallProvider;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class SelectSqlParser implements SQLASTVisitor {

    private final String dbType;

    public SelectSqlParser(String dbType) {
        this.dbType = dbType;
    }

    public void parse(String sql) {
        WallProvider provider = new MySqlWallProvider();
        WallCheckResult result = provider.check(sql);
        if (result.getViolations().isEmpty()) {
            // 无SQL注入风险和错误, 可执行查询
            List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);
            for (SQLStatement stmt : sqlStatements) {
                stmt.accept(this);
            }
        }
    }

    public static final Pattern PARAMETER_PATTERN = Pattern.compile("#\\{[a-zA-z]*}");
    private static final int PARAMETER_START_INDEX = 2;

    /**
     * 查询的项
     */
    protected List<SQLSelectItem> selectItems = new ArrayList<>();

    /**
     * 查询的表
     */
    protected List<SelectTable> selectTables = new ArrayList<>();

    /**
     * 查询的列
     */
    protected List<SelectColumn> selectColumns = new ArrayList<>();

    /**
     * 查询的参数
     */
    protected List<String> parameters = new ArrayList<>();

    public List<SQLSelectItem> getSelectItems() {
        return selectItems;
    }

    public List<SelectTable> getSelectTables() {
        return selectTables;
    }

    public List<SelectColumn> getSelectColumns() {
        return selectColumns;
    }

    public List<String> getParameters() {
        return parameters;
    }

    /**
     * select 语句
     *
     * @param x SQLSelectQueryBlock
     */
    @Override
    public void endVisit(SQLSelectQueryBlock x) {
        computeSelectColumns();
    }

    @Override
    public boolean visit(SQLExprTableSource x) {
        selectTables.add(new SelectTable(x.getTableName(), x.getAlias()));
        return false;
    }

    @Override
    public boolean visit(SQLCharExpr x) {
        computeParameter(x.toString());
        return false;
    }

    @Override
    public boolean visit(SQLSelectItem x) {
        selectItems.add(x);
        return false;
    }

    @Override
    public boolean visit(SQLVariantRefExpr x) {
        computeParameter(x.getName());
        return false;
    }

    /**
     * 访问查询参数表达式, 匹配查询参数
     *
     * @param expr 查询参数表达式
     */
    protected void computeParameter(String expr) {
        Matcher matcher = PARAMETER_PATTERN.matcher(expr);
        if (matcher.find()) {
            String match = matcher.group();
            parameters.add(match.substring(PARAMETER_START_INDEX, match.length() - 1));
        }
    }

    /**
     * 计算查询列
     */
    protected void computeSelectColumns() {
        selectItems.forEach(item -> {
            String alias = item.getAlias();
            if (item.getExpr() instanceof SQLIdentifierExpr expr) {
                selectColumns.add(new SelectColumn(selectTables.get(0).getName(), expr.getName(), alias));
            } else if (item.getExpr() instanceof SQLAllColumnExpr expr) {
                selectColumns.add(new SelectColumn(selectTables.get(0).getName(), expr.toString(), alias));
            } else if (item.getExpr() instanceof SQLMethodInvokeExpr expr) {
                selectColumns.add(new SelectColumn(null, expr.toString(), alias));
            } else if (item.getExpr() instanceof SQLPropertyExpr expr) {
                selectColumns.add(new SelectColumn(getSelectTableNameByAlias(expr.getOwnerName()), expr.getName(), item.getAlias()));
            }
        });
    }

    /**
     * 根据查询表别名获取查询表名
     * getSelectTableNameByAlias("t") -> "t_user" or null
     *
     * @param alias 查询表别名
     * @return 查询表名
     */
    protected String getSelectTableNameByAlias(String alias) {
        return getSelectTableByAlias(alias).map(SelectTable::getName).orElse(null);
    }

    /**
     * 根据查询表别名获取查询表
     *
     * @param alias 查询表别名
     * @return 查询表
     */
    protected Optional<SelectTable> getSelectTableByAlias(String alias) {
        return selectTables.stream().filter(table -> alias.equals(table.getAlias())).findFirst();
    }
}
