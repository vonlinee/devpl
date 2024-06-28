package org.apache.ddlutils.alteration;

import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ForeignKey;

/**
 * Represents the removal of a foreign key from a table. Note that for
 * simplicity and because it fits the model, this change actually implements
 * table change for the table that the foreign key originates.
 */
public class RemoveForeignKeyChange extends ForeignKeyChangeBase {
    /**
     * Creates a new change object.
     *
     * @param tableName  The name of the table to remove the foreign key from
     * @param foreignKey The foreign key
     */
    public RemoveForeignKeyChange(String tableName, ForeignKey foreignKey) {
        super(tableName, foreignKey);
    }

    @Override
    public void apply(Database model, boolean caseSensitive) {
        findChangedTable(model, caseSensitive).removeForeignKey(findChangedForeignKey(model, caseSensitive));
    }
}
