package org.apache.ddlutils.platform;

import org.apache.ddlutils.alteration.*;
import org.apache.ddlutils.model.Table;

import java.util.List;

/**
 * This is the default predicate for filtering supported table definition changes
 * in the {@link ModelComparator}. It is also useful as the base class for platform
 * specific implementations.
 */
public class DefaultTableDefinitionChangesPredicate implements TableDefinitionChangesPredicate {

    @Override
    public boolean areSupported(Table intermediateTable, List<TableChange> changes) {
        for (TableChange change : changes) {
            if (!isSupported(intermediateTable, change)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the given change is supported.
     *
     * @param intermediateTable The current table to which this change would be applied
     * @param change            The table change
     * @return <code>true</code> if the change is supported
     */
    protected boolean isSupported(Table intermediateTable, TableChange change) {
        if (change instanceof AddColumnChange addColumnChange) {
            return addColumnChange.isAtEnd() &&
                   (!addColumnChange.getNewColumn().isRequired() ||
                    (addColumnChange.getNewColumn().getDefaultValue() != null) ||
                    addColumnChange.getNewColumn().isAutoIncrement());
        } else return change instanceof AddPrimaryKeyChange;
    }
}
