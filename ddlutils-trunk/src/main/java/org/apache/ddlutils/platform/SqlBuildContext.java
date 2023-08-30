package org.apache.ddlutils.platform;

import org.apache.ddlutils.model.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains parameters used in the table creation. Note that the definition
 * order is retained (per table), so if a parameter should be applied before
 * some other parameter, then add it before the other one.
 * @version $Revision: 331006 $
 */
public final class SqlBuildContext {

    /**
     * The parameter maps keyed by the tables.
     */
    private final Map<String, Map<String, Object>> parametersPerTable = new HashMap<>();

    /**
     * Returns the parameters for the given table.
     * @param table The table
     * @return The parameters
     */
    public Map<String, Object> getParametersFor(Table table) {
        Map<String, Object> result = new HashMap<>();
        // the null location of the map contains global param
        Map<String, Object> globalParams = parametersPerTable.get(null);
        Map<String, Object> tableParams = parametersPerTable.get(table.getName());

        if (globalParams != null) {
            result.putAll(globalParams);
        }
        if (tableParams != null) {
            result.putAll(tableParams);
        }
        return result;
    }

    /**
     * Adds a parameter.
     * @param table      The table; if <code>null</code> then the parameter is for all tables
     * @param paramName  The name of the parameter
     * @param paramValue The value of the parameter
     */
    public void addParameter(Table table, String paramName, String paramValue) {
        String key = (table == null ? null : table.getName());
        // we're using a list ordered map to retain the order
        // change: not using an ordered list
        Map<String, Object> params = parametersPerTable.computeIfAbsent(key, k -> new HashMap<>());
        params.put(paramName, paramValue);
    }
}
