package org.apache.ddlutils.platform.mysql;


import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.alteration.*;
import org.apache.ddlutils.model.CascadeActionEnum;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.*;

import java.io.IOException;
import java.sql.Types;

/**
 * The platform implementation for MySQL.
 *
 * @version $Revision: 231306 $
 */
public class MySqlPlatform extends PlatformImplBase {

    /**
     * Creates a new platform instance.
     */
    public MySqlPlatform() {
        PlatformInfo info = getPlatformInfo();
        info.setMaxIdentifierLength(64);
        info.setNullAsDefaultValueRequired(true);
        info.setDefaultValuesForLongTypesSupported(false);
        // see http://dev.mysql.com/doc/refman/4.1/en/example-auto-increment.html
        info.setNonPrimaryKeyIdentityColumnsSupported(false);
        info.setMultipleIdentityColumnsSupported(false);
        info.setMixingIdentityAndNormalPrimaryKeyColumnsSupported(false);
        // MySql returns synthetic default values for pk columns
        info.setSyntheticDefaultValueForRequiredReturned(true);
        info.setPrimaryKeyColumnAutomaticallyRequired(true);
        info.setCommentPrefix("#");
        // Double quotes are only allowed for delimiting identifiers if the server SQL mode includes ANSI_QUOTES
        info.setDelimiterToken("`");
        info.setSupportedOnUpdateActions(new CascadeActionEnum[]{CascadeActionEnum.NONE, CascadeActionEnum.RESTRICT,
            CascadeActionEnum.CASCADE, CascadeActionEnum.SET_NULL});
        info.setDefaultOnUpdateAction(CascadeActionEnum.RESTRICT);
        info.setSupportedOnDeleteActions(new CascadeActionEnum[]{CascadeActionEnum.NONE, CascadeActionEnum.RESTRICT,
            CascadeActionEnum.CASCADE, CascadeActionEnum.SET_NULL});
        info.setDefaultOnDeleteAction(CascadeActionEnum.RESTRICT);

        info.addNativeTypeMapping(Types.ARRAY, "LONGBLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BIT, "TINYINT(1)");
        info.addNativeTypeMapping(Types.BLOB, "LONGBLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BOOLEAN, "TINYINT(1)", Types.BIT);
        info.addNativeTypeMapping(Types.CLOB, "LONGTEXT", Types.LONGVARCHAR);
        info.addNativeTypeMapping(Types.DATALINK, "MEDIUMBLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.DISTINCT, "LONGBLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.FLOAT, "DOUBLE", Types.DOUBLE);
        info.addNativeTypeMapping(Types.JAVA_OBJECT, "LONGBLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.LONGVARBINARY, "MEDIUMBLOB");
        info.addNativeTypeMapping(Types.LONGVARCHAR, "MEDIUMTEXT");
        info.addNativeTypeMapping(Types.NULL, "MEDIUMBLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.NUMERIC, "DECIMAL", Types.DECIMAL);
        info.addNativeTypeMapping(Types.OTHER, "LONGBLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.REAL, "FLOAT");
        info.addNativeTypeMapping(Types.REF, "MEDIUMBLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.STRUCT, "LONGBLOB", Types.LONGVARBINARY);
        // Since TIMESTAMP is not a stable datatype yet, and does not support a higher precision
        // than DATETIME (year to seconds) as of MySQL 5, we map the JDBC type here to DATETIME
        // TODO: Make this configurable
        info.addNativeTypeMapping(Types.TIMESTAMP, "DATETIME");
        // In MySql, TINYINT has only a range of -128 to 127
        info.addNativeTypeMapping(Types.TINYINT, "SMALLINT", Types.SMALLINT);

        info.setDefaultSize(Types.CHAR, 254);
        info.setDefaultSize(Types.VARCHAR, 254);
        info.setDefaultSize(Types.BINARY, 254);
        info.setDefaultSize(Types.VARBINARY, 254);

        setSqlBuilder(new MySqlBuilder(this));
        setModelReader(new MySqlModelReader(this));
    }

    @Override
    public DBType getDBType() {
        return DBTypeEnum.MYSQL;
    }

    @Override
    protected ModelComparator getModelComparator() {
        return new MySqlModelComparator(getPlatformInfo(), getTableDefinitionChangesPredicate(), isDelimitedIdentifierModeOn());
    }

    @Override
    protected TableDefinitionChangesPredicate getTableDefinitionChangesPredicate() {
        return new DefaultTableDefinitionChangesPredicate() {
            @Override
            protected boolean isSupported(Table intermediateTable, TableChange change) {
                if (change instanceof AddColumnChange addColumnChange) {

                    return !addColumnChange.getNewColumn().isAutoIncrement() &&
                           (!addColumnChange.getNewColumn().isRequired() || (addColumnChange.getNewColumn().getDefaultValue() != null));
                } else if (change instanceof ColumnDefinitionChange colDefChange) {
                    Column sourceColumn = intermediateTable.findColumn(colDefChange.getChangedColumn(), isDelimitedIdentifierModeOn());

                    return !ColumnDefinitionChange.isTypeChanged(getPlatformInfo(), sourceColumn, colDefChange.getNewColumn()) &&
                           !ColumnDefinitionChange.isSizeChanged(getPlatformInfo(), sourceColumn, colDefChange.getNewColumn());
                } else {
                    return (change instanceof RemoveColumnChange) ||
                           (change instanceof AddPrimaryKeyChange) ||
                           (change instanceof PrimaryKeyChange) ||
                           (change instanceof RemovePrimaryKeyChange);
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

        if (change.getPreviousColumn() != null) {
            prevColumn = changedTable.findColumn(change.getPreviousColumn(), isDelimitedIdentifierModeOn());
        }
        ((MySqlBuilder) getSqlBuilder()).insertColumn(changedTable, change.getNewColumn(), prevColumn);
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

        ((MySqlBuilder) getSqlBuilder()).recreateColumn(changedTable, change.getNewColumn());
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

        ((MySqlBuilder) getSqlBuilder()).dropColumn(changedTable, removedColumn);
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

        ((MySqlBuilder) getSqlBuilder()).dropPrimaryKey(changedTable);
        change.apply(currentModel, isDelimitedIdentifierModeOn());
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

        ((MySqlBuilder) getSqlBuilder()).dropPrimaryKey(changedTable);
        getSqlBuilder().createPrimaryKey(changedTable, newPKColumns);
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }
}
