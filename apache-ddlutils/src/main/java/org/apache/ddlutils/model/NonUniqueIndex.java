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

    /**
     * {@inheritDoc}
     */
    public boolean isUnique() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equalsIgnoreCase(Index other) {
        if (other instanceof NonUniqueIndex) {
            NonUniqueIndex otherIndex = (NonUniqueIndex) other;
            boolean checkName = (name != null) && (name.length() > 0) &&
                    (otherIndex.name != null) && (otherIndex.name.length() > 0);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Index [name=" +
                getName() +
                "; " +
                getColumnCount() +
                " columns]";
    }

    /**
     * {@inheritDoc}
     */
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
