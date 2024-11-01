package org.apache.ddlutils.platform.oracle;

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
 * The platform for Oracle 8.
 * <p>
 */
public class Oracle8Platform extends PlatformImplBase {

    /**
     * Creates a new platform instance.
     */
    public Oracle8Platform() {
        PlatformInfo info = getPlatformInfo();

        info.setMaxIdentifierLength(30);
        info.setIdentityStatusReadingSupported(false);
        info.setPrimaryKeyColumnAutomaticallyRequired(true);
        info.setSupportedOnUpdateActions(new CascadeActionEnum[]{CascadeActionEnum.NONE});
        info.setSupportedOnDeleteActions(new CascadeActionEnum[]{CascadeActionEnum.CASCADE, CascadeActionEnum.SET_NULL, CascadeActionEnum.NONE});
        info.addEquivalentOnDeleteActions(CascadeActionEnum.NONE, CascadeActionEnum.RESTRICT);

        // Note that the back-mappings are partially done by the model reader, not the driver
        info.addNativeTypeMapping(Types.ARRAY, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.BIGINT, "NUMBER(38)");
        info.addNativeTypeMapping(Types.BINARY, "RAW", Types.VARBINARY);
        info.addNativeTypeMapping(Types.BIT, "NUMBER(1)");
        info.addNativeTypeMapping(Types.BOOLEAN, "NUMBER(1)", Types.BIT);
        info.addNativeTypeMapping(Types.DATALINK, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.DATE, "DATE", Types.TIMESTAMP);
        info.addNativeTypeMapping(Types.DECIMAL, "NUMBER");
        info.addNativeTypeMapping(Types.DISTINCT, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.DOUBLE, "DOUBLE PRECISION");
        info.addNativeTypeMapping(Types.FLOAT, "FLOAT", Types.DOUBLE);
        info.addNativeTypeMapping(Types.JAVA_OBJECT, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.LONGVARBINARY, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.LONGVARCHAR, "CLOB", Types.CLOB);
        info.addNativeTypeMapping(Types.NULL, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.NUMERIC, "NUMBER", Types.DECIMAL);
        info.addNativeTypeMapping(Types.OTHER, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.REF, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.SMALLINT, "NUMBER(5)");
        info.addNativeTypeMapping(Types.STRUCT, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.TIME, "DATE", Types.TIMESTAMP);
        info.addNativeTypeMapping(Types.TIMESTAMP, "DATE");
        info.addNativeTypeMapping(Types.TINYINT, "NUMBER(3)");
        info.addNativeTypeMapping(Types.VARBINARY, "RAW");
        info.addNativeTypeMapping(Types.VARCHAR, "VARCHAR2");

        info.setDefaultSize(Types.CHAR, 254);
        info.setDefaultSize(Types.VARCHAR, 254);
        info.setDefaultSize(Types.BINARY, 254);
        info.setDefaultSize(Types.VARBINARY, 254);

        setSqlBuilder(new Oracle8Builder(this));
        setModelReader(new Oracle8ModelReader(this));
    }

    @Override
    public DatabaseType getDBType() {
        return BuiltinDatabaseType.ORACLE8;
    }

    @Override
    protected TableDefinitionChangesPredicate getTableDefinitionChangesPredicate() {
        // While Oracle has an ALTER TABLE MODIFY statement, it is somewhat limited
        // esp. if there is data in the table, so we don't use it
        return new DefaultTableDefinitionChangesPredicate() {
            @Override
            protected boolean isSupported(Table intermediateTable, TableChange change) {
                if ((change instanceof AddPrimaryKeyChange) ||
                    (change instanceof RemovePrimaryKeyChange)) {
                    return true;
                } else if (change instanceof RemoveColumnChange removeColumnChange) {
                    // TODO: for now we trigger recreating the table, but ideally we should simply add the necessary pk changes
                    Column column = intermediateTable.findColumn(removeColumnChange.getChangedColumn(), isDelimitedIdentifierModeOn());

                    return !column.isPrimaryKey();
                } else if (change instanceof AddColumnChange addColumnChange) {

                    // Oracle can only add not insert columns
                    // Also, we cannot add NOT NULL columns unless they have a default value
                    return addColumnChange.isAtEnd() &&
                           (!addColumnChange.getNewColumn().isRequired() || (addColumnChange.getNewColumn().getDefaultValue() != null));
                } else {
                    return false;
                }
            }
        };
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

        ((Oracle8Builder) getSqlBuilder()).dropColumn(changedTable, removedColumn);
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

        ((Oracle8Builder) getSqlBuilder()).dropPrimaryKey(changedTable);
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

        ((Oracle8Builder) getSqlBuilder()).dropPrimaryKey(changedTable);
        getSqlBuilder().createPrimaryKey(changedTable, newPKColumns);
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }
}
