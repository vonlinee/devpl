package org.apache.ddlutils.alteration;

import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Represents the change of the order of the columns of a table.
 * @version $Revision: $
 */
public class ColumnOrderChange extends TableChangeImplBase {
    /**
     * The map containing the new positions keyed by the source columns.
     */
    private final Map _newPositions;

    /**
     * Creates a new change object.
     * @param tableName    The name of the table whose primary key is to be changed
     * @param newPositions The map containing the new positions keyed by the source column names
     */
    public ColumnOrderChange(String tableName, Map newPositions) {
        super(tableName);
        _newPositions = newPositions;
    }

    /**
     * Returns the new position of the given source column.
     * @param sourceColumnName The column's name
     * @param caseSensitive    Whether case of the column name matters
     * @return The new position or -1 if no position is marked for the column
     */
    public int getNewPosition(String sourceColumnName, boolean caseSensitive) {
        Integer newPos = null;

        if (caseSensitive) {
            newPos = (Integer) _newPositions.get(sourceColumnName);
        } else {
            for (Iterator it = _newPositions.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();

                if (sourceColumnName.equalsIgnoreCase((String) entry.getKey())) {
                    newPos = (Integer) entry.getValue();
                    break;
                }
            }
        }

        return newPos == null ? -1 : newPos.intValue();
    }

    public void apply(Database database, boolean caseSensitive) {
        Table table = findChangedTable(database, caseSensitive);
        ArrayList newColumns = new ArrayList();

        for (int idx = 0; idx < table.getColumnCount(); idx++) {
            newColumns.add(table.getColumn(idx));
        }
        for (int idx = 0; idx < table.getColumnCount(); idx++) {
            Column column = table.getColumn(idx);
            int newPos = getNewPosition(column.getName(), caseSensitive);

            if (newPos >= 0) {
                newColumns.set(newPos, column);
            }
        }
        table.removeAllColumns();
        table.addColumns(newColumns);
    }
}
