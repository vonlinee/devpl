package org.apache.ddlutils.io;

import org.apache.ddlutils.model.RowData;
import org.apache.ddlutils.model.Table;

import java.util.Iterator;
import java.util.Map;

/**
 * Stores the identity of a database object as defined by its primary keys. Is used
 * by {@link org.apache.ddlutils.io.DataToDatabaseSink} class for inserting objects
 * in the correct order.
 */
public class Identity {
    /**
     * The table.
     */
    private final Table _table;
    /**
     * The identity columns and their values.
     */
    private final RowData _columnValues = new RowData();
    /**
     * The optional foreign key name whose referenced object this identity represents.
     */
    private String _fkName;

    /**
     * Creates a new identity object for the given table.
     *
     * @param table The name of the table
     */
    public Identity(Table table) {
        _table = table;
    }

    /**
     * Creates a new identity object for the given table.
     *
     * @param table  The table
     * @param fkName The name of the foreign key whose referenced object this identity represents
     */
    public Identity(Table table, String fkName) {
        _table = table;
        _fkName = fkName;
    }

    /**
     * Returns the table that this identity is for.
     *
     * @return The table
     */
    public Table getTable() {
        return _table;
    }

    /**
     * Returns the name of the foreign key whose referenced object this identity represents. This
     * name is <code>null</code> if the identity is not for a foreign key, or if the foreign key
     * was unnamed.
     *
     * @return The foreign key name
     */
    public String getForeignKeyName() {
        return _fkName;
    }

    /**
     * Specifies the value of the indicated identity columns.
     *
     * @param name  The column name
     * @param value The value for the column
     */
    public void setColumnValue(String name, Object value) {
        _columnValues.put(name, value);
    }

    /**
     * Returns the value of the indicated identity columns.
     *
     * @param name The column name
     * @return The column's value
     */
    public Object getColumnValue(String name) {
        return _columnValues.get(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Identity otherIdentity)) {
            return false;
        }

        if (!_table.equals(otherIdentity._table)) {
            return false;
        }
        if (_columnValues.keySet().size() != otherIdentity._columnValues.keySet().size()) {
            return false;
        }
        for (Map.Entry<String, Object> entry : _columnValues.entrySet()) {
            Object otherValue = otherIdentity._columnValues.get(entry.getKey());

            if (entry.getValue() == null) {
                if (otherValue != null) {
                    return false;
                }
            } else {
                if (!entry.getValue().equals(otherValue)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(_table.getName());
        buffer.append(":");
        for (Iterator<Map.Entry<String, Object>> it = _columnValues.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> entry = it.next();
            buffer.append(entry.getKey());
            buffer.append("=");
            buffer.append(entry.getValue());
            if (it.hasNext()) {
                buffer.append(";");
            }
        }
        return buffer.toString();
    }
}
