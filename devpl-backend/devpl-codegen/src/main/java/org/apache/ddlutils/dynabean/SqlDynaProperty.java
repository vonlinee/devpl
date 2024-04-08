package org.apache.ddlutils.dynabean;


import org.apache.ddlutils.model.Column;

/**
 * A SqlDynaProperty which maps to a persistent Column in a database.
 * The Column describes additional relational metadata
 * for the property such as whether the property is a primary key column,
 * an autoIncrement column and the SQL type etc.
 *
 *
 */
public class SqlDynaProperty {

    /**
     * The column for which this dyna property is defined.
     */
    private final Column _column;

    private String name;
    private Class<?> type;

    /**
     * Creates a property instance for the given column that accepts any data type.
     *
     * @param column The column
     */
    public SqlDynaProperty(Column column) {
        this.name = column.getName();
        _column = column;
    }

    public SqlDynaProperty(String columnName) {
        this.name = columnName;
        this._column = new Column();
        this._column.setName(columnName);
    }

    /**
     * Creates a property instance for the given column that only accepts the given type.
     *
     * @param column The column
     * @param type   The type of the property
     */
    public SqlDynaProperty(Column column, Class<?> type) {
        this.name = column.getName();
        this.type = type;
        _column = column;
    }

    /**
     * Returns the column for which this property is defined.
     *
     * @return The column
     */
    public Column getColumn() {
        return _column;
    }

    /**
     * Determines whether this property is for a primary key column.
     *
     * @return <code>true</code> if the property is for a primary key column
     */
    public boolean isPrimaryKey() {
        return getColumn().isPrimaryKey();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
