package io.devpl.common.interfaces.impl;

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
import io.devpl.common.exception.FieldParseException;
import io.devpl.common.interfaces.FieldParser;
import io.devpl.common.model.SelectColumn;
import io.devpl.common.model.SelectTable;
import io.devpl.sdk.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从查询Sql中解析字段
 * <a href="https://juejin.cn/post/7083280831602982919">...</a>
 */
public class SqlFieldParser implements FieldParser, SQLASTVisitor {

    private final String dbType;

    public SqlFieldParser(String dbType) {
        this.dbType = dbType;
    }

    @Override
    public List<Map<String, Object>> parse(String sql) throws FieldParseException {

        DbType dbTypeEnum = DbType.of(dbType);

        WallProvider provider = new MySqlWallProvider();
        WallCheckResult result = provider.check(sql);
        if (result.getViolations().isEmpty()) {
            // 无SQL注入风险和错误, 可执行查询
            List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);
            for (SQLStatement stmt : sqlStatements) {
                stmt.accept(this);
            }
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (SelectColumn selectColumn : selectColumns) {
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

    public static final Pattern PARAMETER_PATTERN = Pattern.compile("#\\{[a-zA-z]*}");
    private static final int PARAMETER_START_INDEX = 2;

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
            if (item.getExpr() instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr expr = (SQLIdentifierExpr) item.getExpr();
                selectColumns.add(new SelectColumn(selectTables.get(0).getName(), expr.getName(), alias));
            } else if (item.getExpr() instanceof SQLAllColumnExpr) {
                SQLAllColumnExpr expr = (SQLAllColumnExpr) item.getExpr();
                selectColumns.add(new SelectColumn(selectTables.get(0).getName(), expr.toString(), alias));
            } else if (item.getExpr() instanceof SQLMethodInvokeExpr) {
                SQLMethodInvokeExpr expr = (SQLMethodInvokeExpr) item.getExpr();
                selectColumns.add(new SelectColumn(null, expr.toString(), alias));
            } else if (item.getExpr() instanceof SQLPropertyExpr) {
                SQLPropertyExpr expr = (SQLPropertyExpr) item.getExpr();
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
