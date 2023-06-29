package io.devpl.codegen.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sql语句解析工具
 */
public class SqlParserUtils {
    /**
     * 查询sql字段
     **/
    public static List<String> selectItems(String sql) throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        List<SelectItem> selectitems = plain.getSelectItems();
        List<String> str_items = new ArrayList<>();
        if (selectitems != null) {
            for (SelectItem selectitem : selectitems) {
                str_items.add(selectitem.toString());
            }
        }
        return str_items;
    }

    public static List<String> selectTable(String sql) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        Select selectStatement = (Select) statement;
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        return tablesNamesFinder.getTableList(selectStatement);
    }

    public static List<String> selectJoin(String sql) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        Select selectStatement = (Select) statement;
        PlainSelect plain = (PlainSelect) selectStatement.getSelectBody();
        List<Join> joinList = plain.getJoins();
        List<String> tablewithjoin = new ArrayList<>();
        if (joinList != null) {
            for (Join join : joinList) {
                join.setLeft(true);// 是否开放left jion中的left
                tablewithjoin.add(join.toString());
            }
        }
        return tablewithjoin;
    }

    /**
     * 查询where
     **/
    public static String selectWhere(String sql) throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        Expression where_expression = plain.getWhere();
        return where_expression.toString();
    }

    public static List<Map<String, Object>> parseWhere(String sql) {
        try {
            Select select = (Select) CCJSqlParserUtil.parse(sql);
            SelectBody selectBody = select.getSelectBody();
            PlainSelect plainSelect = (PlainSelect) selectBody;
            Expression expr = CCJSqlParserUtil.parseCondExpression(plainSelect.getWhere().toString());
            List<Map<String, Object>> arrList = new ArrayList<>();
            expr.accept(new ExpressionDeParser() {
                int depth = 0;

                @Override
                public void visit(Parenthesis parenthesis) {
                    depth++;
                    parenthesis.getExpression().accept(this);
                    depth--;
                }

                @Override
                public void visit(OrExpression orExpression) {
                    visitBinaryExpr(orExpression, "OR");
                }

                @Override
                public void visit(AndExpression andExpression) {
                    visitBinaryExpr(andExpression, "AND");
                }

                private void visitBinaryExpr(BinaryExpression expr, String operator) {
                    Map<String, Object> map = new HashMap<>();
                    if (!(expr.getLeftExpression() instanceof OrExpression) && !(expr.getLeftExpression() instanceof AndExpression) && !(expr.getLeftExpression() instanceof Parenthesis)) {
                        getBuffer();
                    }
                    expr.getLeftExpression().accept(this);
                    map.put("leftExpression", expr.getLeftExpression());
                    map.put("operator", operator);
                    if (!(expr.getRightExpression() instanceof OrExpression) && !(expr.getRightExpression() instanceof AndExpression) && !(expr.getRightExpression() instanceof Parenthesis)) {
                        getBuffer();
                    }
                    expr.getRightExpression().accept(this);
                    map.put("rightExpression", expr.getRightExpression());
                    arrList.add(map);
                }
            });
            return arrList;
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<Object, Object> fullResolutionWhere(String where) {
        Map<Object, Object> map = new HashMap<>();
        try {
            Expression expr = CCJSqlParserUtil.parseCondExpression(where);
            expr.accept(new ExpressionVisitorAdapter() {
                @Override
                protected void visitBinaryExpression(BinaryExpression expr) {
                    if (expr instanceof ComparisonOperator) {
                        map.put("leftExpression", expr.getLeftExpression());
                        map.put("operate", expr.getStringExpression());
                        map.put("rightExpression", expr.getRightExpression());
                    }
                    super.visitBinaryExpression(expr);
                }
            });
            // 暂时无法解析IS NOT NULL 和 IS NULL
            if (CollectionUtils.isEmpty(map) && (where.toUpperCase().contains("IS NOT NULL") || where.toUpperCase()
                    .contains("IS NULL"))) {
                map.put("leftExpression", where.substring(0, where.lastIndexOf("IS")));
                map.put("operate", null);
                map.put("rightExpression", where.substring(where.lastIndexOf("IS"), where.length()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<String> selectGroupBy(String sql) throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        List<Expression> GroupByColumnReferences = plain.getGroupBy().getGroupByExpressions();
        List<String> str_groupby = new ArrayList<>();
        if (GroupByColumnReferences != null) {
            for (Expression groupByColumnReference : GroupByColumnReferences) {
                str_groupby.add(groupByColumnReference.toString());
            }
        }
        return str_groupby;
    }

    public static List<String> selectOrderBy(String sql) throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        List<OrderByElement> OrderByElements = plain.getOrderByElements();
        List<String> str_orderby = new ArrayList<String>();
        if (OrderByElements != null) {
            for (OrderByElement orderByElement : OrderByElements) {
                str_orderby.add(orderByElement.toString());
            }
        }
        return str_orderby;
    }

    public static Map<String, String> selectSubSelect(SelectBody selectBody) throws JSQLParserException {
        Map<String, String> map = new HashMap<>();
        if (selectBody instanceof PlainSelect) {
            List<SelectItem> selectItems = ((PlainSelect) selectBody).getSelectItems();
            for (SelectItem selectItem : selectItems) {
                if (selectItem.toString().contains("(") && selectItem.toString().contains(")")) {
                    map.put("selectItemsSubselect", selectItem.toString());
                }
            }
            Expression where = ((PlainSelect) selectBody).getWhere();
            if (where != null) {
                String whereStr = where.toString();
                if (whereStr.contains("(") && whereStr.contains(")")) {
                    int firstIndex = whereStr.indexOf("(");
                    int lastIndex = whereStr.lastIndexOf(")");
                    CharSequence charSequence = whereStr.subSequence(firstIndex, lastIndex + 1);
                    map.put("whereSubselect", charSequence.toString());
                }
            }
            FromItem fromItem = ((PlainSelect) selectBody).getFromItem();
            if (fromItem instanceof SubSelect) {
                map.put("fromItemSubselect", fromItem.toString());
            }
        } else if (selectBody instanceof WithItem) {

        }
        return map;
    }

    public static boolean isMultiSubSelect(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            FromItem fromItem = ((PlainSelect) selectBody).getFromItem();
            if (fromItem instanceof SubSelect) {
                SelectBody subBody = ((SubSelect) fromItem).getSelectBody();
                if (subBody instanceof PlainSelect) {
                    FromItem subFromItem = ((PlainSelect) subBody).getFromItem();
                    if (subFromItem instanceof SubSelect) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) throws JSQLParserException {
        String sql = "CREATE TABLE `party_build` (\n" + "  `school_code` varchar(50) NOT NULL COMMENT '主键,学校代码',\n" + "  `school_year` varchar(50) NOT NULL COMMENT '学年,如:2020-2021表示2020到2021学年',\n" + "  `term` int(11) NOT NULL COMMENT '学期,如:1为上学期,2为下学期',\n" + "  `sub_party_organization_count` int(11) NOT NULL COMMENT '下级党组织个数',\n" + "  `party_member_num` int(11) NOT NULL COMMENT '党员人数',\n" + "  `party_life_times` int(11) NOT NULL COMMENT '党组织生活次数',\n" + "  `party_activity_times` int(11) NOT NULL COMMENT '党建活动次数',\n" + "  PRIMARY KEY (`school_code`) USING BTREE\n" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='党建党风概况';";
        Statement stmt = CCJSqlParserUtil.parse(sql);
        CreateTable createTable = (CreateTable) stmt;

        final List<ColumnDefinition> columnDefinitions = createTable.getColumnDefinitions();

        for (ColumnDefinition columnDefinition : columnDefinitions) {
            final ColDataType colDataType = columnDefinition.getColDataType();
            System.out.println(colDataType.getDataType());
            System.out.println(colDataType.getArrayData());
            System.out.println(colDataType.getArgumentsStringList());
            System.out.println(colDataType.getCharacterSet());
            System.out.println("\n");
        }
    }
}
