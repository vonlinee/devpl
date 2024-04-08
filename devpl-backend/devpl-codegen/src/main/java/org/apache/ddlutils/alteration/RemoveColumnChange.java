package org.apache.ddlutils.alteration;


import org.apache.ddlutils.model.Database;

/**
 * Represents the removal of a column from a table.
 */
public class RemoveColumnChange extends ColumnChangeImplBase {
    /**
     * Creates a new change object.
     *
     * @param tableName  The name of the table to remove the column from
     * @param columnName The column's name
     */
    public RemoveColumnChange(String tableName, String columnName) {
        super(tableName, columnName);
    }

    @Override
    public void apply(Database model, boolean caseSensitive) {
        findChangedTable(model, caseSensitive).removeColumn(findChangedColumn(model, caseSensitive));
    }
}
