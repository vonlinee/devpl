package org.apache.ddlutils.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a column of an index in the database model.
 */
public class IndexColumn implements Serializable, Cloneable {
    /**
     * Unique ID for serialization purposes.
     */
    private static final long serialVersionUID = -5009366896427504739L;
    /**
     * The name of the column.
     */
    protected String _name;
    /**
     * The size of the column in the index.
     */
    protected String _size;
    /**
     * The position within the owning index.
     */
    private int _ordinalPosition;
    /**
     * The indexed column.
     */
    private Column _column;

    /**
     * Creates a new index column object.
     */
    public IndexColumn() {
    }

    /**
     * Creates a new index column object.
     * @param column The indexed column
     */
    public IndexColumn(Column column) {
        _column = column;
        _name = column.getName();
    }

    /**
     * Creates a new index column object.
     * @param columnName The name of the corresponding table column
     */
    public IndexColumn(String columnName) {
        _name = columnName;
    }

    /**
     * Returns the position within the owning index.
     * @return The position
     */
    public int getOrdinalPosition() {
        return _ordinalPosition;
    }

    /**
     * Sets the position within the owning index. Please note that you should not
     * change the value once the column has been added to a index.
     * @param position The position
     */
    public void setOrdinalPosition(int position) {
        _ordinalPosition = position;
    }

    /**
     * Returns the name of the column.
     * @return The name
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets the name of the column.
     * @param name The name
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Returns the indexed column.
     * @return The column
     */
    public Column getColumn() {
        return _column;
    }

    /**
     * Sets the indexed column.
     * @param column The column
     */
    public void setColumn(Column column) {
        _column = column;
        _name = (column == null ? null : column.getName());
    }

    /**
     * Returns the size of the column in the index.
     * @return The size
     */
    public String getSize() {
        return _size;
    }

    /**
     * Sets the size of the column in the index.
     * @param size The size
     */
    public void setSize(String size) {
        _size = size;
    }

    /**
     * Compares this index column to the given one while ignoring the case of identifiers.
     * @param other The other index column
     * @return <code>true</code> if this index column is equal (ignoring case) to the given one
     */
    public boolean equalsIgnoreCase(IndexColumn other) {
        return Objects.equals(_name, other._name) && Objects.equals(_size, other._size);
    }

    @Override
    public String toString() {
        return "Index column [name=" + getName() + "; size=" + getSize() + "]";
    }

    @Override
    public IndexColumn clone() {
        try {
            IndexColumn clone = (IndexColumn) super.clone();
            clone._name = this._name;
            clone._column = new Column();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
