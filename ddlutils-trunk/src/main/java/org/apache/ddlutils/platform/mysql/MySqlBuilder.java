package org.apache.ddlutils.platform.mysql;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.alteration.ColumnDefinitionChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.util.StringUtils;

import java.io.IOException;
import java.sql.Types;
import java.util.Iterator;
import java.util.Map;

/**
 * The SQL Builder for MySQL.
 * @version $Revision$
 */
public class MySqlBuilder extends SqlBuilder {
    /**
     * Creates a new builder instance.
     * @param platform The plaftform this builder belongs to
     */
    public MySqlBuilder(DatabasePlatform platform) {
        super(platform);
        // we need to handle the backslash first otherwise the other
        // already escaped sequences would be affected
        addEscapedCharSequence("\\", "\\\\");
        addEscapedCharSequence("\0", "\\0");
        addEscapedCharSequence("'", "\\'");
        addEscapedCharSequence("\"", "\\\"");
        addEscapedCharSequence("\b", "\\b");
        addEscapedCharSequence("\n", "\\n");
        addEscapedCharSequence("\r", "\\r");
        addEscapedCharSequence("\t", "\\t");
        addEscapedCharSequence("\u001A", "\\Z");
        addEscapedCharSequence("%", "\\%");
        addEscapedCharSequence("_", "\\_");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dropTable(Table table) throws IOException {
        print("DROP TABLE IF EXISTS ");
        printIdentifier(getTableName(table));
        printEndOfStatement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
        print("AUTO_INCREMENT");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean shouldGeneratePrimaryKeys(Column[] primaryKeyColumns) {
        // mySQL requires primary key indication for autoincrement key columns
        // I'm not sure why the default skips the pk statement if all are identity
        return true;
    }

    /**
     * {@inheritDoc}
     * Normally mysql will return the LAST_INSERT_ID as the column name for the inserted id.
     * Since ddlutils expects the real column name of the field that is autoincrementing, the
     * column has an alias of that column name.
     */
    @Override
    public String getSelectLastIdentityValues(Table table) {
        String autoIncrementKeyName = "";
        if (table.getAutoIncrementColumns().length > 0) {
            autoIncrementKeyName = table.getAutoIncrementColumns()[0].getName();
        }
        return "SELECT LAST_INSERT_ID() " + autoIncrementKeyName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeTableCreationStmtEnding(Table table, Map<String, Object> parameters) throws IOException {
        if (parameters != null) {
            print(" ");
            // MySql supports additional table creation options which are appended
            // at the end of the CREATE TABLE statement
            for (Iterator<Map.Entry<String, Object>> it = parameters.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> entry = it.next();

                print(entry.getKey());
                if (entry.getValue() != null) {
                    print("=");
                    print(entry.getValue().toString());
                }
                if (it.hasNext()) {
                    print(" ");
                }
            }
        }
        super.writeTableCreationStmtEnding(table, parameters);
    }

    @Override
    protected String getSqlType(Column column) {
        final String sqlType = super.getSqlType(column);
        System.out.println(column.getName() + " " + sqlType);
        return sqlType;
    }

    @Override
    protected void writeColumn(Table table, Column column) throws IOException {
        super.writeColumn(table, column);
        if (!StringUtils.isEmpty(column.getDescription())) {
            print(" COMMENT '" + column.getDescription() + "'");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dropForeignKey(Table table, ForeignKey foreignKey) throws IOException {
        writeTableAlterStmt(table);
        print("DROP FOREIGN KEY ");
        printIdentifier(getForeignKeyName(table, foreignKey));
        printEndOfStatement();

        // InnoDB won't drop the auto-index for the foreign key automatically, so we have to do it
        if (foreignKey.isAutoIndexPresent()) {
            writeTableAlterStmt(table);
            print("DROP INDEX ");
            printIdentifier(getForeignKeyName(table, foreignKey));
            printEndOfStatement();
        }
    }

    /**
     * Writes the SQL to add/insert a column.
     * @param table      The table
     * @param newColumn  The new column
     * @param prevColumn The column after which the new column shall be added; <code>null</code>
     *                   if the new column is to be inserted at the beginning
     */
    public void insertColumn(Table table, Column newColumn, Column prevColumn) throws IOException {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("ADD COLUMN ");
        writeColumn(table, newColumn);
        if (prevColumn != null) {
            print(" AFTER ");
            printIdentifier(getColumnName(prevColumn));
        } else {
            print(" FIRST");
        }
        printEndOfStatement();
    }

    /**
     * Writes the SQL to drop a column.
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
    }

    /**
     * Writes the SQL to drop the primary key of the given table.
     * @param table The table
     */
    public void dropPrimaryKey(Table table) throws IOException {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("DROP PRIMARY KEY");
        printEndOfStatement();
    }

    /**
     * Writes the SQL to recreate a column, e.g. with a different type.
     * @param table  The table
     * @param column The new column definition
     */
    public void recreateColumn(Table table, Column column) throws IOException {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("MODIFY COLUMN ");
        writeColumn(table, column);
        printEndOfStatement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeCastExpression(Column sourceColumn, Column targetColumn) throws IOException {
        boolean sizeChanged = ColumnDefinitionChange.isSizeChanged(getPlatformInfo(), sourceColumn, targetColumn);
        boolean typeChanged = ColumnDefinitionChange.isTypeChanged(getPlatformInfo(), sourceColumn, targetColumn);

        if (sizeChanged || typeChanged) {
            String targetNativeType = getNativeType(targetColumn);

            switch (targetColumn.getJdbcTypeCode()) {
                case Types.BIT:
                case Types.BOOLEAN:
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER:
                case Types.BIGINT:
                    targetNativeType = "SIGNED";
                    break;
                case Types.FLOAT:
                case Types.REAL:
                case Types.DOUBLE:
                    targetNativeType = "SIGNED"; // ?
                    break;
                case Types.DECIMAL:
                case Types.NUMERIC:
                    targetNativeType = "DECIMAL";
                    break;
                case Types.DATE:
                    targetNativeType = "DATE";
                    break;
                case Types.TIMESTAMP:
                    targetNativeType = "DATETIME";
                    break;
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                case Types.CLOB:
                    targetNativeType = "CHAR";
                    break;
                default:
                    targetNativeType = "BINARY";
                    break;
            }

            print("CAST(");
            if (TypeMap.isTextType(sourceColumn.getJdbcTypeCode()) && TypeMap.isTextType(targetColumn.getJdbcTypeCode()) && sizeChanged) {
                print("LEFT(");
                printIdentifier(getColumnName(sourceColumn));
                print(",");
                print(targetColumn.getSize());
                print(")");
            } else {
                printIdentifier(getColumnName(sourceColumn));
            }
            print(" AS ");
            print(getSqlType(targetColumn, targetNativeType));
            print(")");
        } else {
            printIdentifier(getColumnName(sourceColumn));
        }
    }
}
