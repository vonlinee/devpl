package org.apache.ddlutils.platform;

import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.util.ObjectMap;

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
    private final Map<String, ObjectMap> parametersPerTable = new HashMap<>();

    public ObjectMap getGlobalParameters() {
        ObjectMap valueMap = parametersPerTable.get(null);
        if (valueMap == null) {
            valueMap = new ObjectMap();
            parametersPerTable.put(null, valueMap);
        }
        return valueMap;
    }

    public void addGlobalParam(String paramName, Object value) {
        getGlobalParameters().put(paramName, value);
    }

    /**
     * Returns the parameters for the given table.
     * @param table The table
     * @return The parameters
     */
    public ObjectMap getParametersFor(Table table) {
        ObjectMap result = new ObjectMap();
        // the null location of the map contains global param
        ObjectMap globalParams = parametersPerTable.get(null);
        ObjectMap tableParams = parametersPerTable.get(table.getName());

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
    public void addParameter(Table table, String paramName, Object paramValue) {
        String key = (table == null ? null : table.getName());
        // we're using a list ordered map to retain the order
        // change: not using an ordered list
        ObjectMap params = parametersPerTable.computeIfAbsent(key, k -> new ObjectMap());
        params.put(paramName, paramValue);
    }
}
