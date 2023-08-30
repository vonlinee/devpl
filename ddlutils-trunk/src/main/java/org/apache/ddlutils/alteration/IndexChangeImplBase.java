package org.apache.ddlutils.alteration;

import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * The base class for changes affecting indexes.
 * @version $Revision: $
 */
public abstract class IndexChangeImplBase extends TableChangeImplBase
        implements IndexChange {
    /**
     * The names of the columns in the index.
     */
    private List _columnNames = new ArrayList();

    /**
     * Creates a new change object.
     * @param tableName The name of the changed table
     * @param index     The index; note that this change object will not maintain a reference
     *                  to the index object
     */
    public IndexChangeImplBase(String tableName, Index index) {
        super(tableName);
        for (int colIdx = 0; colIdx < index.getColumnCount(); colIdx++) {
            _columnNames.add(index.getColumn(colIdx).getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    public Index findChangedIndex(Database model, boolean caseSensitive) {
        Table table = findChangedTable(model, caseSensitive);

        if (table != null) {
            for (int indexIdx = 0; indexIdx < table.getIndexCount(); indexIdx++) {
                Index curIndex = table.getIndex(indexIdx);

                if (curIndex.getColumnCount() == _columnNames.size()) {
                    for (int colIdx = 0; colIdx < curIndex.getColumnCount(); colIdx++) {
                        String curColName = curIndex.getColumn(colIdx).getName();
                        String expectedColName = (String) _columnNames.get(colIdx);

                        if ((caseSensitive && curColName.equals(expectedColName)) ||
                                (!caseSensitive && curColName.equalsIgnoreCase(expectedColName))) {
                            return curIndex;
                        }
                    }
                }
            }
        }
        return null;
    }
}
