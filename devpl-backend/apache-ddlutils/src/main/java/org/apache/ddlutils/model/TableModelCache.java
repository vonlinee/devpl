package org.apache.ddlutils.model;

import org.apache.ddlutils.DatabaseObjectRelationMappingException;
import org.apache.ddlutils.util.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a cache of dyna class instances for a specific model, as well as
 * helper methods for dealing with these classes.
 */
public class TableModelCache {
    /**
     * A cache of the SqlDynaClasses per table name.
     */
    private final Map<String, TableModel> _tableClassCache = new HashMap<>();

    /**
     * Creates a new dyna bean instance for the given table.
     *
     * @param table The table
     * @return The new empty dyna bean
     */
    public TableRow createObject(Table table) throws DatabaseObjectRelationMappingException {
        return new TableRow(TableModel.newInstance(table));
    }

    /**
     * Creates a new dyna bean instance for the given table and copies the values from the
     * given source object. The source object can be a bean, a map or a dyna bean.
     * This method is useful when iterating through an arbitrary dyna bean
     * result set after performing a query, then creating a copy as a SqlDynaBean
     * which is bound to a specific table.
     * This new SqlDynaBean can be kept around, changed and stored back into the database.
     *
     * @param table  The table to create the dyna bean for
     * @param source Either a bean, a {@link java.util.Map} or a dyna bean that will be used
     *               to populate the result int dyna bean
     * @return A new dyna bean bound to the given table and containing all the properties from
     * the source object
     */
    public TableRow copy(Table table, Object source) throws DatabaseObjectRelationMappingException {
        TableRow answer = createObject(table);
        try {
            // copy all the properties from the source
            BeanUtils.copyProperties(answer, source);
        } catch (InvocationTargetException | IllegalAccessException ex) {
            throw new DatabaseObjectRelationMappingException("Could not populate the bean", ex);
        }
        return answer;
    }

    /**
     * Returns the {@link TableModel} for the given table. If it does not
     * exist yet, a new one will be created based on the Table definition.
     *
     * @param table The table
     * @return The <code>SqlDynaClass</code> for the indicated table
     */
    public TableModel getDynaClass(Table table) {
        TableModel answer = _tableClassCache.get(table.getName());
        if (answer == null) {
            answer = createDynaClass(table);
            _tableClassCache.put(table.getName(), answer);
        }
        return answer;
    }

    /**
     * Returns the {@link TableModel} for the given bean.
     *
     * @param dynaBean The bean
     * @return The dyna bean class
     */
    public TableModel getDynaClass(TableRow dynaBean) throws DatabaseObjectRelationMappingException {
        TableModel dynaClass = dynaBean.getTableModel();
        if (dynaClass != null) {
            return dynaClass;
        } else {
            // TODO: we could autogenerate an SqlDynaClass here ?
            throw new DatabaseObjectRelationMappingException("The dyna bean is not an instance of a SqlDynaClass");
        }
    }

    /**
     * Creates a new {@link TableModel} instance for the given table based on the table definition.
     *
     * @param table The table
     * @return The new dyna class
     */
    private TableModel createDynaClass(Table table) {
        return TableModel.newInstance(table);
    }
}
