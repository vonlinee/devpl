package org.apache.ddlutils.platform.mssql;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.alteration.*;
import org.apache.ddlutils.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A model comparator customized for Sql Server.
 * @version $Revision: $
 */
public class MSSqlModelComparator extends ModelComparator {
    /**
     * Creates a new Sql Server model comparator object.
     * @param platformInfo            The platform info
     * @param tableDefChangePredicate The predicate that defines whether tables changes are supported
     *                                by the platform or not; all changes are supported if this is null
     * @param caseSensitive           Whether comparison is case-sensitive
     */
    public MSSqlModelComparator(PlatformInfo platformInfo,
                                TableDefinitionChangesPredicate tableDefChangePredicate,
                                boolean caseSensitive) {
        super(platformInfo, tableDefChangePredicate, caseSensitive);
        setGeneratePrimaryKeyChanges(false);
        setCanDropPrimaryKeyColumns(false);
    }

    @Override
    protected List<TableChange> checkForPrimaryKeyChanges(Database sourceModel,
                                                          Table sourceTable,
                                                          Database intermediateModel,
                                                          Table intermediateTable,
                                                          Database targetModel,
                                                          Table targetTable) {
        List<TableChange> changes = super.checkForPrimaryKeyChanges(sourceModel, sourceTable, intermediateModel, intermediateTable, targetModel, targetTable);

        // now we add pk changes if one of the pk columns was changed
        // we only need to do this if there is no other pk change (which can only be a remove or add change or both)
        if (changes.isEmpty()) {
            List<Column> columns = getRelevantChangedColumns(sourceTable, targetTable);

            for (Column targetColumn : columns) {
                if (targetColumn.isPrimaryKey()) {
                    changes.add(new RemovePrimaryKeyChange(sourceTable.getName()));
                    changes.add(new AddPrimaryKeyChange(sourceTable.getName(), sourceTable.getPrimaryKeyColumnNames()));
                    break;
                }
            }
        }
        return changes;
    }

    @Override
    protected List<TableChange> checkForRemovedIndexes(Database sourceModel,
                                                       Table sourceTable,
                                                       Database intermediateModel,
                                                       Table intermediateTable,
                                                       Database targetModel,
                                                       Table targetTable) {
        List<TableChange> changes = super.checkForRemovedIndexes(sourceModel, sourceTable, intermediateModel, intermediateTable, targetModel, targetTable);
        Index[] targetIndexes = targetTable.getIndices();
        List<RemoveIndexChange> additionalChanges = new ArrayList<>();

        // removing all indexes that are maintained and that use a changed column
        if (targetIndexes.length > 0) {
            List<Column> columns = getRelevantChangedColumns(sourceTable, targetTable);

            if (!columns.isEmpty()) {
                for (Index targetIndex : targetIndexes) {
                    Index sourceIndex = findCorrespondingIndex(sourceTable, targetIndex);

                    if (sourceIndex != null) {
                        for (Column targetColumn : columns) {
                            if (targetIndex.hasColumn(targetColumn)) {
                                additionalChanges.add(new RemoveIndexChange(intermediateTable.getName(), targetIndex));
                                break;
                            }
                        }
                    }
                }
                for (RemoveIndexChange additionalChange : additionalChanges) {
                    additionalChange.apply(intermediateModel, isCaseSensitive());
                }
                changes.addAll(additionalChanges);
            }
        }
        return changes;
    }

    @Override
    protected List<TableChange> checkForAddedIndexes(Database sourceModel,
                                                     Table sourceTable,
                                                     Database intermediateModel,
                                                     Table intermediateTable,
                                                     Database targetModel,
                                                     Table targetTable) {
        List<TableChange> changes = super.checkForAddedIndexes(sourceModel, sourceTable, intermediateModel, intermediateTable, targetModel, targetTable);
        Index[] targetIndexes = targetTable.getIndices();
        List<AddIndexChange> additionalChanges = new ArrayList<>();

        // re-adding all indexes that are maintained and that use a changed column
        if (targetIndexes.length > 0) {
            for (Index targetIndex : targetIndexes) {
                Index sourceIndex = findCorrespondingIndex(sourceTable, targetIndex);
                Index intermediateIndex = findCorrespondingIndex(intermediateTable, targetIndex);

                if ((sourceIndex != null) && (intermediateIndex == null)) {
                    additionalChanges.add(new AddIndexChange(intermediateTable.getName(), targetIndex));
                }
            }
            for (AddIndexChange additionalChange : additionalChanges) {
                additionalChange.apply(intermediateModel, isCaseSensitive());
            }
            changes.addAll(additionalChanges);
        }
        return changes;
    }

    @Override
    protected List<TableChange> checkForRemovedForeignKeys(Database sourceModel,
                                                           Database intermediateModel,
                                                           Database targetModel) {
        List<TableChange> changes = super.checkForRemovedForeignKeys(sourceModel, intermediateModel, targetModel);
        List<RemoveForeignKeyChange> additionalChanges = new ArrayList<>();

        // removing all foreign keys that are maintained and that use a changed column
        for (int tableIdx = 0; tableIdx < targetModel.getTableCount(); tableIdx++) {
            Table targetTable = targetModel.getTable(tableIdx);
            Table sourceTable = sourceModel.findTable(targetTable.getName(), isCaseSensitive());

            if (sourceTable != null) {
                List<Column> columns = getRelevantChangedColumns(sourceTable, targetTable);

                if (!columns.isEmpty()) {
                    for (int fkIdx = 0; fkIdx < targetTable.getForeignKeyCount(); fkIdx++) {
                        ForeignKey targetFk = targetTable.getForeignKey(fkIdx);
                        ForeignKey sourceFk = findCorrespondingForeignKey(sourceTable, targetFk);

                        if (sourceFk != null) {
                            for (Column targetColumn : columns) {
                                if (targetFk.hasLocalColumn(targetColumn) || targetFk.hasForeignColumn(targetColumn)) {
                                    additionalChanges.add(new RemoveForeignKeyChange(sourceTable.getName(), targetFk));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        for (RemoveForeignKeyChange additionalChange : additionalChanges) {
            additionalChange.apply(intermediateModel, isCaseSensitive());
        }
        changes.addAll(additionalChanges);
        return changes;
    }

    @Override
    protected List<TableChange> checkForAddedForeignKeys(Database sourceModel,
                                                         Database intermediateModel,
                                                         Database targetModel) {
        List<TableChange> changes = super.checkForAddedForeignKeys(sourceModel, intermediateModel, targetModel);
        List<AddForeignKeyChange> additionalChanges = new ArrayList<>();

        // re-adding all foreign keys that are maintained and that use a changed column
        for (int tableIdx = 0; tableIdx < targetModel.getTableCount(); tableIdx++) {
            Table targetTable = targetModel.getTable(tableIdx);
            Table sourceTable = sourceModel.findTable(targetTable.getName(), isCaseSensitive());
            Table intermediateTable = intermediateModel.findTable(targetTable.getName(), isCaseSensitive());

            if (sourceTable != null) {
                for (int fkIdx = 0; fkIdx < targetTable.getForeignKeyCount(); fkIdx++) {
                    ForeignKey targetFk = targetTable.getForeignKey(fkIdx);
                    ForeignKey sourceFk = findCorrespondingForeignKey(sourceTable, targetFk);
                    ForeignKey intermediateFk = findCorrespondingForeignKey(intermediateTable, targetFk);

                    if ((sourceFk != null) && (intermediateFk == null)) {
                        additionalChanges.add(new AddForeignKeyChange(intermediateTable.getName(), targetFk));
                    }
                }
            }
        }
        for (AddForeignKeyChange additionalChange : additionalChanges) {
            additionalChange.apply(intermediateModel, isCaseSensitive());
        }
        changes.addAll(additionalChanges);
        return changes;
    }

    /**
     * Returns all columns that are changed in a way that makes it necessary to recreate foreign keys and
     * indexes using them.
     * @param sourceTable The source table
     * @param targetTable The target table
     * @return The columns (from the target table)
     */
    private List<Column> getRelevantChangedColumns(Table sourceTable, Table targetTable) {
        List<Column> result = new ArrayList<>();

        for (int columnIdx = 0; columnIdx < targetTable.getColumnCount(); columnIdx++) {
            Column targetColumn = targetTable.getColumn(columnIdx);
            Column sourceColumn = sourceTable.findColumn(targetColumn.getName(), isCaseSensitive());

            if (sourceColumn != null) {
                int targetTypeCode = getPlatformInfo().getTargetJdbcType(targetColumn.getJdbcTypeCode());

                if ((targetTypeCode != sourceColumn.getJdbcTypeCode()) ||
                    ColumnDefinitionChange.isSizeChanged(getPlatformInfo(), sourceColumn, targetColumn)) {
                    result.add(targetColumn);
                }
            }
        }
        return result;
    }
}
