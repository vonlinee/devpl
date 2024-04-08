package org.apache.ddlutils.model;

import java.util.ArrayList;
import java.util.List;

/**
 * TableClass is associated with a persistent Table in a Database.
 * mapping a table in database to a java object
 */
public class TableClass {
    /**
     * The table for which this dyna class is defined.
     */
    private final Table _table;
    protected String name;
    protected ColumnProperty[] properties;
    /**
     * The primary key dyna properties.
     */
    private ColumnProperty[] _primaryKeyProperties;
    /**
     * The non-primary key dyna properties.
     */
    private ColumnProperty[] _nonPrimaryKeyProperties;

    /**
     * Creates a new dyna class instance for the given table that has the given properties.
     *
     * @param table      The table
     * @param properties The dyna properties
     */
    public TableClass(Table table, ColumnProperty[] properties) {
        if (table != null) this.name = table.getName();
        this.properties = properties;
        _table = table;
    }

    /**
     * Factory method for creating and initializing a new dyna class instance
     * for the given table.
     *
     * @param table The table
     * @return The dyna class for the table
     */
    public static TableClass newInstance(Table table) {
        List<ColumnProperty> properties = new ArrayList<>();
        if (table != null) {
            for (int idx = 0; idx < table.getColumnCount(); idx++) {
                properties.add(new ColumnProperty(table.getColumn(idx)));
            }
        }
        ColumnProperty[] array = new ColumnProperty[properties.size()];
        properties.toArray(array);
        return new TableClass(table, array);
    }

    /**
     * Returns the table for which this dyna class is defined.
     *
     * @return The table
     */
    public Table getTable() {
        return _table;
    }

    // Helper methods
    //-------------------------------------------------------------------------

    /**
     * Returns the table name for which this dyna class is defined.
     *
     * @return The table name
     */
    public String getTableName() {
        return getTable().getName();
    }

    /**
     * Returns the properties of this dyna class.
     *
     * @return The properties
     */
    public ColumnProperty[] getSqlDynaProperties() {
        ColumnProperty[] props = this.properties;
        return swallowCopy(props);
    }

    /**
     * Returns the properties for the primary keys of the corresponding table.
     *
     * @return The properties
     */
    public ColumnProperty[] getPrimaryKeyProperties() {
        if (_primaryKeyProperties == null) {
            initPrimaryKeys();
        }
        return swallowCopy(_primaryKeyProperties);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] swallowCopy(T[] src) {
        Object[] result = new Object[src.length];
        System.arraycopy(src, 0, result, 0, src.length);
        return (T[]) result;
    }

    /**
     * Returns the properties for the non-primary keys of the corresponding table.
     *
     * @return The properties
     */
    public ColumnProperty[] getNonPrimaryKeyProperties() {
        if (_nonPrimaryKeyProperties == null) {
            initPrimaryKeys();
        }
        return swallowCopy(_nonPrimaryKeyProperties);
    }

    // Implementation methods
    //-------------------------------------------------------------------------

    /**
     * Initializes the primary key and non-primary key property arrays.
     */
    protected void initPrimaryKeys() {
        List<ColumnProperty> pkProps = new ArrayList<>();
        List<ColumnProperty> nonPkProps = new ArrayList<>();
        ColumnProperty[] properties = this.properties;

        for (ColumnProperty sqlProperty : properties) {
            if (sqlProperty.isPrimaryKey()) {
                pkProps.add(sqlProperty);
            } else {
                nonPkProps.add(sqlProperty);
            }
        }
        _primaryKeyProperties = pkProps.toArray(new ColumnProperty[0]);
        _nonPrimaryKeyProperties = nonPkProps.toArray(new ColumnProperty[0]);
    }

    public ColumnProperty[] getDynaProperties() {
        return this.properties;
    }

    public String getName() {
        return name;
    }

    public TableObject newInstance() {
        return new TableObject(this);
    }

    public ColumnProperty getDynaProperty(String attrName) {
        if (this.properties != null) {
            for (ColumnProperty property : this.properties) {
                if (property.getName().equalsIgnoreCase(attrName)) {
                    return property;
                }
            }
        }
        return null;
    }
}
