package org.apache.ddlutils.model;

import java.util.ArrayList;

/**
 * Represents an index definition for a table.
 * @version $Revision: 289996 $
 */
public class NonUniqueIndex extends GenericIndex {
    /**
     * Unique ID for serialization purposes.
     */
    private static final long serialVersionUID = -3591499395114850301L;

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public Index copy() throws ModelException {
        NonUniqueIndex result = new NonUniqueIndex();
        result.name = name;
        ArrayList<IndexColumn> columnList = new ArrayList<>();
        for (IndexColumn column : columns) {
            columnList.add(column.clone());
        }
        result.columns = columnList;
        return result;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public boolean equalsIgnoreCase(Index other) {
        if (other instanceof NonUniqueIndex otherIndex) {
            boolean checkName = (name != null) && (!name.isEmpty()) &&
                                (otherIndex.name != null) && (!otherIndex.name.isEmpty());
            if ((!checkName || name.equalsIgnoreCase(otherIndex.name)) &&
                (getColumnCount() == otherIndex.getColumnCount())) {
                for (int idx = 0; idx < getColumnCount(); idx++) {
                    if (!getColumn(idx).equalsIgnoreCase(otherIndex.getColumn(idx))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Index [name=" +
            getName() +
            "; " +
            getColumnCount() +
            " columns]";
    }

    @Override
    public String toVerboseString() {
        StringBuilder result = new StringBuilder();
        result.append("Index [");
        result.append(getName());
        result.append("] columns:");
        for (int idx = 0; idx < getColumnCount(); idx++) {
            result.append(" ");
            result.append(getColumn(idx).toString());
        }
        return result.toString();
    }
}
