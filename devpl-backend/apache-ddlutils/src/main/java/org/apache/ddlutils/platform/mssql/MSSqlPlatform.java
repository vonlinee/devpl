package org.apache.ddlutils.platform.mssql;


import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.alteration.*;
import org.apache.ddlutils.model.CascadeActionEnum;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.*;
import org.apache.ddlutils.util.StringUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;

/**
 * The platform implementation for the Microsoft SQL Server database.
 */
public class MSSqlPlatform extends PlatformImplBase {

    /**
     * Creates a new platform instance.
     */
    public MSSqlPlatform() {
        PlatformInfo info = getPlatformInfo();

        info.setMaxIdentifierLength(128);
        info.setPrimaryKeyColumnAutomaticallyRequired(true);
        info.setIdentityColumnAutomaticallyRequired(true);
        info.setMultipleIdentityColumnsSupported(false);
        info.setSupportedOnUpdateActions(new CascadeActionEnum[]{CascadeActionEnum.CASCADE, CascadeActionEnum.NONE});
        info.addEquivalentOnUpdateActions(CascadeActionEnum.NONE, CascadeActionEnum.RESTRICT);
        info.setSupportedOnDeleteActions(new CascadeActionEnum[]{CascadeActionEnum.CASCADE, CascadeActionEnum.NONE});
        info.addEquivalentOnDeleteActions(CascadeActionEnum.NONE, CascadeActionEnum.RESTRICT);

        info.addNativeTypeMapping(Types.ARRAY, "IMAGE", Types.LONGVARBINARY);
        // BIGINT will be mapped back to BIGINT by the model reader
        info.addNativeTypeMapping(Types.BIGINT, "DECIMAL(19,0)");
        info.addNativeTypeMapping(Types.BLOB, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BOOLEAN, "BIT", Types.BIT);
        info.addNativeTypeMapping(Types.CLOB, "TEXT", Types.LONGVARCHAR);
        info.addNativeTypeMapping(Types.DATALINK, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.DATE, "DATETIME", Types.TIMESTAMP);
        info.addNativeTypeMapping(Types.DISTINCT, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.DOUBLE, "FLOAT", Types.FLOAT);
        info.addNativeTypeMapping(Types.INTEGER, "INT");
        info.addNativeTypeMapping(Types.JAVA_OBJECT, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.LONGVARBINARY, "IMAGE");
        info.addNativeTypeMapping(Types.LONGVARCHAR, "TEXT");
        info.addNativeTypeMapping(Types.NULL, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.OTHER, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.REF, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.STRUCT, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.TIME, "DATETIME", Types.TIMESTAMP);
        info.addNativeTypeMapping(Types.TIMESTAMP, "DATETIME");
        info.addNativeTypeMapping(Types.TINYINT, "SMALLINT", Types.SMALLINT);

        info.setDefaultSize(Types.CHAR, 254);
        info.setDefaultSize(Types.VARCHAR, 254);
        info.setDefaultSize(Types.BINARY, 254);
        info.setDefaultSize(Types.VARBINARY, 254);

        setSqlBuilder(new MSSqlBuilder(this));
        setModelReader(new MSSqlModelReader(this));
    }

    @Override
    public DatabaseType getDBType() {
        return BuiltinDatabaseType.MSSQL;
    }

    /**
     * Determines whether we need to use identity override mode for the given table.
     *
     * @param table The table
     * @return <code>true</code> if identity override mode is needed
     */
    private boolean useIdentityOverrideFor(Table table) {
        return isIdentityOverrideOn() &&
               getPlatformInfo().isIdentityOverrideAllowed() &&
               (table.getAutoIncrementColumns().length > 0);
    }

    @Override
    protected void beforeInsert(Connection connection, Table table) throws SQLException {
        if (useIdentityOverrideFor(table)) {
            MSSqlBuilder builder = (MSSqlBuilder) getSqlBuilder();
            connection.createStatement().execute(builder.getEnableIdentityOverrideSql(table));
        }
    }

    @Override
    protected void afterInsert(Connection connection, Table table) throws SQLException {
        if (useIdentityOverrideFor(table)) {
            MSSqlBuilder builder = (MSSqlBuilder) getSqlBuilder();

            connection.createStatement().execute(builder.getDisableIdentityOverrideSql(table));
        }
    }

    @Override
    protected void beforeUpdate(Connection connection, Table table) throws SQLException {
        beforeInsert(connection, table);
    }

    @Override
    protected void afterUpdate(Connection connection, Table table) throws SQLException {
        afterInsert(connection, table);
    }

    @Override
    protected ModelComparator getModelComparator() {
        return new MSSqlModelComparator(getPlatformInfo(), getTableDefinitionChangesPredicate(), isDelimitedIdentifierModeOn());
    }

    @Override
    protected TableDefinitionChangesPredicate getTableDefinitionChangesPredicate() {
        return new DefaultTableDefinitionChangesPredicate() {
            @Override
            protected boolean isSupported(Table intermediateTable, TableChange change) {
                if ((change instanceof RemoveColumnChange) ||
                    (change instanceof AddPrimaryKeyChange) ||
                    (change instanceof PrimaryKeyChange) ||
                    (change instanceof RemovePrimaryKeyChange)) {
                    return true;
                } else if (change instanceof AddColumnChange addColumnChange) {

                    // Sql Server can only add not insert columns, and they cannot be required unless also
                    // auto increment or with a DEFAULT value
                    return (addColumnChange.getNextColumn() == null) &&
                           (!addColumnChange.getNewColumn().isRequired() ||
                            addColumnChange.getNewColumn().isAutoIncrement() ||
                            !StringUtils.isEmpty(addColumnChange.getNewColumn().getDefaultValue()));
                } else if (change instanceof ColumnDefinitionChange colDefChange) {
                    Column curColumn = intermediateTable.findColumn(colDefChange.getChangedColumn(), isDelimitedIdentifierModeOn());
                    Column newColumn = colDefChange.getNewColumn();

                    // Sql Server has no way of adding or removing an IDENTITY constraint
                    // Also, Sql Server cannot handle reducing the size (even with the CAST in place)
                    return (curColumn.isAutoIncrement() == colDefChange.getNewColumn().isAutoIncrement()) &&
                           (curColumn.isRequired() || (curColumn.isRequired() == newColumn.isRequired())) &&
                           !ColumnDefinitionChange.isSizeReduced(getPlatformInfo(), curColumn, newColumn);
                } else {
                    return false;
                }
            }
        };
    }

    @Override
    protected Database processChanges(Database model, Collection<ModelChange> changes, CreationParameters params) throws IOException, DdlUtilsException {
        if (!changes.isEmpty()) {
            ((MSSqlBuilder) getSqlBuilder()).turnOnQuotation();
        }
        return super.processChanges(model, changes, params);
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
        Column removedColumn = changedTable.findColumn(change.getChangedColumn(), isDelimitedIdentifierModeOn());

        ((MSSqlBuilder) getSqlBuilder()).dropColumn(changedTable, removedColumn);
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }

    /**
     * Processes the removal of a primary key from a table.
     *
     * @param currentModel The current database schema
     * @param params       The parameters used in the creation of new tables. Note that for existing
     *                     tables, the parameters won't be applied
     * @param change       The change object
     */
    public void processChange(Database currentModel,
                              CreationParameters params,
                              RemovePrimaryKeyChange change) throws IOException {
        Table changedTable = findChangedTable(currentModel, change);

        ((MSSqlBuilder) getSqlBuilder()).dropPrimaryKey(changedTable);
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }

    /**
     * Processes the change of the column of a table.
     *
     * @param currentModel The current database schema
     * @param params       The parameters used in the creation of new tables. Note that for existing
     *                     tables, the parameters won't be applied
     * @param change       The change object
     */
    public void processChange(Database currentModel,
                              CreationParameters params,
                              ColumnDefinitionChange change) throws IOException {
        Table changedTable = findChangedTable(currentModel, change);
        Column changedColumn = changedTable.findColumn(change.getChangedColumn(), isDelimitedIdentifierModeOn());

        ((MSSqlBuilder) getSqlBuilder()).recreateColumn(changedTable, changedColumn, change.getNewColumn());
    }

    /**
     * Processes the change of the primary key of a table.
     *
     * @param currentModel The current database schema
     * @param params       The parameters used in the creation of new tables. Note that for existing
     *                     tables, the parameters won't be applied
     * @param change       The change object
     */
    public void processChange(Database currentModel,
                              CreationParameters params,
                              PrimaryKeyChange change) throws IOException {
        Table changedTable = findChangedTable(currentModel, change);
        String[] newPKColumnNames = change.getNewPrimaryKeyColumns();
        Column[] newPKColumns = new Column[newPKColumnNames.length];

        for (int colIdx = 0; colIdx < newPKColumnNames.length; colIdx++) {
            newPKColumns[colIdx] = changedTable.findColumn(newPKColumnNames[colIdx], isDelimitedIdentifierModeOn());
        }
        ((MSSqlBuilder) getSqlBuilder()).dropPrimaryKey(changedTable);
        getSqlBuilder().createPrimaryKey(changedTable, newPKColumns);
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }
}
