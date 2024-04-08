package org.apache.ddlutils.alteration;


import org.apache.ddlutils.model.Table;

import java.util.List;

/**
 * Defines a predicate that allows platforms to state whether all of the given table definition
 * changes (i.e. column changes and primary key changes) are supported by the platform or not.
 */
public interface TableDefinitionChangesPredicate {
    /**
     * Evaluates the given list of table changes and determines whether they are supported.
     *
     * @param intermediateTable The current table object which has certain non-table-definition
     *                          changes already applied (those that would come before the give
     *                          list of changes in the result of
     *                          {@link ModelComparator#compare(org.apache.ddlutils.model.Database, org.apache.ddlutils.model.Database)}
     * @param changes           The non-empty list of changes
     * @return <code>true</code> if the current plaform supports them
     */
    boolean areSupported(Table intermediateTable, List<TableChange> changes);
}
