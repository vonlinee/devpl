package org.apache.ddlutils.model;

import org.apache.ddlutils.util.PojoMap;

/**
 * associated with a single row with a table in database.
 */
public class TableObject {

    private final TableClass tableClass;

    /**
     * all data of columns in this table
     */
    private final PojoMap rowData = new PojoMap();

    /**
     * Creates a new dyna bean of the given class.
     *
     * @param tableClass The dyna class
     */
    public TableObject(TableClass tableClass) {
        this.tableClass = tableClass;
    }

    public TableClass getTableClass() {
        return tableClass;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        TableClass type = getTableClass();
        ColumnProperty[] props = type.getDynaProperties();

        result.append(type.getName());
        result.append(": ");
        for (int idx = 0; idx < props.length; idx++) {
            if (idx > 0) {
                result.append(", ");
            }
            result.append(props[idx].getName());
            result.append(" = ");
            result.append(getColumnValue(props[idx].getName()));
        }
        return result.toString();
    }

    /**
     * @param name 属性名
     * @return 属性值
     */
    public Object getColumnValue(String name) {
        return rowData.get(name);
    }

    /**
     * @param name  属性名
     * @param value 属性值
     */
    public void setColumnValue(String name, Object value) {
        rowData.put(name, value);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TableObject other) {
            TableClass dynaClass = getTableClass();
            if (dynaClass.equals(other.getTableClass())) {
                ColumnProperty[] props = dynaClass.getDynaProperties();
                for (ColumnProperty prop : props) {
                    Object value = getColumnValue(prop.getName());
                    Object otherValue = other.getColumnValue(prop.getName());
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
