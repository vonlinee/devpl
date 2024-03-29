package io.devpl.codegen.parser.sql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUnique;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import io.devpl.codegen.db.IndexInfo;

import java.util.*;
import java.util.stream.Collectors;

public class MySQLColumnVisitor extends MySqlSchemaStatVisitor {

    /**
     * 保存索引信息
     */
    private final List<IndexInfo> indices = new ArrayList<>();

    @Override
    public boolean visit(SQLColumnDefinition x) {
        String tableName = null;
        SQLObject parent = x.getParent();
        if (parent instanceof SQLCreateTableStatement) {
            tableName = SQLUtils.normalize(((SQLCreateTableStatement) parent).getTableName());
        }
        if (Objects.isNull(tableName)) {
            return true;
        }
        String columnName = SQLUtils.normalize(x.getName().toString());
        TableStat.Column column = this.addColumn(tableName, columnName);
        column.setDataType(x.getDataType().getName());
        Map<String, Object> attr = column.getAttributes();
        if (Objects.isNull(attr)) {
            attr = new HashMap<>();
            column.setAttributes(attr);
        }
        // 其他属性
        // attr.put("sqlSegment", x.toString());
        if (Objects.nonNull(x.getComment())) {
            attr.put("comment", SQLUtils.normalize(x.getComment().toString()));
        }
        attr.put("unsigned", ((SQLDataTypeImpl) x.getDataType()).isUnsigned());
        if (Objects.nonNull(x.getDefaultExpr())) {
            attr.put("defaultValue", SQLUtils.normalize(x.getDefaultExpr().toString()));
        }
        List<Object> typeArgs = new ArrayList<>();
        attr.put("typeArgs", typeArgs);
        for (SQLExpr argument : x.getDataType().getArguments()) {
            if (argument instanceof SQLIntegerExpr) {
                Number number = ((SQLIntegerExpr) argument).getNumber();
                typeArgs.add(number);
            }
        }
        for (Object item : x.getConstraints()) {
            if (item instanceof SQLPrimaryKey) {
                column.setPrimaryKey(true);
            } else if (item instanceof SQLUnique) {
                column.setUnique(true);
            } else if (item instanceof SQLNotNullConstraint) {
                attr.put("notnull", true);
            } else if (item instanceof SQLNullConstraint) {
                attr.put("notnull", false);
            }
        }
        return false;
    }

    @Override
    public boolean visit(MySqlKey x) {
        addIndex(x);
        return false;
    }

    @Override
    public boolean visit(MySqlUnique x) {
        addIndex(x);
        return false;
    }

    @Override
    public boolean visit(MySqlPrimaryKey x) {
        addIndex(x);
        return false;
    }


    private void addIndex(MySqlKey x) {
        SQLIndexDefinition indexDefinition = x.getIndexDefinition();
        List<String> indexColumns = indexDefinition.getColumns().stream().map(v -> SQLUtils.normalize(v.getExpr().toString())).collect(Collectors.toList());
        IndexInfo index = new IndexInfo(
            getOrDefault(indexDefinition.getName(), "")
            , getOrDefault(indexDefinition.getType(), "")
            , getOrDefault(indexDefinition.getOptions().getComment(), "")
            , indexColumns);
        this.indices.add(index);
    }

    private String getOrDefault(Object obj, String defaultValue) {
        return Objects.isNull(obj) ? defaultValue : SQLUtils.normalize(String.valueOf(obj));
    }

    /**
     * 获取索引信息
     *
     * @return 索引信息
     */
    public List<IndexInfo> getIndices() {
        return new ArrayList<>(indices);
    }
}

