package org.apache.ddlutils.alteration;

import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

/**
 * Represents the addition of a primary key to a table which does not have one.
 */
public class AddPrimaryKeyChange extends TableChangeBase {
    /**
     * The names of the columns making up the primary key.
     */
    private final String[] _primaryKeyColumns;

    /**
     * Creates a new change object.
     *
     * @param tableName         The name of the table to add the primary key to
     * @param primaryKeyColumns The names of the columns making up the primary key
     */
    public AddPrimaryKeyChange(String tableName, String[] primaryKeyColumns) {
        super(tableName);
        if (primaryKeyColumns == null) {
            _primaryKeyColumns = new String[0];
        } else {
            _primaryKeyColumns = new String[primaryKeyColumns.length];

            System.arraycopy(primaryKeyColumns, 0, _primaryKeyColumns, 0, primaryKeyColumns.length);
        }
    }

    /**
     * Returns the primary key column names making up the new primary key.
     *
     * @return The primary key column names
     */
    public String[] getPrimaryKeyColumns() {
        String[] result = new String[_primaryKeyColumns.length];

        System.arraycopy(_primaryKeyColumns, 0, result, 0, _primaryKeyColumns.length);
        return result;
    }

    @Override
    public void apply(Database model, boolean caseSensitive) {
        Table table = findChangedTable(model, caseSensitive);
        for (String primaryKeyColumn : _primaryKeyColumns) {
            Column column = table.findColumn(primaryKeyColumn, caseSensitive);
            column.setPrimaryKey(true);
        }
    }
}
