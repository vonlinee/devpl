package org.apache.ddlutils.dynabean;


import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * SqlDynaClass is a DynaClass which is associated with a persistent
 * Table in a Database.
 *
 *
 */
public class SqlDynaClass {
    /**
     * The table for which this dyna class is defined.
     */
    private final Table _table;
    protected String name;
    protected SqlDynaProperty[] properties;
    /**
     * The primary key dyna properties.
     */
    private SqlDynaProperty[] _primaryKeyProperties;
    /**
     * The non-primary key dyna properties.
     */
    private SqlDynaProperty[] _nonPrimaryKeyProperties;

    /**
     * Creates a new dyna class instance for the given table that has the given properties.
     *
     * @param table      The table
     * @param properties The dyna properties
     */
    public SqlDynaClass(Table table, SqlDynaProperty[] properties) {
        if (table != null)
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
    public static SqlDynaClass newInstance(Table table) {
        List<SqlDynaProperty> properties = new ArrayList<>();

        if (table != null) {
            for (int idx = 0; idx < table.getColumnCount(); idx++) {
                properties.add(new SqlDynaProperty(table.getColumn(idx)));
            }
        }

        SqlDynaProperty[] array = new SqlDynaProperty[properties.size()];
        properties.toArray(array);
        return new SqlDynaClass(table, array);
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
    public SqlDynaProperty[] getSqlDynaProperties() {
        SqlDynaProperty[] props = this.properties;
        return swallowCopy(props);
    }

    /**
     * Returns the properties for the primary keys of the corresponding table.
     *
     * @return The properties
     */
    public SqlDynaProperty[] getPrimaryKeyProperties() {
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
    public SqlDynaProperty[] getNonPrimaryKeyProperties() {
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
        List<SqlDynaProperty> pkProps = new ArrayList<>();
        List<SqlDynaProperty> nonPkProps = new ArrayList<>();
        SqlDynaProperty[] properties = this.properties;

        for (SqlDynaProperty sqlProperty : properties) {
            if (sqlProperty.isPrimaryKey()) {
                pkProps.add(sqlProperty);
            } else {
                nonPkProps.add(sqlProperty);
            }
        }
        _primaryKeyProperties = pkProps.toArray(new SqlDynaProperty[0]);
        _nonPrimaryKeyProperties = nonPkProps.toArray(new SqlDynaProperty[0]);
    }

    public SqlDynaProperty[] getDynaProperties() {
        return this.properties;
    }

    public String getName() {
        return name;
    }

    public SqlDynaBean newInstance() {
        return new SqlDynaBean(this);
    }

    public SqlDynaProperty getDynaProperty(String attrName) {
        if (this.properties != null) {
            for (SqlDynaProperty property : this.properties) {
                if (property.getName().equalsIgnoreCase(attrName)) {
                    return property;
                }
            }
        }
        return null;
    }
}
