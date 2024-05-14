package org.apache.ddlutils.model;

import java.util.Objects;

/**
 * ColumnProperty which maps to a persistent Column in a database to a property of a Java Pojo.
 * The Column describes additional relational metadata
 * for the property such as whether the property is a primary key column,
 * an autoIncrement column and the SQL type etc.
 */
public class ColumnProperty {
    /**
     * The column for which this property is defined.
     */
    private final Column _column;
    /**
     * The column name
     */
    private String name;
    /**
     * the java property type
     */
    private Class<?> type;

    /**
     * Creates a property instance for the given column that accepts any data type.
     *
     * @param column The column
     */
    public ColumnProperty(Column column) {
        this.name = column.getName();
        _column = column;
    }

    public ColumnProperty(String columnName) {
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
    public ColumnProperty(Column column, Class<?> type) {
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

    public final void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must be not null");
    }

    public Class<?> getType() {
        return type;
    }

    public final void setType(Class<?> type) {
        this.type = Objects.requireNonNull(type, "type of column must be not null");
    }
}
