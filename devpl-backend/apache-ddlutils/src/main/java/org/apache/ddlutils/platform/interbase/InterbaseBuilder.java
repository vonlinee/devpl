package org.apache.ddlutils.platform.interbase;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.util.ContextMap;

import java.io.IOException;
import java.sql.Types;

/**
 * The SQL Builder for the Interbase database.
 */
public class InterbaseBuilder extends SqlBuilder {
    /**
     * Creates a new builder instance.
     *
     * @param platform The plaftform this builder belongs to
     */
    public InterbaseBuilder(Platform platform) {
        super(platform);
        addEscapedCharSequence("'", "''");
    }

    @Override
    public void createTable(Database database, Table table, ContextMap parameters) throws IOException {
        super.createTable(database, table, parameters);

        // creating generator and trigger for auto-increment
        Column[] columns = table.getAutoIncrementColumns();
        for (Column column : columns) {
            writeAutoIncrementCreateStmts(database, table, column);
        }
    }

    @Override
    protected String getNativeDefaultValue(Column column) {
        if ((column.getTypeCode() == Types.BIT) || (column.getTypeCode() == Types.BOOLEAN)) {
            return getDefaultValueHelper().convert(column.getDefaultValue(), column.getTypeCode(), Types.SMALLINT);
        } else {
            return super.getNativeDefaultValue(column);
        }
    }

    @Override
    public void dropTable(Table table) throws IOException {
        // dropping generators for auto-increment
        Column[] columns = table.getAutoIncrementColumns();
        for (Column column : columns) {
            writeAutoIncrementDropStmts(table, column);
        }
        super.dropTable(table);
    }

    @Override
    public void dropIndex(Table table, Index index) throws IOException {
        // Index names in Interbase are unique to a schema and hence we do not
        // need the ON <tablename> clause
        print("DROP INDEX ");
        printIdentifier(getIndexName(index));
        printEndOfStatement();
    }

    /**
     * Writes the creation statements to make the given column an auto-increment column.
     *
     * @param database The database model
     * @param table    The table
     * @param column   The column to make auto-increment
     */
    private void writeAutoIncrementCreateStmts(Database database, Table table, Column column) throws IOException {
        print("CREATE GENERATOR ");
        printIdentifier(getGeneratorName(table, column));
        printEndOfStatement();

        print("CREATE TRIGGER ");
        printIdentifier(getTriggerName(table, column));
        print(" FOR ");
        printlnIdentifier(getTableName(table));
        println("ACTIVE BEFORE INSERT POSITION 0 AS");
        print("BEGIN IF (NEW.");
        printIdentifier(getColumnName(column));
        print(" IS NULL) THEN NEW.");
        printIdentifier(getColumnName(column));
        print(" = GEN_ID(");
        printIdentifier(getGeneratorName(table, column));
        print(", 1); END");
        printEndOfStatement();
    }

    /**
     * Writes the statements to drop the auto-increment status for the given column.
     *
     * @param table  The table
     * @param column The column to remove the auto-increment status for
     */
    private void writeAutoIncrementDropStmts(Table table, Column column) throws IOException {
        print("DROP TRIGGER ");
        printIdentifier(getTriggerName(table, column));
        printEndOfStatement();

        print("DROP GENERATOR ");
        printIdentifier(getGeneratorName(table, column));
        printEndOfStatement();
    }

    /**
     * Determines the name of the trigger for an auto-increment column.
     *
     * @param table  The table
     * @param column The auto-increment column
     * @return The trigger name
     */
    protected String getTriggerName(Table table, Column column) {
        return getConstraintName("trg", table, column.getName(), null);
    }

    /**
     * Determines the name of the generator for an auto-increment column.
     *
     * @param table  The table
     * @param column The auto-increment column
     * @return The generator name
     */
    protected String getGeneratorName(Table table, Column column) {
        return getConstraintName("gen", table, column.getName(), null);
    }

    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
        // we're using a generator
    }

    @Override
    public String getSelectLastIdentityValues(Table table) {
        Column[] columns = table.getAutoIncrementColumns();

        if (columns.length == 0) {
            return null;
        } else {
            StringBuilder result = new StringBuilder();

            result.append("SELECT ");
            for (Column column : columns) {
                result.append("GEN_ID(");
                result.append(getDelimitedIdentifier(getGeneratorName(table, column)));
                result.append(", 0)");
            }
            result.append(" FROM RDB$DATABASE");
            return result.toString();
        }
    }

    /**
     * Writes the SQL to add/insert a column.
     *
     * @param model      The database model
     * @param table      The table
     * @param newColumn  The new column
     * @param prevColumn The column after which the new column shall be added; <code>null</code>
     *                   if the new column is to be inserted at the beginning
     */
    public void insertColumn(Database model, Table table, Column newColumn, Column prevColumn) throws IOException {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("ADD ");
        writeColumn(table, newColumn);
        printEndOfStatement();

        if (prevColumn != null) {
            // Even though Interbase can only add columns, we can move them later on
            print("ALTER TABLE ");
            printlnIdentifier(getTableName(table));
            printIndent();
            print("ALTER ");
            printIdentifier(getColumnName(newColumn));
            print(" POSITION ");
            // column positions start at 1 in Interbase
            print(String.valueOf(table.getColumnIndex(prevColumn) + 2));
            printEndOfStatement();
        }
        if (newColumn.isAutoIncrement()) {
            writeAutoIncrementCreateStmts(model, table, newColumn);
        }
    }

    /**
     * Writes the SQL to drop a column.
     *
     * @param table  The table
     * @param column The column to drop
     */
    public void dropColumn(Table table, Column column) throws IOException {
        if (column.isAutoIncrement()) {
            writeAutoIncrementDropStmts(table, column);
        }
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("DROP ");
        printIdentifier(getColumnName(column));
        printEndOfStatement();
    }
}
