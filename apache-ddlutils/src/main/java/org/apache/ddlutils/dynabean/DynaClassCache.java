package org.apache.ddlutils.dynabean;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.ddlutils.model.Table;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a cache of dyna class instances for a specific model, as well as
 * helper methods for dealing with these classes.
 * @version $Revision: 231110 $
 */
public class DynaClassCache {
    /**
     * A cache of the SqlDynaClasses per table name.
     */
    private final Map<String, SqlDynaClass> _dynaClassCache = new HashMap<>();

    /**
     * Creates a new dyna bean instance for the given table.
     * @param table The table
     * @return The new empty dyna bean
     */
    public DynaBean createNewInstance(Table table) throws RuntimeException {
        try {
            return getDynaClass(table).newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException("Could not create a new dyna bean for table " + table.getName(), ex);
        }
    }

    /**
     * Creates a new dyna bean instance for the given table and copies the values from the
     * given source object. The source object can be a bean, a map or a dyna bean.
     * This method is useful when iterating through an arbitrary dyna bean
     * result set after performing a query, then creating a copy as a DynaBean
     * which is bound to a specific table.
     * This new DynaBean can be kept around, changed and stored back into the database.
     * @param table  The table to create the dyna bean for
     * @param source Either a bean, a {@link java.util.Map} or a dyna bean that will be used
     *               to populate the resultint dyna bean
     * @return A new dyna bean bound to the given table and containing all the properties from
     * the source object
     */
    public DynaBean copy(Table table, Object source) {
        DynaBean answer = createNewInstance(table);
        try {
            // copy all the properties from the source
            BeanUtils.copyProperties(answer, source);
        } catch (InvocationTargetException | IllegalAccessException ex) {
            throw new RuntimeException("Could not populate the bean", ex);
        }
        return answer;
    }

    /**
     * Returns the {@link SqlDynaClass} for the given table. If the it does not
     * exist yet, a new one will be created based on the Table definition.
     * @param table The table
     * @return The <code>SqlDynaClass</code> for the indicated table
     */
    public SqlDynaClass getDynaClass(Table table) {
        SqlDynaClass answer = _dynaClassCache.get(table.getName());

        if (answer == null) {
            answer = SqlDynaClass.newInstance(table);
            _dynaClassCache.put(table.getName(), answer);
        }
        return answer;
    }

    /**
     * Returns the {@link SqlDynaClass} for the given bean.
     * @param dynaBean The bean
     * @return The dyna bean class
     */
    public SqlDynaClass getDynaClass(DynaBean dynaBean) {
        DynaClass dynaClass = dynaBean.getDynaClass();
        if (dynaClass instanceof SqlDynaClass) {
            return (SqlDynaClass) dynaClass;
        }
        // TODO: we could autogenerate an SqlDynaClass here ?
        throw new RuntimeException("The dyna bean is not an instance of a SqlDynaClass");
    }
}
