package org.apache.ddlutils.model;

import org.apache.ddlutils.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TableModel is associated with a persistent Table in a Database.
 * mapping a table in database to a java object
 */
public class TableModel {
    /**
     * The table for which this class is defined.
     */
    private final Table _table;
    /**
     * class name
     */
    protected String name;
    /**
     * property list of a java pojo class
     */
    protected ColumnProperty[] properties;
    /**
     * The primary key properties.
     */
    private ColumnProperty[] _primaryKeyProperties;
    /**
     * The non-primary key properties.
     */
    private ColumnProperty[] _nonPrimaryKeyProperties;

    /**
     * Creates a new dyna class instance for the given table that has the given properties.
     *
     * @param table      The table
     * @param properties The dyna properties
     */
    public TableModel(Table table, ColumnProperty[] properties) {
        Objects.requireNonNull(table, "table must not be null");
        this.name = table.getName();
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
    public static TableModel of(Table table) {
        ColumnProperty[] properties = new ColumnProperty[table.getColumnCount()];
        for (int i = 0; i < table.getColumnCount(); i++) {
            properties[i] = new ColumnProperty(table.getColumn(i));
        }
        return new TableModel(table, properties);
    }

    /**
     * Returns the table for which this dyna class is defined.
     *
     * @return The table
     */
    public Table getTable() {
        return _table;
    }

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
     * @return The properties copy
     */
    public ColumnProperty[] getProperties() {
        ColumnProperty[] result = new ColumnProperty[this.properties.length];
        System.arraycopy(this.properties, 0, result, 0, this.properties.length);
        return result;
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
        return Utils.swallowCopy(_primaryKeyProperties);
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
        return Utils.swallowCopy(_nonPrimaryKeyProperties);
    }

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

    /**
     * @return origin properties of this table class
     * @see TableModel#getProperties()
     */
    public ColumnProperty[] getOriginProperties() {
        return this.properties;
    }

    public String getName() {
        return name;
    }

    public ColumnProperty getProperty(String propertyName) {
        if (this.properties != null) {
            for (ColumnProperty property : this.properties) {
                if (property.getName().equalsIgnoreCase(propertyName)) {
                    return property;
                }
            }
        }
        return null;
    }

    /**
     * create an empty row
     *
     * @return empty row, all column values is null.
     */
    public TableRow createRow() {
        return new TableRow(this);
    }
}
