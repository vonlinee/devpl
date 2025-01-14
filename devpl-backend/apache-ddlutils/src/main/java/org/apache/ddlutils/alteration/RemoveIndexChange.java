package org.apache.ddlutils.alteration;

import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;

/**
 * Represents the removal of an index from a table.
 */
public class RemoveIndexChange extends IndexChangeBase {
    /**
     * Creates a new change object.
     *
     * @param tableName The name of the table to remove the index from
     * @param index     The index
     */
    public RemoveIndexChange(String tableName, Index index) {
        super(tableName, index);
    }

    @Override
    public void apply(Database model, boolean caseSensitive) {
        findChangedTable(model, caseSensitive).removeIndex(findChangedIndex(model, caseSensitive));
    }
}
