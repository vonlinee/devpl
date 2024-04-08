package org.apache.ddlutils.alteration;


import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

/**
 * Represents a change to a table or sub-element of a table (e.g. a column).
 */
public interface TableChange extends ModelChange {
    /**
     * Returns the name of the affected table from the original model.
     *
     * @return The name of the affected table
     */
    String getChangedTableName();

    /**
     * Finds the table object corresponding to the changed table in the given database model.
     *
     * @param model         The database model
     * @param caseSensitive Whether identifiers are case-sensitive
     * @return The table object or <code>null</code> if it could not be found
     */
    Table findChangedTable(Database model, boolean caseSensitive);
}
