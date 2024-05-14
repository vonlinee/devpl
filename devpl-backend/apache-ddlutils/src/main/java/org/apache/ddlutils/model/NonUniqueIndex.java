package org.apache.ddlutils.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an index definition for a table.
 */
public class NonUniqueIndex extends SimpleIndex {
    /**
     * Unique ID for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = -3591499395114850301L;

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public Index copy() throws ModelException {
        NonUniqueIndex result = new NonUniqueIndex();
        result.setName(getName());
        result._columns = new ArrayList<>(_columns); // swallow copy
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NonUniqueIndex other) {
            if (!Objects.equals(getName(), other.getName())) {
                return false;
            }
            if (_columns.size() != other._columns.size()) {
                return false;
            }
            for (int i = 0; i < getColumnCount(); i++) {
                if (!Objects.equals(getColumn(i), other.getColumn(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean equalsIgnoreCase(Index other) {
        if (other instanceof NonUniqueIndex otherIndex) {
            boolean checkName = hasName() && otherIndex.hasName();
            if ((!checkName || getName().equalsIgnoreCase(otherIndex.getName())) &&
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
    public int hashCode() {
        return Objects.hash(getName()) + Arrays.hashCode(getColumns());
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
