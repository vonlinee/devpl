package org.apache.ddlutils.alteration;

import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Reference;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * The base class for changes affecting foreign keys.
 */
public abstract class ForeignKeyChangeBase extends TableChangeBase
    implements ForeignKeyChange {
    /**
     * List of pairs of local and corresponding foreign column names that make up the foreign key.
     */
    private final List<Pair<String>> _referenceColumnNames = new ArrayList<>();

    /**
     * Creates a new change object.
     *
     * @param tableName  The name of the table that owns the foreign key
     * @param foreignKey The foreign key; note that this change object will not maintain a reference
     *                   to the foreign key object
     */
    public ForeignKeyChangeBase(String tableName, ForeignKey foreignKey) {
        super(tableName);
        for (int refIdx = 0; refIdx < foreignKey.getReferenceCount(); refIdx++) {
            Reference ref = foreignKey.getReference(refIdx);

            _referenceColumnNames.add(new Pair<>(ref.getLocalColumnName(), ref.getForeignColumnName()));
        }
    }

    @Override
    public ForeignKey findChangedForeignKey(Database model, boolean caseSensitive) {
        Table table = findChangedTable(model, caseSensitive);

        if (table != null) {
            for (int fkIdx = 0; fkIdx < table.getForeignKeyCount(); fkIdx++) {
                ForeignKey curFk = table.getForeignKey(fkIdx);

                if (curFk.getReferenceCount() == _referenceColumnNames.size()) {
                    for (int refIdx = 0; refIdx < curFk.getReferenceCount(); refIdx++) {
                        Reference ref = curFk.getReference(refIdx);
                        Pair<String> colNames = _referenceColumnNames.get(refIdx);

                        if (caseSensitive) {
                            if (ref.getLocalColumnName().equals(colNames.getFirst()) &&
                                ref.getForeignColumnName().equals(colNames.getSecond())) {
                                return curFk;
                            }
                        } else {
                            if (ref.getLocalColumnName().equalsIgnoreCase(colNames.getFirst()) &&
                                ref.getForeignColumnName().equalsIgnoreCase(colNames.getSecond())) {
                                return curFk;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
