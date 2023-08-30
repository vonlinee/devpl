package org.apache.ddlutils.model;

import org.apache.ddlutils.util.StringUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Provides compatibility with Torque-style xml with separate &lt;index&gt; and
 * &lt;unique&gt; tags, but adds no functionality.  All indexes are treated the
 * same by the Table.
 * @version $Revision$
 */
public class UniqueIndex extends GenericIndex {
    /**
     * Unique ID for serialization purposes.
     */
    private static final long serialVersionUID = -4097003126550294993L;

    /**
     * {@inheritDoc}
     */
    public boolean isUnique() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Index copy() throws ModelException {
        UniqueIndex result = new UniqueIndex();
        result.name = name;
        result.columns = (ArrayList<IndexColumn>) columns.clone();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UniqueIndex) {
            UniqueIndex other = (UniqueIndex) obj;
            return Objects.equals(name, other.name) && Objects.equals(columns, other.columns);
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equalsIgnoreCase(Index other) {
        if (other instanceof UniqueIndex) {
            UniqueIndex otherIndex = (UniqueIndex) other;
            boolean checkName = StringUtils.hasText(name, otherIndex.name);
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
    public int hashCode() {
        return columns.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Unique index [name=" +
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
