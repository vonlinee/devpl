package org.apache.ddlutils.model;

import java.util.*;

/**
 * Contains some utility functions for working with the model classes.
 */
public final class ModelHelper {
    /**
     * Determines whether one of the tables in the list has a foreign key to a table outside the list,
     * or a table outside the list has a foreign key to one of the tables in the list.
     *
     * @param model  The database model
     * @param tables The tables
     * @throws ModelException If such a foreign key exists
     */
    public static void checkForForeignKeysToAndFromTables(Database model, Table[] tables) throws ModelException {
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
    public static void removeForeignKeysToAndFromTables(Database model, Table[] tables) {
        List<Table> tableList = Arrays.asList(tables);
        for (int tableIdx = 0; tableIdx < model.getTableCount(); tableIdx++) {
            Table curTable = model.getTable(tableIdx);
            boolean curTableIsInList = tableList.contains(curTable);
            List<ForeignKey> fksToRemove = new ArrayList<>();
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

    /**
     * 返回一个所有列的值都为null的Map，表示一行数据
     *
     * @param table 表
     * @return key为列名称, value为null
     */
    public static Map<String, Object> emptyColumnValueMap(TableModel table) {
        Map<String, Object> columnValueMap = new HashMap<>();
        for (ColumnProperty property : table.getProperties()) {
            columnValueMap.put(property.getName(), null);
        }
        return columnValueMap;
    }

    /**
     * 根据表名和列名称创建 Table 模型，其他信息都置为空
     *
     * @param tableName   表名
     * @param columnNames 列名称
     * @return Table
     */
    public static Table newTable(String tableName, Set<String> columnNames) {
        Table table = new Table();
        table.setName(tableName);
        for (String columnName : columnNames) {
            Column column = new Column();
            column.setName(columnName);
            table.addColumn(column);
        }
        return table;
    }
}
