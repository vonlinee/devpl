package org.apache.ddlutils.alteration;

import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

/**
 * Base class for change implementations.
 */
public abstract class TableChangeBase implements TableChange {
    /**
     * The name of the affected table.
     */
    private final String _tableName;

    /**
     * Creates a new change object.
     *
     * @param tableName The table's name
     */
    public TableChangeBase(String tableName) {
        _tableName = tableName;
    }

    @Override
    public String getChangedTableName() {
        return _tableName;
    }

    @Override
    public Table findChangedTable(Database model, boolean caseSensitive) {
        return model.findTable(_tableName, caseSensitive);
    }
}
