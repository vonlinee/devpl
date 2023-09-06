package org.apache.ddlutils.model;

import org.apache.ddlutils.util.StringUtils;

import java.util.ArrayList;

/**
 * Base class for indexes.
 */
public abstract class GenericIndex implements Index {
    /**
     * The name of the index.
     */
    protected String name;
    /**
     * The columns making up the index.
     */
    protected ArrayList<IndexColumn> columns = new ArrayList<>();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public IndexColumn getColumn(int idx) {
        return columns.get(idx);
    }

    @Override
    public IndexColumn[] getColumns() {
        return columns.toArray(new IndexColumn[0]);
    }

    @Override
    public boolean hasColumn(Column column) {
        for (int idx = 0; idx < columns.size(); idx++) {
            IndexColumn curColumn = getColumn(idx);
            if (column.equals(curColumn.getColumn())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasColumn(String columnName, boolean caseSensitive) {
        for (int idx = 0; idx < columns.size(); idx++) {
            IndexColumn curColumn = getColumn(idx);
            if (StringUtils.equals(columnName, curColumn.getName(), caseSensitive)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addColumn(IndexColumn column) {
        if (column != null) {
            for (int idx = 0; idx < columns.size(); idx++) {
                IndexColumn curColumn = getColumn(idx);
                if (curColumn.getOrdinalPosition() > column.getOrdinalPosition()) {
                    columns.add(idx, column);
                    return;
                }
            }
            columns.add(column);
        }
    }

    @Override
    public void removeColumn(IndexColumn column) {
        columns.remove(column);
    }

    @Override
    public void removeColumn(int idx) {
        columns.remove(idx);
    }
}
