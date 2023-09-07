package org.apache.ddlutils.platform.firebird;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.alteration.ColumnDefinitionChange;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.util.ObjectMap;

import java.io.IOException;
import java.sql.Types;

/**
 * The SQL Builder for the FireBird database.
 * @version $Revision: 231306 $
 */
public class FirebirdBuilder extends SqlBuilder {
    /**
     * Creates a new builder instance.
     * @param platform The plaftform this builder belongs to
     */
    public FirebirdBuilder(DatabasePlatform platform) {
        super(platform);
        addEscapedCharSequence("'", "''");
    }

    @Override
    public void createTable(Database database, Table table, ObjectMap parameters) throws IOException {
        super.createTable(database, table, parameters);

        // creating generator and trigger for auto-increment
        Column[] columns = table.getAutoIncrementColumns();

        for (Column column : columns) {
            writeAutoIncrementCreateStmts(database, table, column);
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

    /**
     * Writes the creation statements to make the given column an auto-increment column.
     * @param database The database model
     * @param table    The table
     * @param column   The column to make auto-increment
     */
    private void writeAutoIncrementCreateStmts(Database database, Table table, Column column) throws IOException {
        print("CREATE GENERATOR ");
        printIdentifier(getGeneratorName(table, column));
        printEndOfStatement();

        print("CREATE TRIGGER ");
        printIdentifier(getConstraintName("trg", table, column.getName(), null));
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
     * @param table  The table
     * @param column The column to remove the auto-increment status for
     */
    private void writeAutoIncrementDropStmts(Table table, Column column) throws IOException {
        print("DROP TRIGGER ");
        printIdentifier(getConstraintName("trg", table, column.getName(), null));
        printEndOfStatement();

        print("DROP GENERATOR ");
        printIdentifier(getGeneratorName(table, column));
        printEndOfStatement();
    }

    /**
     * Determines the name of the generator for an auto-increment column.
     * @param table  The table
     * @param column The auto-increment column
     * @return The generator name
     */
    protected String getGeneratorName(Table table, Column column) {
        return getConstraintName("gen", table, column.getName(), null);
    }

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
            for (int idx = 0; idx < columns.length; idx++) {
                result.append("GEN_ID(");
                result.append(getDelimitedIdentifier(getGeneratorName(table, columns[idx])));
                result.append(", 0)");
            }
            result.append(" FROM RDB$DATABASE");
            return result.toString();
        }
    }

    protected String getNativeDefaultValue(Column column) {
        if ((column.getJdbcTypeCode() == Types.BIT) || (column.getJdbcTypeCode() == Types.BOOLEAN)) {
            return getDefaultValueHelper().convert(column.getDefaultValue(), column.getJdbcTypeCode(), Types.SMALLINT);
        } else {
            return super.getNativeDefaultValue(column);
        }
    }

    @Override
    public void createForeignKeys(Database database) throws IOException {
        for (int idx = 0; idx < database.getTableCount(); idx++) {
            createForeignKeys(database, database.getTable(idx));
        }
    }

    @Override
    public void dropIndex(Table table, Index index) throws IOException {
        // Index names in Firebird are unique to a schema and hence Firebird does not
        // use the ON <tablename> clause
        print("DROP INDEX ");
        printIdentifier(getIndexName(index));
        printEndOfStatement();
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
            writeAutoIncrementCreateStmts(model, table, newColumn);
        }
    }

    /**
     * Writes the SQL to add/insert a column.
     * @param model      The database model
     * @param table      The table
     * @param newColumn  The new column
     * @param prevColumn The column after which the new column shall be added; <code>null</code>
     *                   if the new column is to be inserted at the beginning
     */
    public void insertColumn(Database model, Table table, Column newColumn, Column prevColumn) throws IOException {
        addColumn(model, table, newColumn);

        // column positions start at 1 in Firebird
        int pos = 1;

        if (prevColumn != null) {
            pos = table.getColumnIndex(prevColumn) + 2;
        }

        // Even though Firebird can only add columns, we can move them later on
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("ALTER ");
        printIdentifier(getColumnName(newColumn));
        print(" POSITION ");
        print(String.valueOf(pos));
        printEndOfStatement();
    }

    /**
     * Writes the SQL to drop a column.
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

    @Override
    protected void writeCastExpression(Column sourceColumn, Column targetColumn) throws IOException {
        boolean sizeChanged = ColumnDefinitionChange.isSizeChanged(getPlatformInfo(), sourceColumn, targetColumn);
        boolean typeChanged = ColumnDefinitionChange.isTypeChanged(getPlatformInfo(), sourceColumn, targetColumn);

        if (sizeChanged || typeChanged) {
            boolean needSubstr = TypeMap.isTextType(targetColumn.getJdbcTypeCode()) && sizeChanged &&
                                 (targetColumn.getSize() != null) && (sourceColumn.getSizeAsInt() > targetColumn.getSizeAsInt());

            if (needSubstr) {
                print("SUBSTRING(");
            }
            // we're not using CAST but instead string construction which does not require us to know the size
            print("(");
            printIdentifier(getColumnName(sourceColumn));
            print(" || '' ");
            if (needSubstr) {
                print(") FROM 1 FOR ");
                print(targetColumn.getSize());
                print(")");
            } else {
                print(")");
            }
        } else {
            super.writeCastExpression(sourceColumn, targetColumn);
        }
    }
}
