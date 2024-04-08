package org.apache.ddlutils.model;


import org.apache.ddlutils.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for indexes.
 */
public abstract class IndexImplBase extends SchemaObject implements Index {

    /**
     * The columns making up the index.
     */
    protected List<IndexColumn> _columns = new ArrayList<>();

    @Override
    public int getColumnCount() {
        return _columns.size();
    }

    @Override
    public IndexColumn getColumn(int idx) {
        return _columns.get(idx);
    }

    @Override
    public IndexColumn[] getColumns() {
        return _columns.toArray(new IndexColumn[0]);
    }

    @Override
    public boolean hasColumn(Column column) {
        for (int idx = 0; idx < _columns.size(); idx++) {
            IndexColumn curColumn = getColumn(idx);
            if (column.equals(curColumn.getColumn())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasColumn(String columnName, boolean caseSensitive) {
        for (int idx = 0; idx < _columns.size(); idx++) {
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
            for (int idx = 0; idx < _columns.size(); idx++) {
                IndexColumn curColumn = getColumn(idx);

                if (curColumn.getOrdinalPosition() > column.getOrdinalPosition()) {
                    _columns.add(idx, column);
                    return;
                }
            }
            _columns.add(column);
        }
    }

    @Override
    public void removeColumn(IndexColumn column) {
        _columns.remove(column);
    }

    @Override
    public void removeColumn(int idx) {
        _columns.remove(idx);
    }
}
