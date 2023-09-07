package org.apache.ddlutils.task;

import org.apache.ddlutils.model.Table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Specifies a parameter for the creation of the tables. These are usually platform specific.
 * Note that parameters are only applied when creating new tables, not when altering existing ones.
 * Note also that if no table name is specified, the parameter is used for all created tables.
 */
public class TableSpecificParameter extends Parameter {
    // TODO: Some wildcard/regular expression mechanism would be useful

    /**
     * The tables for which this parameter is applicable.
     */
    private final List<String> _tables = new ArrayList<>();

    /**
     * Specifies the comma-separated list of table names in whose creation this parameter
     * shall be used. For every table not in this list, the parameter is ignored.
     * @param tableList The tables
     *                  Use this or the <code>table</code> parameter. If neither is specified,
     *                  the parameter is applied in the creation of all tables.
     */
    public void setTables(String tableList) {
        StringTokenizer tokenizer = new StringTokenizer(tableList, ",");

        while (tokenizer.hasMoreTokens()) {
            String tableName = tokenizer.nextToken().trim();

            // TODO: Quotation, escaped characters ?
            _tables.add(tableName);
        }
    }

    /**
     * Specifies the name of the table in whose creation this parameter shall be applied.
     * @param tableName The table
     *                  Use this or the <code>tables</code> parameter. If neither is specified,
     *                  the parameter is applied in the creation of all tables.
     */
    public void setTable(String tableName) {
        _tables.add(tableName);
    }

    /**
     * Determines whether this parameter is applicable to the given table.
     * @param table         The table
     * @param caseSensitive Whether the case of the table name is relevant
     * @return <code>true</code> if this parameter is applicable to the table
     */
    public boolean isForTable(Table table, boolean caseSensitive) {
        if (_tables.isEmpty()) {
            return true;
        }
        for (Iterator<String> it = _tables.iterator(); it.hasNext(); ) {
            String tableName = it.next();
            if ((caseSensitive && tableName.equals(table.getName())) ||
                (!caseSensitive && tableName.equalsIgnoreCase(table.getName()))) {
                return true;
            }
        }
        return false;
    }
}
