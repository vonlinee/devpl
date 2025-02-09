package org.apache.ddlutils.alteration;

import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

/**
 * Base class for changes to columns.
 */
public abstract class ColumnChangeBase extends TableChangeBase implements ColumnChange {
    /**
     * The column's name.
     */
    private final String _columnName;

    /**
     * Creates a new change object.
     *
     * @param tableName  The name of the table to remove the column from
     * @param columnName The column's name
     */
    public ColumnChangeBase(String tableName, String columnName) {
        super(tableName);
        _columnName = columnName;
    }

    @Override
    public String getChangedColumn() {
        return _columnName;
    }

    @Override
    public Column findChangedColumn(Database model, boolean caseSensitive) {
        Table table = findChangedTable(model, caseSensitive);
        return table == null ? null : table.findColumn(_columnName, caseSensitive);
    }
}
