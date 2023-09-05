package org.apache.ddlutils.platform.oracle;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.alteration.ColumnDefinitionChange;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.util.StringUtils;
import org.apache.ddlutils.util.ValueMap;

import java.io.IOException;
import java.sql.Types;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * The SQL Builder for Oracle.
 * @version $Revision$
 */
public class Oracle8Builder extends SqlBuilder {
    /**
     * The regular expression pattern for ISO dates, i.e. 'YYYY-MM-DD'.
     */
    private final Pattern _isoDatePattern;
    /**
     * The regular expression pattern for ISO times, i.e. 'HH:MI:SS'.
     */
    private final Pattern _isoTimePattern;
    /**
     * The regular expression pattern for ISO timestamps, i.e. 'YYYY-MM-DD HH:MI:SS.fffffffff'.
     */
    private final Pattern _isoTimestampPattern;

    /**
     * Creates a new builder instance.
     * @param platform The plaftform this builder belongs to
     */
    public Oracle8Builder(DatabasePlatform platform) {
        super(platform);
        addEscapedCharSequence("'", "''");

        try {
            _isoDatePattern = Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2}");
            _isoTimePattern = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
            _isoTimestampPattern = Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}[\\.\\d{1,8}]?");
        } catch (PatternSyntaxException ex) {
            throw new DdlUtilsException(ex);
        }
    }


    @Override
    public void createTable(Database database, Table table, ValueMap parameters) throws IOException {
        // lets create any sequences
        Column[] columns = table.getAutoIncrementColumns();

        for (Column value : columns) {
            createAutoIncrementSequence(table, value);
        }

        super.createTable(database, table, parameters);

        for (Column column : columns) {
            createAutoIncrementTrigger(table, column);
        }
    }


    @Override
    public void dropTable(Table table) throws IOException {
        Column[] columns = table.getAutoIncrementColumns();

        for (Column column : columns) {
            dropAutoIncrementTrigger(table, column);
            dropAutoIncrementSequence(table, column);
        }

        print("DROP TABLE ");
        printIdentifier(getTableName(table));
        print(" CASCADE CONSTRAINTS");
        printEndOfStatement();
    }

    /**
     * Creates the sequence necessary for the auto-increment of the given column.
     * @param table  The table
     * @param column The column
     */
    protected void createAutoIncrementSequence(Table table,
                                               Column column) throws IOException {
        print("CREATE SEQUENCE ");
        printIdentifier(getConstraintName("seq", table, column.getName(), null));
        printEndOfStatement();
    }

    /**
     * Creates the trigger necessary for the auto-increment of the given column.
     * @param table  The table
     * @param column The column
     */
    protected void createAutoIncrementTrigger(Table table,
                                              Column column) throws IOException {
        String columnName = getColumnName(column);
        String triggerName = getConstraintName("trg", table, column.getName(), null);

        if (getPlatform().isScriptModeOn()) {
            // For the script, we output a more nicely formatted version
            print("CREATE OR REPLACE TRIGGER ");
            printlnIdentifier(triggerName);
            print("BEFORE INSERT ON ");
            printlnIdentifier(getTableName(table));
            print("FOR EACH ROW WHEN (new.");
            printIdentifier(columnName);
            println(" IS NULL)");
            println("BEGIN");
            print("  SELECT ");
            printIdentifier(getConstraintName("seq", table, column.getName(), null));
            print(".nextval INTO :new.");
            printIdentifier(columnName);
            print(" FROM dual");
            println(getPlatformInfo().getSqlCommandDelimiter());
            print("END");
            println(getPlatformInfo().getSqlCommandDelimiter());
            println("/");
            println();
        } else {
            // note that the BEGIN ... SELECT ... END; is all in one line and does
            // not contain a semicolon except for the END-one
            // this way, the tokenizer will not split the statement before the END
            print("CREATE OR REPLACE TRIGGER ");
            printIdentifier(triggerName);
            print(" BEFORE INSERT ON ");
            printIdentifier(getTableName(table));
            print(" FOR EACH ROW WHEN (new.");
            printIdentifier(columnName);
            println(" IS NULL)");
            print("BEGIN SELECT ");
            printIdentifier(getConstraintName("seq", table, column.getName(), null));
            print(".nextval INTO :new.");
            printIdentifier(columnName);
            print(" FROM dual");
            print(getPlatformInfo().getSqlCommandDelimiter());
            print(" END");
            // It is important that there is a semicolon at the end of the statement (or more
            // precisely, at the end of the PL/SQL block), and thus we put two semicolons here
            // because the tokenizer will remove the one at the end
            print(getPlatformInfo().getSqlCommandDelimiter());
            printEndOfStatement();
        }
    }

    /**
     * Drops the sequence used for the auto-increment of the given column.
     * @param table  The table
     * @param column The column
     */
    protected void dropAutoIncrementSequence(Table table,
                                             Column column) throws IOException {
        print("DROP SEQUENCE ");
        printIdentifier(getConstraintName("seq", table, column.getName(), null));
        printEndOfStatement();
    }

    /**
     * Drops the trigger used for the auto-increment of the given column.
     * @param table  The table
     * @param column The column
     */
    protected void dropAutoIncrementTrigger(Table table,
                                            Column column) throws IOException {
        print("DROP TRIGGER ");
        printIdentifier(getConstraintName("trg", table, column.getName(), null));
        printEndOfStatement();
    }


    @Override
    protected void createTemporaryTable(Database database, Table table, ValueMap parameters) throws IOException {
        createTable(database, table, parameters);
    }


    @Override
    protected void dropTemporaryTable(Database database, Table table) throws IOException {
        dropTable(table);
    }


    @Override
    public void dropForeignKeys(Table table) throws IOException {
        // no need to as we drop the table with CASCASE CONSTRAINTS
    }


    @Override
    public void dropIndex(Table table, Index index) throws IOException {
        // Index names in Oracle are unique to a schema and hence Oracle does not
        // use the ON <tablename> clause
        print("DROP INDEX ");
        printIdentifier(getIndexName(index));
        printEndOfStatement();
    }


    @Override
    protected void printDefaultValue(Object defaultValue, int typeCode) throws IOException {
        if (defaultValue != null) {
            String defaultValueStr = defaultValue.toString();
            boolean shouldUseQuotes = !TypeMap.isNumericType(typeCode) && !defaultValueStr.startsWith("TO_DATE(");

            if (shouldUseQuotes) {
                // characters are only escaped when within a string literal
                print(getPlatformInfo().getValueQuoteToken());
                print(escapeStringValue(defaultValueStr));
                print(getPlatformInfo().getValueQuoteToken());
            } else {
                print(defaultValueStr);
            }
        }
    }


    @Override
    protected String getNativeDefaultValue(Column column) {
        if ((column.getJdbcTypeCode() == Types.BIT) || (column.getJdbcTypeCode() == Types.BOOLEAN)) {
            return getDefaultValueHelper().convert(column.getDefaultValue(), column.getJdbcTypeCode(), Types.SMALLINT);
        }
        // Oracle does not accept ISO formats, so we have to convert an ISO spec if we find one
        // But these are the only formats that we make sure work, every other format has to be database-dependent
        // and thus the user has to ensure that it is correct
        else if (column.getJdbcTypeCode() == Types.DATE) {
            if (_isoDatePattern.matcher(column.getDefaultValue()).matches()) {
                return "TO_DATE('" + column.getDefaultValue() + "', 'YYYY-MM-DD')";
            }
        } else if (column.getJdbcTypeCode() == Types.TIME) {
            if (_isoTimePattern.matcher(column.getDefaultValue()).matches()) {
                return "TO_DATE('" + column.getDefaultValue() + "', 'HH24:MI:SS')";
            }
        } else if (column.getJdbcTypeCode() == Types.TIMESTAMP) {
            if (_isoTimestampPattern.matcher(column.getDefaultValue()).matches()) {
                return "TO_DATE('" + column.getDefaultValue() + "', 'YYYY-MM-DD HH24:MI:SS')";
            }
        }
        return super.getNativeDefaultValue(column);
    }


    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
        // we're using sequences instead
    }


    @Override
    public String getSelectLastIdentityValues(Table table) {
        Column[] columns = table.getAutoIncrementColumns();

        if (columns.length > 0) {
            StringBuilder result = new StringBuilder();

            result.append("SELECT ");
            for (int idx = 0; idx < columns.length; idx++) {
                if (idx > 0) {
                    result.append(",");
                }
                result.append(getDelimitedIdentifier(getConstraintName("seq", table, columns[idx].getName(), null)));
                result.append(".currval");
            }
            result.append(" FROM dual");
            return result.toString();
        } else {
            return null;
        }
    }


    @Override
    public void addColumn(Database model, Table table, Column newColumn) throws IOException {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("ADD ");
        writeColumn(table, newColumn);
        printEndOfStatement();
        if (newColumn.isAutoIncrement()) {
            createAutoIncrementSequence(table, newColumn);
            createAutoIncrementTrigger(table, newColumn);
        }
    }

    /**
     * Writes the SQL to drop a column.
     * @param table  The table
     * @param column The column to drop
     */
    public void dropColumn(Table table, Column column) throws IOException {
        if (column.isAutoIncrement()) {
            dropAutoIncrementTrigger(table, column);
            dropAutoIncrementSequence(table, column);
        }
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


    @Override
    protected void writeCastExpression(Column sourceColumn, Column targetColumn) throws IOException {
        boolean sizeChanged = TypeMap.isTextType(targetColumn.getJdbcTypeCode()) &&
            ColumnDefinitionChange.isSizeChanged(getPlatformInfo(), sourceColumn, targetColumn) &&
            !StringUtils.isEmpty(targetColumn.getSize());

        if (sizeChanged) {
            print("SUBSTR(");
        }
        if (ColumnDefinitionChange.isTypeChanged(getPlatformInfo(), sourceColumn, targetColumn)) {
            print("CAST (");
            printIdentifier(getColumnName(sourceColumn));
            print(" AS ");
            print(getSqlType(targetColumn));
            print(")");
        } else {
            printIdentifier(getColumnName(sourceColumn));
        }
        if (sizeChanged) {
            print(",0,");
            print(targetColumn.getSize());
            print(")");
        }
    }
}
