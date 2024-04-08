package org.apache.ddlutils.alteration;


import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

/**
 * Represents the removal of the primary key from a table.
 */
public class RemovePrimaryKeyChange extends TableChangeImplBase {
    /**
     * Creates a new change object.
     *
     * @param tableName The name of the table to remove the primary key from
     */
    public RemovePrimaryKeyChange(String tableName) {
        super(tableName);
    }

    @Override
    public void apply(Database model, boolean caseSensitive) {
        Table table = findChangedTable(model, caseSensitive);
        Column[] pkCols = table.getPrimaryKeyColumns();

        for (Column pkCol : pkCols) {
            pkCol.setPrimaryKey(false);
        }
    }
}
