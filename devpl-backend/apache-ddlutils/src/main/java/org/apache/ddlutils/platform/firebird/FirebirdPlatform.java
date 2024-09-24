package org.apache.ddlutils.platform.firebird;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.alteration.*;
import org.apache.ddlutils.model.CascadeActionEnum;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.*;

import java.io.IOException;
import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The platform implementation for the Firebird database.
 * It is assumed that the database is configured with sql dialect 3!
 */
public class FirebirdPlatform extends PlatformImplBase {

    /**
     * Creates a new Firebird platform instance.
     */
    public FirebirdPlatform() {
        PlatformInfo info = getPlatformInfo();

        info.setMaxIdentifierLength(31);
        info.setSystemForeignKeyIndicesAlwaysNonUnique(true);
        info.setPrimaryKeyColumnAutomaticallyRequired(true);
        info.setCommentPrefix("/*");
        info.setCommentSuffix("*/");
        info.setSupportedOnUpdateActions(new CascadeActionEnum[]{CascadeActionEnum.CASCADE, CascadeActionEnum.SET_DEFAULT, CascadeActionEnum.SET_NULL, CascadeActionEnum.NONE});
        info.setSupportedOnDeleteActions(new CascadeActionEnum[]{CascadeActionEnum.CASCADE, CascadeActionEnum.SET_DEFAULT, CascadeActionEnum.SET_NULL, CascadeActionEnum.NONE});

        info.addNativeTypeMapping(Types.ARRAY, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BINARY, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BIT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.BLOB, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BOOLEAN, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.CLOB, "BLOB SUB_TYPE TEXT", Types.LONGVARCHAR);
        info.addNativeTypeMapping(Types.DATALINK, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.DISTINCT, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.DOUBLE, "DOUBLE PRECISION");
        info.addNativeTypeMapping(Types.FLOAT, "DOUBLE PRECISION", Types.DOUBLE);
        info.addNativeTypeMapping(Types.JAVA_OBJECT, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.LONGVARBINARY, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.LONGVARCHAR, "BLOB SUB_TYPE TEXT");
        info.addNativeTypeMapping(Types.NULL, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.OTHER, "BLOB", Types.LONGVARBINARY);
        // This is back-mapped to REAL in the model reader
        info.addNativeTypeMapping(Types.REAL, "FLOAT");
        info.addNativeTypeMapping(Types.REF, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.STRUCT, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.TINYINT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.VARBINARY, "BLOB", Types.LONGVARBINARY);

        info.setDefaultSize(Types.VARCHAR, 254);
        info.setDefaultSize(Types.CHAR, 254);

        setSqlBuilder(new FirebirdBuilder(this));
        setModelReader(new FirebirdModelReader(this));
    }

    @Override
    public DBType getDBType() {
        return BuiltinDBType.FIREBIRD;
    }

    @Override
    protected ModelComparator getModelComparator() {
        ModelComparator comparator = super.getModelComparator();

        comparator.setCanDropPrimaryKeyColumns(false);
        return comparator;
    }

    @Override
    protected TableDefinitionChangesPredicate getTableDefinitionChangesPredicate() {
        return new DefaultTableDefinitionChangesPredicate() {
            @Override
            public boolean areSupported(Table intermediateTable, List<TableChange> changes) {
                // Firebird does support adding a primary key, but only if none of the primary
                // key columns have been added within the same session
                if (super.areSupported(intermediateTable, changes)) {
                    Set<String> addedColumns = new HashSet<>();
                    String[] pkColNames = null;
                    for (TableChange change : changes) {
                        if (change instanceof AddColumnChange) {
                            addedColumns.add(((AddColumnChange) change).getNewColumn().getName());
                        } else if (change instanceof AddPrimaryKeyChange) {
                            pkColNames = ((AddPrimaryKeyChange) change).getPrimaryKeyColumns();
                        } else if (change instanceof PrimaryKeyChange) {
                            pkColNames = ((PrimaryKeyChange) change).getNewPrimaryKeyColumns();
                        }
                    }
                    if (pkColNames != null) {
                        for (String pkColName : pkColNames) {
                            if (addedColumns.contains(pkColName)) {
                                return false;
                            }
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected boolean isSupported(Table intermediateTable, TableChange change) {
                // Firebird cannot add columns to the primary key or drop columns from it but
                // since we add/drop the primary key with separate changes anyway, this will
                // no problem here
                if (change instanceof AddColumnChange addColumnChange) {

                    // Firebird does not apply default values or identity status to existing rows when adding a column
                    return !addColumnChange.getNewColumn().isAutoIncrement() &&
                           ((addColumnChange.getNewColumn().getDefaultValue() == null) && !addColumnChange.getNewColumn().isRequired());
                } else {
                    return (change instanceof RemoveColumnChange) ||
                           super.isSupported(intermediateTable, change);
                }
            }
        };
    }

    /**
     * Processes the addition of a column to a table.
     *
     * @param currentModel The current database schema
     * @param params       The parameters used in the creation of new tables. Note that for existing
     *                     tables, the parameters won't be applied
     * @param change       The change object
     */
    @Override
    public void processChange(Database currentModel,
                              CreationParameters params,
                              AddColumnChange change) throws IOException {
        Table changedTable = findChangedTable(currentModel, change);
        Column prevColumn = null;

        if (change.getNextColumn() == null) {
            getSqlBuilder().addColumn(currentModel, changedTable, change.getNewColumn());
        } else {
            if (change.getPreviousColumn() != null) {
                prevColumn = changedTable.findColumn(change.getPreviousColumn(), isDelimitedIdentifierModeOn());
            }
            ((FirebirdBuilder) getSqlBuilder()).insertColumn(currentModel,
                changedTable,
                change.getNewColumn(),
                prevColumn);
        }
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }

    /**
     * Processes the removal of a column from a table.
     *
     * @param currentModel The current database schema
     * @param params       The parameters used in the creation of new tables. Note that for existing
     *                     tables, the parameters won't be applied
     * @param change       The change object
     */
    public void processChange(Database currentModel,
                              CreationParameters params,
                              RemoveColumnChange change) throws IOException {
        Table changedTable = findChangedTable(currentModel, change);
        Column droppedColumn = changedTable.findColumn(change.getChangedColumn(), isDelimitedIdentifierModeOn());

        ((FirebirdBuilder) getSqlBuilder()).dropColumn(changedTable, droppedColumn);
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }
}
