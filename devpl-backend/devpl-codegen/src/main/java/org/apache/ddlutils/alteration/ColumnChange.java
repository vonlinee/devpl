package org.apache.ddlutils.alteration;


import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;

/**
 * Represents a change to a column of a table.
 */
public interface ColumnChange extends TableChange {
    /**
     * Returns the name of the affected column from the original model.
     *
     * @return The name of the affected column
     */
    String getChangedColumn();

    /**
     * Finds the column object corresponding to the changed column in the given database model.
     *
     * @param model         The database model
     * @param caseSensitive Whether identifiers are case-sensitive
     * @return The column object or <code>null</code> if it could not be found
     */
    Column findChangedColumn(Database model, boolean caseSensitive);
}
