package org.apache.ddlutils.platform.postgresql;


import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.ColumnDefinitionChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.util.ContextMap;

import java.io.IOException;

/**
 * The SQL Builder for PostgresSql.
 */
public class PostgreSqlBuilder extends SqlBuilder {
    /**
     * Creates a new builder instance.
     *
     * @param platform The platform this builder belongs to
     */
    public PostgreSqlBuilder(Platform platform) {
        super(platform);
        // we need to handle the backslash first otherwise the other
        // already escaped sequences would be affected
        addEscapedCharSequence("\\", "\\\\");
        addEscapedCharSequence("'", "\\'");
        addEscapedCharSequence("\b", "\\b");
        addEscapedCharSequence("\f", "\\f");
        addEscapedCharSequence("\n", "\\n");
        addEscapedCharSequence("\r", "\\r");
        addEscapedCharSequence("\t", "\\t");
    }

    @Override
    public void dropTable(Table table) throws IOException {
        print("DROP TABLE ");
        printIdentifier(getTableName(table));
        print(" CASCADE");
        printEndOfStatement();

        Column[] columns = table.getAutoIncrementColumns();

        for (Column column : columns) {
            dropAutoIncrementSequence(table, column);
        }
    }

    @Override
    public void dropIndex(Table table, Index index) throws IOException {
        print("DROP INDEX ");
        printIdentifier(getIndexName(index));
        printEndOfStatement();
    }

    @Override
    public void createTable(Database database, Table table, ContextMap parameters) throws IOException {
        for (int idx = 0; idx < table.getColumnCount(); idx++) {
            Column column = table.getColumn(idx);

            if (column.isAutoIncrement()) {
                createAutoIncrementSequence(table, column);
            }
        }
        super.createTable(database, table, parameters);
    }

    /**
     * Creates the auto-increment sequence that is then used in the column.
     *
     * @param table  The table
     * @param column The column
     */
    private void createAutoIncrementSequence(Table table, Column column) throws IOException {
        print("CREATE SEQUENCE ");
        printIdentifier(getConstraintName(null, table, column.getName(), "seq"));
        printEndOfStatement();
    }

    /**
     * Creates the auto-increment sequence that is then used in the column.
     *
     * @param table  The table
     * @param column The column
     */
    private void dropAutoIncrementSequence(Table table, Column column) throws IOException {
        print("DROP SEQUENCE ");
        printIdentifier(getConstraintName(null, table, column.getName(), "seq"));
        printEndOfStatement();
    }

    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
        print("UNIQUE DEFAULT nextval('");
        printIdentifier(getConstraintName(null, table, column.getName(), "seq"));
        print("')");
    }

    @Override
    public String getSelectLastIdentityValues(Table table) {
        Column[] columns = table.getAutoIncrementColumns();

        if (columns.length == 0) {
            return null;
        } else {
            StringBuilder result = new StringBuilder();

            result.append("SELECT ");
            for (int idx = 0; idx < columns.length; idx++) {
                if (idx > 0) {
                    result.append(", ");
                }
                result.append("currval('");
                result.append(getDelimitedIdentifier(getConstraintName(null, table, columns[idx].getName(), "seq")));
                result.append("') AS ");
                result.append(getDelimitedIdentifier(columns[idx].getName()));
            }
            return result.toString();
        }
    }

    @Override
    public void addColumn(Database model, Table table, Column newColumn) throws IOException {
        if (newColumn.isAutoIncrement()) {
            createAutoIncrementSequence(table, newColumn);
        }
        super.addColumn(model, table, newColumn);
    }

    /**
     * Writes the SQL to drop a column.
     *
     * @param table  The table
     * @param column The column to drop
     */
    public void dropColumn(Table table, Column column) throws IOException {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("DROP COLUMN ");
        printIdentifier(getColumnName(column));
        printEndOfStatement();
        if (column.isAutoIncrement()) {
            dropAutoIncrementSequence(table, column);
        }
    }

    @Override
    protected void writeCastExpression(Column sourceColumn, Column targetColumn) throws IOException {
        boolean sizeChanged = ColumnDefinitionChange.isSizeChanged(getPlatformInfo(), sourceColumn, targetColumn);
        boolean typeChanged = ColumnDefinitionChange.isTypeChanged(getPlatformInfo(), sourceColumn, targetColumn);

        if (sizeChanged || typeChanged) {
            print("CAST(");
            printIdentifier(getColumnName(sourceColumn));
            print(" AS ");
            print(getSqlType(targetColumn));
            print(")");
        } else {
            printIdentifier(getColumnName(sourceColumn));
        }
    }
}
