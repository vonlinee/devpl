package org.apache.ddlutils.alteration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Compares two database models and creates change objects that express how to
 * adapt the first model so that it becomes the second one. Neither of the models
 * are changed in the process, however, it is also assumed that the models do not
 * change in between.
 * @version $Revision: $
 */
public class ModelComparator {
    /**
     * The log for this comparator.
     */
    private final Log _log = LogFactory.getLog(ModelComparator.class);

    /**
     * The platform information.
     */
    private final PlatformInfo platformInfo;
    /**
     * The predicate that defines which changes are supported by the platform.
     */
    private TableDefinitionChangesPredicate tableDefinitionChangesPredicate;
    /**
     * The object clone helper.
     */
    private final CloneHelper _cloneHelper = new CloneHelper();
    /**
     * Whether comparison is case-sensitive.
     */
    private final boolean caseSensitive;
    /**
     * Whether the comparator should generate {@link PrimaryKeyChange} objects.
     */
    private boolean generatePrimaryKeyChanges = true;
    /**
     * Whether {@link RemoveColumnChange} objects for primary key columns are enough or
     * additional primary key change objects are necessary.
     */
    private boolean canDropPrimaryKeyColumns = true;

    /**
     * Creates a new model comparator object.
     * @param platformInfo            The platform info
     * @param tableDefChangePredicate The predicate that defines whether tables changes are supported
     *                                by the platform or not; all changes are supported if this is null
     * @param caseSensitive           Whether comparison is case-sensitive
     */
    public ModelComparator(PlatformInfo platformInfo,
                           TableDefinitionChangesPredicate tableDefChangePredicate,
                           boolean caseSensitive) {
        this.platformInfo = platformInfo;
        this.caseSensitive = caseSensitive;
        tableDefinitionChangesPredicate = tableDefChangePredicate;
    }

    /**
     * Specifies whether the comparator should generate {@link PrimaryKeyChange} objects or a
     * pair of {@link RemovePrimaryKeyChange} and {@link AddPrimaryKeyChange} objects instead.
     * The default value is <code>true</code>.
     * @param generatePrimaryKeyChanges Whether to create {@link PrimaryKeyChange} objects
     */
    public void setGeneratePrimaryKeyChanges(boolean generatePrimaryKeyChanges) {
        this.generatePrimaryKeyChanges = generatePrimaryKeyChanges;
    }

    /**
     * Specifies whether the {@link RemoveColumnChange} are fine even for primary key columns.
     * If the platform cannot drop primary key columns, set this to <code>false</code> and the
     * comparator will create additional primary key changes.
     * The default value is <code>true</code>.
     * @param canDropPrimaryKeyColumns Whether {@link RemoveColumnChange} objects for primary
     *                                 key columns are ok
     */
    public void setCanDropPrimaryKeyColumns(boolean canDropPrimaryKeyColumns) {
        this.canDropPrimaryKeyColumns = canDropPrimaryKeyColumns;
    }

    /**
     * Returns the info object for the platform.
     * @return The platform info object
     */
    protected PlatformInfo getPlatformInfo() {
        return platformInfo;
    }

    /**
     * Determines whether comparison should be case-sensitive.
     * @return <code>true</code> if case matters
     */
    protected boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
     * Compares the two models and returns the changes necessary to create the second
     * model from the first one.
     * @param sourceModel The source model
     * @param targetModel The target model
     * @return The changes
     */
    public List<ModelChange> compare(Database sourceModel, Database targetModel) {
        Database intermediateModel = _cloneHelper.clone(sourceModel);
        return compareModels(sourceModel, intermediateModel, targetModel);
    }

    /**
     * Compares the given source and target models and creates change objects to get from
     * the source to the target one. These changes will be applied to the given
     * intermediate model (the other two won't be changed), so that it will be equal to
     * the target model after this model has finished.
     * @param sourceModel       The source model
     * @param intermediateModel The intermediate model to apply the changes to
     * @param targetModel       The target model
     * @return The changes
     */
    protected List<ModelChange> compareModels(Database sourceModel,
                                              Database intermediateModel,
                                              Database targetModel) {
        ArrayList<ModelChange> changes = new ArrayList<>();

        changes.addAll(checkForRemovedForeignKeys(sourceModel, intermediateModel, targetModel));
        changes.addAll(checkForRemovedTables(sourceModel, intermediateModel, targetModel));

        for (int tableIdx = 0; tableIdx < intermediateModel.getTableCount(); tableIdx++) {
            Table intermediateTable = intermediateModel.getTable(tableIdx);
            Table sourceTable = sourceModel.findTable(intermediateTable.getName(), caseSensitive);
            Table targetTable = targetModel.findTable(intermediateTable.getName(), caseSensitive);
            List<TableChange> tableChanges = compareTables(sourceModel, sourceTable,
                    intermediateModel, intermediateTable,
                    targetModel, targetTable);

            changes.addAll(tableChanges);
        }

        changes.addAll(checkForAddedTables(sourceModel, intermediateModel, targetModel));
        changes.addAll(checkForAddedForeignKeys(sourceModel, intermediateModel, targetModel));
        return changes;
    }

    /**
     * Creates change objects for foreign keys that are present in the given source model but are no longer in the target
     * model, and applies them to the given intermediate model.
     * @param sourceModel       The source model
     * @param intermediateModel The intermediate model to apply the changes to
     * @param targetModel       The target model
     * @return The changes
     */
    protected List<TableChange> checkForRemovedForeignKeys(Database sourceModel,
                                                           Database intermediateModel,
                                                           Database targetModel) {
        List<TableChange> changes = new ArrayList<>();

        for (int tableIdx = 0; tableIdx < intermediateModel.getTableCount(); tableIdx++) {
            Table intermediateTable = intermediateModel.getTable(tableIdx);
            Table targetTable = targetModel.findTable(intermediateTable.getName(), caseSensitive);
            ForeignKey[] intermediateFks = intermediateTable.getForeignKeys();

            // Dropping foreign keys from tables to be removed might not be necessary, but some databases might require it
            for (ForeignKey sourceFk : intermediateFks) {
                ForeignKey targetFk = targetTable == null ? null : findCorrespondingForeignKey(targetTable, sourceFk);

                if (targetFk == null) {
                    if (_log.isInfoEnabled()) {
                        _log.info("Foreign key " + sourceFk + " needs to be removed from table " + intermediateTable.getName());
                    }

                    RemoveForeignKeyChange fkChange = new RemoveForeignKeyChange(intermediateTable.getName(), sourceFk);

                    changes.add(fkChange);
                    fkChange.apply(intermediateModel, caseSensitive);
                }
            }
        }
        return changes;
    }

    /**
     * Creates change objects for foreign keys that are not present in the given source model but are in the target
     * model, and applies them to the given intermediate model.
     * @param sourceModel       The source model
     * @param intermediateModel The intermediate model to apply the changes to
     * @param targetModel       The target model
     * @return The changes
     */
    protected List<TableChange> checkForAddedForeignKeys(Database sourceModel,
                                                         Database intermediateModel,
                                                         Database targetModel) {
        List<TableChange> changes = new ArrayList<>();

        for (int tableIdx = 0; tableIdx < targetModel.getTableCount(); tableIdx++) {
            Table targetTable = targetModel.getTable(tableIdx);
            Table intermediateTable = intermediateModel.findTable(targetTable.getName(), caseSensitive);

            for (int fkIdx = 0; fkIdx < targetTable.getForeignKeyCount(); fkIdx++) {
                ForeignKey targetFk = targetTable.getForeignKey(fkIdx);
                ForeignKey intermediateFk = findCorrespondingForeignKey(intermediateTable, targetFk);

                if (intermediateFk == null) {
                    if (_log.isInfoEnabled()) {
                        _log.info("Foreign key " + targetFk + " needs to be added to table " + intermediateTable.getName());
                    }

                    intermediateFk = _cloneHelper.clone(targetFk, intermediateTable, intermediateModel, caseSensitive);

                    AddForeignKeyChange fkChange = new AddForeignKeyChange(intermediateTable.getName(), intermediateFk);

                    changes.add(fkChange);
                    fkChange.apply(intermediateModel, caseSensitive);
                }
            }
        }
        return changes;
    }

    /**
     * Creates change objects for tables that are present in the given source model but are no longer in the target
     * model, and applies them to the given intermediate model.
     * @param sourceModel       The source model
     * @param intermediateModel The intermediate model to apply the changes to
     * @param targetModel       The target model
     * @return The changes
     */
    protected List<TableChange> checkForRemovedTables(Database sourceModel,
                                                      Database intermediateModel,
                                                      Database targetModel) {
        List<TableChange> changes = new ArrayList<>();
        Table[] intermediateTables = intermediateModel.getTables();

        for (Table intermediateTable : intermediateTables) {
            Table targetTable = targetModel.findTable(intermediateTable.getName(), caseSensitive);

            if (targetTable == null) {
                if (_log.isInfoEnabled()) {
                    _log.info("Table " + intermediateTable.getName() + " needs to be removed");
                }

                RemoveTableChange tableChange = new RemoveTableChange(intermediateTable.getName());

                changes.add(tableChange);
                tableChange.apply(intermediateModel, caseSensitive);
            }
        }
        return changes;
    }

    /**
     * Creates change objects for tables that are not present in the given source model but are in the target
     * model, and applies them to the given intermediate model.
     * @param sourceModel       The source model
     * @param intermediateModel The intermediate model to apply the changes to
     * @param targetModel       The target model
     * @return The changes
     */
    protected List<ModelChange> checkForAddedTables(Database sourceModel,
                                                    Database intermediateModel,
                                                    Database targetModel) {
        List<ModelChange> changes = new ArrayList<>();

        for (int tableIdx = 0; tableIdx < targetModel.getTableCount(); tableIdx++) {
            Table targetTable = targetModel.getTable(tableIdx);
            Table intermediateTable = intermediateModel.findTable(targetTable.getName(), caseSensitive);

            if (intermediateTable == null) {
                if (_log.isInfoEnabled()) {
                    _log.info("Table " + targetTable.getName() + " needs to be added");
                }

                // we're using a clone of the target table, and remove all foreign
                // keys as these will be added later
                intermediateTable = _cloneHelper.clone(targetTable, true, false, intermediateModel, caseSensitive);

                AddTableChange tableChange = new AddTableChange(intermediateTable);

                changes.add(tableChange);
                tableChange.apply(intermediateModel, caseSensitive);
            }
        }
        return changes;
    }

    /**
     * Compares the two tables and returns the changes necessary to create the second
     * table from the first one.
     * @param sourceModel       The source model
     * @param sourceTable       The source table
     * @param intermediateModel The intermediate model to which the changes will be applied incrementally
     * @param intermediateTable The table corresponding to the source table in the intermediate model
     * @param targetModel       The target model which contains the target table
     * @param targetTable       The target table
     * @return The changes
     */
    protected List<TableChange> compareTables(Database sourceModel,
                                              Table sourceTable,
                                              Database intermediateModel,
                                              Table intermediateTable,
                                              Database targetModel,
                                              Table targetTable) {

        List<TableChange> changes = new ArrayList<>(checkForRemovedIndexes(sourceModel, sourceTable, intermediateModel, intermediateTable, targetModel, targetTable));

        ArrayList<TableChange> tableDefinitionChanges = new ArrayList<>();
        Table tmpTable = _cloneHelper.clone(intermediateTable, true, false, intermediateModel, caseSensitive);

        tableDefinitionChanges.addAll(checkForRemovedColumns(sourceModel, sourceTable, intermediateModel, intermediateTable, targetModel, targetTable));
        tableDefinitionChanges.addAll(checkForChangeOfColumnOrder(sourceModel, sourceTable, intermediateModel, intermediateTable, targetModel, targetTable));
        tableDefinitionChanges.addAll(checkForChangedColumns(sourceModel, sourceTable, intermediateModel, intermediateTable, targetModel, targetTable));
        tableDefinitionChanges.addAll(checkForAddedColumns(sourceModel, sourceTable, intermediateModel, intermediateTable, targetModel, targetTable));
        tableDefinitionChanges.addAll(checkForPrimaryKeyChanges(sourceModel, sourceTable, intermediateModel, intermediateTable, targetModel, targetTable));

        // TODO: check for foreign key changes (on delete/on update)
        if (!tableDefinitionChanges.isEmpty()) {
            if ((tableDefinitionChangesPredicate == null) || tableDefinitionChangesPredicate.areSupported(tmpTable, tableDefinitionChanges)) {
                changes.addAll(tableDefinitionChanges);
            } else {
                // we need to recreate the table; for this to work we need to remove foreign keys to and from the table
                // however, we don't have to add them back here as there is a check for added foreign keys/indexes
                // later on anyway
                // we also don't have to drop indexes on the original table

                ForeignKey[] fks = intermediateTable.getForeignKeys();

                for (ForeignKey fk : fks) {
                    RemoveForeignKeyChange fkChange = new RemoveForeignKeyChange(intermediateTable.getName(), fk);

                    changes.add(fkChange);
                    fkChange.apply(intermediateModel, caseSensitive);
                }
                for (int tableIdx = 0; tableIdx < intermediateModel.getTableCount(); tableIdx++) {
                    Table curTable = intermediateModel.getTable(tableIdx);

                    if (curTable != intermediateTable) {
                        ForeignKey[] curFks = curTable.getForeignKeys();

                        for (ForeignKey curFk : curFks) {
                            if ((caseSensitive && curFk.getForeignTableName()
                                    .equals(intermediateTable.getName())) ||
                                    (!caseSensitive && curFk.getForeignTableName()
                                            .equalsIgnoreCase(intermediateTable.getName()))) {
                                RemoveForeignKeyChange fkChange = new RemoveForeignKeyChange(curTable.getName(), curFk);

                                changes.add(fkChange);
                                fkChange.apply(intermediateModel, caseSensitive);
                            }
                        }
                    }
                }

                RecreateTableChange tableChange = new RecreateTableChange(intermediateTable.getName(),
                        intermediateTable,
                        new ArrayList<>(tableDefinitionChanges));

                changes.add(tableChange);
                tableChange.apply(intermediateModel, caseSensitive);
            }
        }

        changes.addAll(checkForAddedIndexes(sourceModel, sourceTable, intermediateModel, intermediateTable, targetModel, targetTable));

        return changes;
    }

    /**
     * Returns the names of the columns in the intermediate table corresponding to the given column objects.
     * @param columns           The column objects
     * @param intermediateTable The intermediate table
     * @return The column names
     */
    protected String[] getIntermediateColumnNamesFor(Column[] columns, Table intermediateTable) {
        String[] result = new String[columns.length];

        for (int idx = 0; idx < columns.length; idx++) {
            result[idx] = intermediateTable.findColumn(columns[idx].getName(), caseSensitive).getName();
        }
        return result;
    }

    /**
     * Creates change objects for indexes that are present in the given source table but are no longer in the target
     * table, and applies them to the given intermediate model.
     * @param sourceModel       The source model
     * @param sourceTable       The source table
     * @param intermediateModel The intermediate model to apply the changes to
     * @param intermediateTable The table from the intermediate model corresponding to the source table
     * @param targetModel       The target model
     * @param targetTable       The target table
     * @return The changes
     */
    protected List<TableChange> checkForRemovedIndexes(Database sourceModel,
                                                       Table sourceTable,
                                                       Database intermediateModel,
                                                       Table intermediateTable,
                                                       Database targetModel,
                                                       Table targetTable) {
        List<TableChange> changes = new ArrayList<>();
        Index[] indexes = intermediateTable.getIndices();

        for (Index sourceIndex : indexes) {
            Index targetIndex = findCorrespondingIndex(targetTable, sourceIndex);

            if (targetIndex == null) {
                if (_log.isInfoEnabled()) {
                    _log.info("Index " + sourceIndex.getName() + " needs to be removed from table " + intermediateTable.getName());
                }

                RemoveIndexChange change = new RemoveIndexChange(intermediateTable.getName(), sourceIndex);

                changes.add(change);
                change.apply(intermediateModel, caseSensitive);
            }
        }
        return changes;
    }

    /**
     * Creates change objects for indexes that are not present in the given source table but are in the target
     * table, and applies them to the given intermediate model.
     * @param sourceModel       The source model
     * @param sourceTable       The source table
     * @param intermediateModel The intermediate model to apply the changes to
     * @param intermediateTable The table from the intermediate model corresponding to the source table
     * @param targetModel       The target model
     * @param targetTable       The target table
     * @return The changes
     */
    protected List<TableChange> checkForAddedIndexes(Database sourceModel,
                                                     Table sourceTable,
                                                     Database intermediateModel,
                                                     Table intermediateTable,
                                                     Database targetModel,
                                                     Table targetTable) {
        List<TableChange> changes = new ArrayList<>();

        for (int indexIdx = 0; indexIdx < targetTable.getIndexCount(); indexIdx++) {
            Index targetIndex = targetTable.getIndex(indexIdx);
            Index intermediateIndex = findCorrespondingIndex(intermediateTable, targetIndex);
            Index sourceIndex = findCorrespondingIndex(sourceTable, targetIndex);

            if ((sourceIndex == null) && (intermediateIndex == null)) {
                if (_log.isInfoEnabled()) {
                    _log.info("Index " + targetIndex.getName() + " needs to be created for table " + intermediateTable.getName());
                }

                Index clonedIndex = _cloneHelper.clone(targetIndex, intermediateTable, caseSensitive);
                AddIndexChange change = new AddIndexChange(intermediateTable.getName(), clonedIndex);

                changes.add(change);
                change.apply(intermediateModel, caseSensitive);
            }
        }
        return changes;
    }

    /**
     * Checks for changes in the column order between the given source and target table, creates change objects for these and
     * applies them to the given intermediate model.
     * @param sourceModel       The source model
     * @param sourceTable       The source table
     * @param intermediateModel The intermediate model to apply the changes to
     * @param intermediateTable The table from the intermediate model corresponding to the source table
     * @param targetModel       The target model
     * @param targetTable       The target table
     * @return The changes
     */
    protected List<TableChange> checkForChangeOfColumnOrder(Database sourceModel,
                                                            Table sourceTable,
                                                            Database intermediateModel,
                                                            Table intermediateTable,
                                                            Database targetModel,
                                                            Table targetTable) {
        List<TableChange> changes = new ArrayList<>();
        List<Column> targetOrder = new ArrayList<>();
        int numChangedPKs = 0;

        for (int columnIdx = 0; columnIdx < targetTable.getColumnCount(); columnIdx++) {
            Column targetColumn = targetTable.getColumn(columnIdx);
            Column sourceColumn = intermediateTable.findColumn(targetColumn.getName(), caseSensitive);

            if (sourceColumn != null) {
                targetOrder.add(sourceColumn);
            }
        }

        HashMap<String, Integer> newPositions = new HashMap<>();
        for (int columnIdx = 0; columnIdx < intermediateTable.getColumnCount(); columnIdx++) {
            Column sourceColumn = intermediateTable.getColumn(columnIdx);
            int targetIdx = targetOrder.indexOf(sourceColumn);

            if ((targetIdx >= 0) && (targetIdx != columnIdx)) {
                newPositions.put(sourceColumn.getName(), targetIdx);
                if (sourceColumn.isPrimaryKey()) {
                    numChangedPKs++;
                }
            }
        }

        if (!newPositions.isEmpty()) {
            ColumnOrderChange change = new ColumnOrderChange(intermediateTable.getName(), newPositions);
            change.apply(intermediateModel, caseSensitive);
            if (numChangedPKs > 1) {
                // create pk change that only covers the order change
                // fortunately, the order change will have adjusted the pk order already
                changes.add(new PrimaryKeyChange(intermediateTable.getName(),
                        getIntermediateColumnNamesFor(intermediateTable.getPrimaryKeyColumns(), intermediateTable)));
            }
            changes.add(change);
        }
        return changes;
    }

    /**
     * Creates change objects for columns that are present in the given source table but are no longer in the target
     * table, and applies them to the given intermediate model.
     * @param sourceModel       The source model
     * @param sourceTable       The source table
     * @param intermediateModel The intermediate model to apply the changes to
     * @param intermediateTable The table from the intermediate model corresponding to the source table
     * @param targetModel       The target model
     * @param targetTable       The target table
     * @return The changes
     */
    protected List<TableChange> checkForRemovedColumns(Database sourceModel,
                                                       Table sourceTable,
                                                       Database intermediateModel,
                                                       Table intermediateTable,
                                                       Database targetModel,
                                                       Table targetTable) {
        // if the platform does not support dropping pk columns, then the pk handling above will
        // generate appropriate pk changes

        List<TableChange> changes = new ArrayList<>();
        Column[] columns = intermediateTable.getColumns();

        for (Column sourceColumn : columns) {
            Column targetColumn = targetTable.findColumn(sourceColumn.getName(), caseSensitive);

            if (targetColumn == null) {
                if (_log.isInfoEnabled()) {
                    _log.info("Column " + sourceColumn.getName() + " needs to be removed from table " + intermediateTable.getName());
                }

                RemoveColumnChange change = new RemoveColumnChange(intermediateTable.getName(), sourceColumn.getName());

                changes.add(change);
                change.apply(intermediateModel, caseSensitive);
            }
        }
        return changes;
    }

    /**
     * Creates change objects for columns that are not present in the given source table but are in the target
     * table, and applies them to the given intermediate model.
     * @param sourceModel       The source model
     * @param sourceTable       The source table
     * @param intermediateModel The intermediate model to apply the changes to
     * @param intermediateTable The table from the intermediate model corresponding to the source table
     * @param targetModel       The target model
     * @param targetTable       The target table
     * @return The changes
     */
    protected List<TableChange> checkForAddedColumns(Database sourceModel,
                                                     Table sourceTable,
                                                     Database intermediateModel,
                                                     Table intermediateTable,
                                                     Database targetModel,
                                                     Table targetTable) {
        List<TableChange> changes = new ArrayList<>();

        for (int columnIdx = 0; columnIdx < targetTable.getColumnCount(); columnIdx++) {
            Column targetColumn = targetTable.getColumn(columnIdx);
            Column sourceColumn = intermediateTable.findColumn(targetColumn.getName(), caseSensitive);

            if (sourceColumn == null) {
                String prevColumn = (columnIdx > 0 ? intermediateTable.getColumn(columnIdx - 1).getName() : null);
                String nextColumn = (columnIdx < intermediateTable.getColumnCount() ? intermediateTable
                        .getColumn(columnIdx).getName() : null);
                Column clonedColumn = _cloneHelper.clone(targetColumn, false);
                AddColumnChange change = new AddColumnChange(intermediateTable.getName(), clonedColumn, prevColumn, nextColumn);

                changes.add(change);
                change.apply(intermediateModel, caseSensitive);
            }
        }
        return changes;
    }

    /**
     * Creates change objects for columns that have a different in the given source and target table, and applies them
     * to the given intermediate model.
     * @param sourceModel       The source model
     * @param sourceTable       The source table
     * @param intermediateModel The intermediate model to apply the changes to
     * @param intermediateTable The table from the intermediate model corresponding to the source table
     * @param targetModel       The target model
     * @param targetTable       The target table
     * @return The changes
     */
    protected List<TableChange> checkForChangedColumns(Database sourceModel,
                                                       Table sourceTable,
                                                       Database intermediateModel,
                                                       Table intermediateTable,
                                                       Database targetModel,
                                                       Table targetTable) {
        List<TableChange> changes = new ArrayList<>();

        for (int columnIdx = 0; columnIdx < targetTable.getColumnCount(); columnIdx++) {
            Column targetColumn = targetTable.getColumn(columnIdx);
            Column sourceColumn = intermediateTable.findColumn(targetColumn.getName(), caseSensitive);

            if (sourceColumn != null) {
                ColumnDefinitionChange change = compareColumns(intermediateTable, sourceColumn, targetTable, targetColumn);

                if (change != null) {
                    changes.add(change);
                    change.apply(intermediateModel, caseSensitive);
                }
            }
        }
        return changes;
    }

    /**
     * Creates change objects for primary key differences (primary key added/removed/changed), and applies them to the given intermediate model.
     * @param sourceModel       The source model
     * @param sourceTable       The source table
     * @param intermediateModel The intermediate model to apply the changes to
     * @param intermediateTable The table from the intermediate model corresponding to the source table
     * @param targetModel       The target model
     * @param targetTable       The target table
     * @return The changes
     */
    protected List<TableChange> checkForPrimaryKeyChanges(Database sourceModel,
                                                          Table sourceTable,
                                                          Database intermediateModel,
                                                          Table intermediateTable,
                                                          Database targetModel,
                                                          Table targetTable) {
        List<TableChange> changes = new ArrayList<>();
        Column[] sourcePK = sourceTable.getPrimaryKeyColumns();
        Column[] curPK = intermediateTable.getPrimaryKeyColumns();
        Column[] targetPK = targetTable.getPrimaryKeyColumns();

        if ((curPK.length == 0) && (targetPK.length > 0)) {
            if (_log.isInfoEnabled()) {
                _log.info("A primary key needs to be added to the table " + intermediateTable.getName());
            }

            AddPrimaryKeyChange change = new AddPrimaryKeyChange(intermediateTable.getName(), getIntermediateColumnNamesFor(targetPK, intermediateTable));

            changes.add(change);
            change.apply(intermediateModel, caseSensitive);
        } else if ((targetPK.length == 0) && (curPK.length > 0)) {
            if (_log.isInfoEnabled()) {
                _log.info("The primary key needs to be removed from the table " + intermediateTable.getName());
            }

            RemovePrimaryKeyChange change = new RemovePrimaryKeyChange(intermediateTable.getName());

            changes.add(change);
            change.apply(intermediateModel, caseSensitive);
        } else {
            boolean changePK = false;

            if ((curPK.length != targetPK.length) || (!canDropPrimaryKeyColumns && sourcePK.length > targetPK.length)) {
                changePK = true;
            } else if (curPK.length > 0) {
                for (int pkColumnIdx = 0; (pkColumnIdx < curPK.length) && !changePK; pkColumnIdx++) {
                    if (!StringUtils.equals(curPK[pkColumnIdx].getName(), targetPK[pkColumnIdx].getName(), caseSensitive)) {
                        changePK = true;
                    }
                }
            }
            if (changePK) {
                if (_log.isInfoEnabled()) {
                    _log.info("The primary key of table " + intermediateTable.getName() + " needs to be changed");
                }
                if (generatePrimaryKeyChanges) {
                    PrimaryKeyChange change = new PrimaryKeyChange(intermediateTable.getName(),
                            getIntermediateColumnNamesFor(targetPK, intermediateTable));

                    changes.add(change);
                    change.apply(intermediateModel, true);
                } else {
                    RemovePrimaryKeyChange removePKChange = new RemovePrimaryKeyChange(intermediateTable.getName());
                    AddPrimaryKeyChange addPKChange = new AddPrimaryKeyChange(intermediateTable.getName(),
                            getIntermediateColumnNamesFor(targetPK, intermediateTable));

                    changes.add(removePKChange);
                    changes.add(addPKChange);
                    removePKChange.apply(intermediateModel, caseSensitive);
                    addPKChange.apply(intermediateModel, caseSensitive);
                }
            }
        }
        return changes;
    }

    /**
     * Compares the two columns and returns the change necessary to create the second
     * column from the first one if they differ.
     * @param sourceTable  The source table which contains the source column
     * @param sourceColumn The source column
     * @param targetTable  The target table which contains the target column
     * @param targetColumn The target column
     * @return The change or <code>null</code> if the columns are the same
     */
    protected ColumnDefinitionChange compareColumns(Table sourceTable,
                                                    Column sourceColumn,
                                                    Table targetTable,
                                                    Column targetColumn) {
        if (ColumnDefinitionChange.isChanged(getPlatformInfo(), sourceColumn, targetColumn)) {
            Column newColumnDef = _cloneHelper.clone(sourceColumn, true);
            int targetTypeCode = platformInfo.getTargetJdbcType(targetColumn.getJdbcTypeCode());
            boolean sizeMatters = platformInfo.hasSize(targetTypeCode);
            boolean scaleMatters = platformInfo.hasPrecisionAndScale(targetTypeCode);

            newColumnDef.setJdbcTypeCode(targetColumn.getJdbcTypeCode());
            newColumnDef.setSize(sizeMatters || scaleMatters ? targetColumn.getSize() : null);
            newColumnDef.setAutoIncrement(targetColumn.isAutoIncrement());
            newColumnDef.setRequired(targetColumn.isRequired());
            newColumnDef.setDescription(targetColumn.getDescription());
            newColumnDef.setDefaultValue(targetColumn.getDefaultValue());
            return new ColumnDefinitionChange(sourceTable.getName(), sourceColumn.getName(), newColumnDef);
        }
        return null;
    }

    /**
     * Searches in the given table for a corresponding foreign key. If the given key
     * has no name, then a foreign key to the same table with the same columns (but not
     * necessarily in the same order) is searched. If the given key has a name, then the
     * corresponding key also needs to have the same name, or no name at all, but not a
     * different one.
     * @param table The table to search in
     * @param fk    The original foreign key
     * @return The corresponding foreign key if found
     */
    protected ForeignKey findCorrespondingForeignKey(Table table, ForeignKey fk) {
        for (int fkIdx = 0; fkIdx < table.getForeignKeyCount(); fkIdx++) {
            ForeignKey curFk = table.getForeignKey(fkIdx);

            if ((caseSensitive && fk.equals(curFk)) ||
                    (!caseSensitive && fk.equalsIgnoreCase(curFk))) {
                return curFk;
            }
        }
        return null;
    }

    /**
     * Searches in the given table for a corresponding index. If the given index
     * has no name, then an index to the same table with the same columns in the
     * same order is searched. If the given index has a name, then the corresponding
     * index also needs to have the same name, or no name at all, but not a different one.
     * @param table The table to search in
     * @param index The original index
     * @return The corresponding index if found
     */
    protected Index findCorrespondingIndex(Table table, Index index) {
        for (int indexIdx = 0; indexIdx < table.getIndexCount(); indexIdx++) {
            Index curIndex = table.getIndex(indexIdx);

            if ((caseSensitive && index.equals(curIndex)) ||
                    (!caseSensitive && index.equalsIgnoreCase(curIndex))) {
                return curIndex;
            }
        }
        return null;
    }
}
