package org.apache.ddlutils.model;


import java.io.Serial;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Provides compatibility with Torque-style xml with separate &lt;index&gt; and
 * &lt;unique&gt; tags, but adds no functionality.  All indexes are treated the
 * same by the Table.
 */
public class UniqueIndex extends IndexImplBase {
    /**
     * Unique ID for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = -4097003126550294993L;

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public Index copy() throws ModelException {
        UniqueIndex result = new UniqueIndex();
        result.setName(getName());
        result._columns = new ArrayList<>(_columns);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UniqueIndex other) {
            if (!Objects.equals(getName(), other.getName())) {
                return false;
            }
            if (getColumnCount() != other.getColumnCount()) {
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
        if (other instanceof UniqueIndex otherIndex) {
            boolean checkName = hasName() && otherIndex.hasName();
            if ((!checkName || getName().equalsIgnoreCase(otherIndex.getName())) && (getColumnCount() == otherIndex.getColumnCount())) {
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
        return _columns.hashCode();
    }

    @Override
    public String toString() {
        return "Unique index [name=" + getName() + "; " + getColumnCount() + " columns]";
    }

    @Override
    public String toVerboseString() {
        StringBuilder result = new StringBuilder();

        result.append("Unique index [");
        result.append(getName());
        result.append("] columns:");
        for (int idx = 0; idx < getColumnCount(); idx++) {
            result.append(" ");
            result.append(getColumn(idx).toString());
        }

        return result.toString();
    }
}
