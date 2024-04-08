package org.apache.ddlutils.dynabean;

import org.apache.ddlutils.util.ContextMap;

public class SqlDynaBean extends ContextMap {

    SqlDynaClass sqlDynaClass;

    /**
     * Creates a new dyna bean of the given class.
     *
     * @param dynaClass The dyna class
     */
    public SqlDynaBean(SqlDynaClass dynaClass) {
        this.sqlDynaClass = dynaClass;
    }

    public SqlDynaClass getDynaClass() {
        return sqlDynaClass;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        SqlDynaClass type = getDynaClass();
        SqlDynaProperty[] props = type.getDynaProperties();

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
     * @param name 属性名
     * @return 属性值
     */
    public Object get(String name) {
        return super.get(name);
    }

    /**
     * @param name  属性名
     * @param value 属性值
     */
    public void set(String name, Object value) {
        put(name, value);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SqlDynaBean other) {
            SqlDynaClass dynaClass = getDynaClass();

            if (dynaClass.equals(other.getDynaClass())) {
                SqlDynaProperty[] props = dynaClass.getDynaProperties();
                for (SqlDynaProperty prop : props) {
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
