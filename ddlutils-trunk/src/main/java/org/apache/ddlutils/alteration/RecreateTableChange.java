package org.apache.ddlutils.alteration;

import org.apache.ddlutils.model.CloneHelper;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

import java.util.List;

/**
 * Represents the recreation of a table, i.e. creating a temporary table using the target table definition,
 * copying the data from the original table into this temporary table, dropping the original table and
 * finally renaming the temporary table to the final table. This change is only created by the model
 * comparator if the table definition change predicate flags a table change as unsupported.
 * @version $Revision: $
 */
public class RecreateTableChange extends TableChangeImplBase {
    /**
     * The target table definition.
     */
    private final Table _targetTable;
    /**
     * The original table changes, one of which is unsupported by the current platform.
     */
    private final List<TableChange> _originalChanges;

    /**
     * Creates a new change object for recreating a table. This change is used to specify that a table needs
     * to be dropped and then re-created (with changes). In the standard model comparison algorithm, it will
     * replace all direct changes to the table's columns (i.e. foreign key and index changes are unaffected).
     * @param tableName       The name of the table
     * @param targetTable     The table as it should be; note that the change object will keep a reference
     *                        to this table which means that it should not be changed after creating this
     *                        change object
     * @param originalChanges The original changes that this change object replaces
     */
    public RecreateTableChange(String tableName, Table targetTable, List<TableChange> originalChanges) {
        super(tableName);
        _targetTable = targetTable;
        _originalChanges = originalChanges;
    }

    /**
     * Returns the original table changes, one of which is unsupported by the current platform.
     * @return The table changes
     */
    public List<TableChange> getOriginalChanges() {
        return _originalChanges;
    }

    /**
     * Returns the target table definition. Due to when an object of this kind is created in the comparison
     * process, this table object will not have any foreign keys pointing to or from it, i.e. it is
     * independent of any model.
     * @return The table definition
     */
    public Table getTargetTable() {
        return _targetTable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(Database database, boolean caseSensitive) {
        // we only need to replace the table in the model, as there can't be a
        // foreign key from or to it when these kind of changes are created
        for (int tableIdx = 0; tableIdx < database.getTableCount(); tableIdx++) {
            Table curTable = database.getTable(tableIdx);

            if ((caseSensitive && curTable.getName().equals(getChangedTable())) || (!caseSensitive && curTable.getName()
                    .equalsIgnoreCase(getChangedTable()))) {
                database.removeTable(tableIdx);
                database.addTable(tableIdx, new CloneHelper().clone(_targetTable, true, false, database, caseSensitive));
                break;
            }
        }
    }
}
