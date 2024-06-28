package org.apache.ddlutils.alteration;

import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

/**
 * Represents the removal of a table from a model.
 * <p>
 * TODO: this should be a model change
 */
public class RemoveTableChange extends TableChangeBase {
    /**
     * Creates a new change object.
     *
     * @param tableName The name of the table to be removed
     */
    public RemoveTableChange(String tableName) {
        super(tableName);
    }

    @Override
    public void apply(Database model, boolean caseSensitive) {
        Table table = findChangedTable(model, caseSensitive);
        model.removeTable(table);
    }
}
