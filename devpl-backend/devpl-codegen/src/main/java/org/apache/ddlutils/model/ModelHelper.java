package org.apache.ddlutils.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains some utility functions for working with the model classes.
 */
public class ModelHelper {
    /**
     * Determines whether one of the tables in the list has a foreign key to a table outside the list,
     * or a table outside the list has a foreign key to one of the tables in the list.
     *
     * @param model  The database model
     * @param tables The tables
     * @throws ModelException If such a foreign key exists
     */
    public void checkForForeignKeysToAndFromTables(Database model, Table[] tables) throws ModelException {
        List<Table> tableList = Arrays.asList(tables);

        for (int tableIdx = 0; tableIdx < model.getTableCount(); tableIdx++) {
            Table curTable = model.getTable(tableIdx);
            boolean curTableIsInList = tableList.contains(curTable);

            for (int fkIdx = 0; fkIdx < curTable.getForeignKeyCount(); fkIdx++) {
                ForeignKey curFk = curTable.getForeignKey(fkIdx);

                if (curTableIsInList != tableList.contains(curFk.getForeignTable())) {
                    throw new ModelException("The table " + curTable.getName() + " has a foreign key to table " + curFk.getForeignTable().getName());
                }
            }
        }
    }

    /**
     * Removes all foreign keys from the tables in the list to tables outside the list,
     * or from tables outside the list to tables in the list.
     *
     * @param model  The database model
     * @param tables The tables
     */
    public void removeForeignKeysToAndFromTables(Database model, Table[] tables) {
        List<Table> tableList = Arrays.asList(tables);

        for (int tableIdx = 0; tableIdx < model.getTableCount(); tableIdx++) {
            Table curTable = model.getTable(tableIdx);
            boolean curTableIsInList = tableList.contains(curTable);
            ArrayList<ForeignKey> fksToRemove = new ArrayList<>();

            for (int fkIdx = 0; fkIdx < curTable.getForeignKeyCount(); fkIdx++) {
                ForeignKey curFk = curTable.getForeignKey(fkIdx);

                if (curTableIsInList != tableList.contains(curFk.getForeignTable())) {
                    fksToRemove.add(curFk);
                }
                for (ForeignKey foreignKey : fksToRemove) {
                    curTable.removeForeignKey(foreignKey);
                }
            }
        }
    }
}
