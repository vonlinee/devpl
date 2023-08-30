package org.apache.ddlutils.alteration;

import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

/**
 * Base class for change implementations.
 * @version $Revision: $
 */
public abstract class TableChangeImplBase implements TableChange {
    /**
     * The name of the affected table.
     */
    private final String tableName;

    /**
     * Creates a new change object.
     * @param tableName The table's name
     */
    public TableChangeImplBase(String tableName) {
        this.tableName = tableName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getChangedTable() {
        return tableName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Table findChangedTable(Database model, boolean caseSensitive) {
        return model.findTable(tableName, caseSensitive);
    }
}
