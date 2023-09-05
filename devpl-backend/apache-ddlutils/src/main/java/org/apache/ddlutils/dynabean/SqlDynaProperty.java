package org.apache.ddlutils.dynabean;

import org.apache.ddlutils.model.Column;

/**
 * A DynaProperty which maps to a persistent Column in a database.
 * The Column describes additional relational metadata
 * for the property such as whether the property is a primary key column,
 * an autoIncrement column and the SQL type etc.
 * @version $Revision$
 */
public class SqlDynaProperty extends DynaProperty {
    /**
     * Unique ID for serialization purposes.
     */
    private static final long serialVersionUID = -4491018827449106588L;

    /**
     * The column for which this dyna property is defined.
     */
    private final Column column;

    /**
     * Creates a property instance for the given column that accepts any data type.
     * @param column The column
     */
    public SqlDynaProperty(Column column) {
        super(column.getName());
        this.column = column;
    }

    /**
     * Returns the column for which this property is defined.
     * @return The column
     */
    public Column getColumn() {
        return column;
    }

    // Helper methods
    //-------------------------------------------------------------------------

    /**
     * Determines whether this property is for a primary key column.
     * @return <code>true</code> if the property is for a primary key column
     */
    public boolean isPrimaryKey() {
        return getColumn().isPrimaryKey();
    }
}
