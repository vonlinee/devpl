package org.apache.ddlutils.platform.derby;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.ColumnDefinitionChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.cloudscape.CloudscapeBuilder;

import java.io.IOException;
import java.sql.Types;

/**
 * The SQL Builder for Derby.
 *
 * @version $Revision: 279413 $
 */
public class DerbyBuilder extends CloudscapeBuilder {
    /**
     * Creates a new builder instance.
     *
     * @param platform The plaftform this builder belongs to
     */
    public DerbyBuilder(Platform platform) {
        super(platform);
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
    public void dropIndex(Table table, Index index) throws IOException {
        // Index names in Derby are unique to a schema and hence Derby does not
        // use the ON <tablename> clause
        print("DROP INDEX ");
        printIdentifier(getIndexName(index));
        printEndOfStatement();
    }

    @Override
    protected void writeCastExpression(Column sourceColumn, Column targetColumn) throws IOException {
        if (ColumnDefinitionChange.isSizeChanged(getPlatformInfo(), sourceColumn, targetColumn) ||
            ColumnDefinitionChange.isTypeChanged(getPlatformInfo(), sourceColumn, targetColumn)) {
            String targetNativeType = getNativeType(targetColumn);

            // Derby currently has the limitation that it cannot convert numeric values
            // to VARCHAR, though it can convert them to CHAR
            if (TypeMap.isNumericType(sourceColumn.getTypeCode()) &&
                "VARCHAR".equalsIgnoreCase(targetNativeType)) {
                targetNativeType = "CHAR";
            }

            print("CAST (");
            printIdentifier(getColumnName(sourceColumn));
            print(" AS ");
            print(getSqlType(targetColumn, targetNativeType));
            print(")");
        } else {
            printIdentifier(getColumnName(sourceColumn));
        }
    }
}
