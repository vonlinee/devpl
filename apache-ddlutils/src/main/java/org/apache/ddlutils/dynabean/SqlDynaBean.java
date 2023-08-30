package org.apache.ddlutils.dynabean;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;

/**
 * SqlDynaBean is a DynaBean which can be persisted as a single row in
 * a Database Table.
 * @version $Revision$
 */
public class SqlDynaBean extends BasicDynaBean {
    /**
     * Unique ID for serialization purposes.
     */
    private static final long serialVersionUID = -6946514447446174227L;

    /**
     * Creates a new dyna bean of the given class.
     * @param dynaClass The dyna class
     */
    public SqlDynaBean(DynaClass dynaClass) {
        super(dynaClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        DynaClass type = getDynaClass();
        DynaProperty[] props = type.getDynaProperties();

        result.append(type.getName());
        result.append(": ");
        for (int idx = 0; idx < props.length; idx++) {
            if (idx > 0) {
                result.append(", ");
            }
            result.append(props[idx].getName());
            result.append(" = ");
            result.append(get(props[idx].getName()));
        }
        return result.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SqlDynaBean) {
            SqlDynaBean other = (SqlDynaBean) obj;
            DynaClass dynaClass = getDynaClass();
            if (dynaClass.equals(other.getDynaClass())) {
                DynaProperty[] props = dynaClass.getDynaProperties();
                for (DynaProperty prop : props) {
                    Object value = get(prop.getName());
                    Object otherValue = other.get(prop.getName());
                    if (value == null) {
                        if (otherValue != null) {
                            return false;
                        }
                    } else {
                        return value.equals(otherValue);
                    }
                }
                return true;
            }
        }
        return false;
    }
}
