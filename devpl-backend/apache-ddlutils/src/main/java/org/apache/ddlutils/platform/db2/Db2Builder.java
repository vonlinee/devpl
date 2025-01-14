package org.apache.ddlutils.platform.db2;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.ColumnDefinitionChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.SqlBuilder;

import java.io.IOException;
import java.sql.Types;

/**
 * The SQL Builder for DB2.
 */
public class Db2Builder extends SqlBuilder {
    /**
     * Creates a new builder instance.
     *
     * @param platform The plaftform this builder belongs to
     */
    public Db2Builder(Platform platform) {
        super(platform);
        addEscapedCharSequence("'", "''");
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
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
        print("GENERATED BY DEFAULT AS IDENTITY");
    }

    @Override
    public String getSelectLastIdentityValues(Table table) {
        return "VALUES IDENTITY_VAL_LOCAL()";
    }

    /**
     * Generates the SQL to drop a column from a table.
     *
     * @param table  The table where to drop the column from
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
     * Writes the SQL for dropping the primary key of the given table.
     *
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
    public void dropIndex(Table table, Index index) throws IOException {
        // Index names in DB2 are unique to a schema and hence Derby does not
        // use the ON <tablename> clause
        print("DROP INDEX ");
        printIdentifier(getIndexName(index));
        printEndOfStatement();
    }

    @Override
    protected void writeCastExpression(Column sourceColumn, Column targetColumn) throws IOException {
        String sourceNativeType = getBareNativeType(sourceColumn);
        String targetNativeType = getBareNativeType(targetColumn);

        if (sourceNativeType.equals(targetNativeType) &&
            !ColumnDefinitionChange.isSizeChanged(getPlatformInfo(), sourceColumn, targetColumn)) {
            printIdentifier(getColumnName(sourceColumn));
        } else {
            String type = getSqlType(targetColumn);

            // DB2 has the limitation that it cannot convert numeric values
            // to VARCHAR, though it can convert them to CHAR
            if (TypeMap.isNumericType(sourceColumn.getTypeCode()) &&
                "VARCHAR".equalsIgnoreCase(targetNativeType)) {
                Object sizeSpec = targetColumn.getSize();

                if (sizeSpec == null) {
                    sizeSpec = getPlatformInfo().getDefaultSize(targetColumn.getTypeCode());
                }
                type = "CHAR(" + sizeSpec.toString() + ")";
            }

            print("CAST(");
            printIdentifier(getColumnName(sourceColumn));
            print(" AS ");
            print(type);
            print(")");
        }
    }
}
