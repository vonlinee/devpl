package org.apache.ddlutils.alteration;


import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.util.StringUtils;

/**
 * Represents the change of the primary key of a table.
 */
public class PrimaryKeyChange extends TableChangeBase {
    /**
     * The names of the columns making up the new primary key.
     */
    private final String[] _newPrimaryKeyColumns;

    /**
     * Creates a new change object.
     *
     * @param tableName            The name of the table whose primary key is to be changed
     * @param newPrimaryKeyColumns The names of the columns making up the new primary key
     */
    public PrimaryKeyChange(String tableName, String[] newPrimaryKeyColumns) {
        super(tableName);
        if (newPrimaryKeyColumns == null) {
            _newPrimaryKeyColumns = new String[0];
        } else {
            _newPrimaryKeyColumns = new String[newPrimaryKeyColumns.length];

            System.arraycopy(newPrimaryKeyColumns, 0, _newPrimaryKeyColumns, 0, newPrimaryKeyColumns.length);
        }
    }

    /**
     * Returns the names of the columns making up the new primary key.
     *
     * @return The column names
     */
    public String[] getNewPrimaryKeyColumns() {
        return StringUtils.copy(_newPrimaryKeyColumns);
    }

    @Override
    public void apply(Database model, boolean caseSensitive) {
        Table table = findChangedTable(model, caseSensitive);
        Column[] pkCols = table.getPrimaryKeyColumns();

        for (Column pkCol : pkCols) {
            pkCol.setPrimaryKey(false);
        }
        for (String newPrimaryKeyColumn : _newPrimaryKeyColumns) {
            Column column = table.findColumn(newPrimaryKeyColumn, caseSensitive);
            column.setPrimaryKey(true);
        }
    }
}
