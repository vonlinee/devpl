package org.apache.ddlutils.model;

/**
 * associated with a single row with a table in database.
 */
public class TableRow {

    /**
     * relational table model
     */
    private final TableModel tableModel;

    /**
     * all data of columns in this table
     */
    private final ResultSetRow rowData;

    /**
     * Creates a new dyna bean of the given class.
     *
     * @param tableModel The dyna class
     */
    public TableRow(TableModel tableModel) {
        this.tableModel = tableModel;
        this.rowData = new ResultSetRow();
        for (ColumnProperty property : this.tableModel.getProperties()) {
            this.rowData.addColumn(property.getName(), null);
        }
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        TableModel model = getTableModel();
        ColumnProperty[] props = model.getOriginProperties();
        result.append(model.getName());
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
        return rowData.getColumnValue(name);
    }

    /**
     * @param name  属性名
     * @param value 属性值
     */
    public final void setColumnValue(String name, Object value) {
        rowData.setColumnValue(name, value);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TableRow other) {
            TableModel dynaClass = getTableModel();
            if (dynaClass.equals(other.getTableModel())) {
                ColumnProperty[] props = dynaClass.getOriginProperties();
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

    public ResultSetRow getRowData() {
        return rowData;
    }
}
