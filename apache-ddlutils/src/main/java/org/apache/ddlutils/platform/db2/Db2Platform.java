package org.apache.ddlutils.platform.db2;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.alteration.*;
import org.apache.ddlutils.model.CascadeActionEnum;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.DefaultTableDefinitionChangesPredicate;
import org.apache.ddlutils.platform.GenericDatabasePlatform;
import org.apache.ddlutils.platform.SqlBuildContext;
import org.apache.ddlutils.util.StringUtils;

import java.io.IOException;
import java.sql.Types;

/**
 * The DB2 platform implementation.
 * @version $Revision: 231306 $
 */
public class Db2Platform extends GenericDatabasePlatform {
    /**
     * Database name of this platform.
     */
    public static final String DATABASENAME = "DB2";
    /**
     * The standard DB2 jdbc driver.
     */
    public static final String JDBC_DRIVER = "com.ibm.db2.jcc.DB2Driver";
    /**
     * Older name for the jdbc driver.
     */
    public static final String JDBC_DRIVER_OLD1 = "COM.ibm.db2.jdbc.app.DB2Driver";
    /**
     * Older name for the jdbc driver.
     */
    public static final String JDBC_DRIVER_OLD2 = "COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver";
    /**
     * The JTOpen JDBC Driver.
     */
    public static final String JDBC_DRIVER_JTOPEN = "com.ibm.as400.access.AS400JDBCDriver";
    /**
     * The subprotocol used by the standard DB2 driver.
     */
    public static final String JDBC_SUBPROTOCOL = "db2";
    /**
     * An alternative subprotocol used by the standard DB2 driver on OS/390.
     */
    public static final String JDBC_SUBPROTOCOL_OS390_1 = "db2os390";
    /**
     * An alternative subprotocol used by the standard DB2 driver on OS/390.
     */
    public static final String JDBC_SUBPROTOCOL_OS390_2 = "db2os390sqlj";
    /**
     * An alternative subprotocol used by the JTOpen driver on OS/400.
     */
    public static final String JDBC_SUBPROTOCOL_JTOPEN = "as400";

    /**
     * Creates a new platform instance.
     */
    public Db2Platform() {
        PlatformInfo info = getPlatformInfo();

        info.setMaxIdentifierLength(18);
        info.setIdentityColumnAutomaticallyRequired(true);
        info.setPrimaryKeyColumnsHaveToBeRequired(true);
        info.setMultipleIdentityColumnsSupported(false);
        info.setSupportedOnUpdateActions(new CascadeActionEnum[]{CascadeActionEnum.RESTRICT, CascadeActionEnum.NONE});
        info.setSupportedOnDeleteActions(new CascadeActionEnum[]{CascadeActionEnum.RESTRICT, CascadeActionEnum.CASCADE, CascadeActionEnum.SET_NULL, CascadeActionEnum.NONE});

        // the BINARY types are also handled by Db2Builder.getSqlType(Column)
        info.addNativeTypeMapping(Types.ARRAY, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.BINARY, "CHAR {0} FOR BIT DATA");
        info.addNativeTypeMapping(Types.BIT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.BOOLEAN, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.FLOAT, "DOUBLE", Types.DOUBLE);
        info.addNativeTypeMapping(Types.JAVA_OBJECT, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.LONGVARBINARY, "LONG VARCHAR FOR BIT DATA");
        info.addNativeTypeMapping(Types.LONGVARCHAR, "LONG VARCHAR");
        info.addNativeTypeMapping(Types.NULL, "LONG VARCHAR FOR BIT DATA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.NUMERIC, "DECIMAL", Types.DECIMAL);
        info.addNativeTypeMapping(Types.OTHER, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.STRUCT, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.TINYINT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.VARBINARY, "VARCHAR {0} FOR BIT DATA");

        info.setDefaultSize(Types.CHAR, 254);
        info.setDefaultSize(Types.VARCHAR, 254);
        info.setDefaultSize(Types.BINARY, 254);
        info.setDefaultSize(Types.VARBINARY, 254);

        setSqlBuilder(new Db2Builder(this));
        setModelReader(new Db2ModelReader(this));
    }


    public String getName() {
        return DATABASENAME;
    }


    protected ModelComparator getModelComparator() {
        ModelComparator comparator = super.getModelComparator();

        comparator.setCanDropPrimaryKeyColumns(false);
        comparator.setGeneratePrimaryKeyChanges(false);
        return comparator;
    }


    protected TableDefinitionChangesPredicate getTableDefinitionChangesPredicate() {
        return new DefaultTableDefinitionChangesPredicate() {
            protected boolean isSupported(Table intermediateTable, TableChange change) {
                if ((change instanceof RemoveColumnChange) ||
                    (change instanceof PrimaryKeyChange) ||
                    (change instanceof RemovePrimaryKeyChange)) {
                    return true;
                } else if (change instanceof AddColumnChange addColumnChange) {

                    // DB2 cannot add IDENTITY columns, and required columns need a default value
                    return (addColumnChange.getNextColumn() == null) &&
                        !addColumnChange.getNewColumn().isAutoIncrement() &&
                        (!addColumnChange.getNewColumn().isRequired() || !StringUtils.isEmpty(addColumnChange
                            .getNewColumn().getDefaultValue()));
                } else {
                    return false;
                }
            }
        };
    }

    /**
     * Processes a change representing the addition of a column.
     * @param currentModel The current database schema
     * @param params       The parameters used in the creation of new tables. Note that for existing
     *                     tables, the parameters won't be applied
     * @param change       The change object
     */
    public void processChange(Database currentModel,
                              SqlBuildContext params,
                              RemoveColumnChange change) throws IOException {
        Table changedTable = findChangedTable(currentModel, change);
        Column removedColumn = changedTable.findColumn(change.getChangedColumn(), isDelimitedIdentifierModeOn());

        ((Db2Builder) getSqlBuilder()).dropColumn(changedTable, removedColumn);
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }

    /**
     * Processes the removal of a primary key from a table.
     * @param currentModel The current database schema
     * @param params       The parameters used in the creation of new tables. Note that for existing
     *                     tables, the parameters won't be applied
     * @param change       The change object
     */
    public void processChange(Database currentModel,
                              SqlBuildContext params,
                              RemovePrimaryKeyChange change) throws IOException {
        Table changedTable = findChangedTable(currentModel, change);

        ((Db2Builder) getSqlBuilder()).dropPrimaryKey(changedTable);
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }

    /**
     * Processes the change of the primary key of a table.
     * @param currentModel The current database schema
     * @param params       The parameters used in the creation of new tables. Note that for existing
     *                     tables, the parameters won't be applied
     * @param change       The change object
     */
    public void processChange(Database currentModel,
                              SqlBuildContext params,
                              PrimaryKeyChange change) throws IOException {
        Table changedTable = findChangedTable(currentModel, change);
        String[] newPKColumnNames = change.getNewPrimaryKeyColumns();
        Column[] newPKColumns = new Column[newPKColumnNames.length];

        for (int colIdx = 0; colIdx < newPKColumnNames.length; colIdx++) {
            newPKColumns[colIdx] = changedTable.findColumn(newPKColumnNames[colIdx], isDelimitedIdentifierModeOn());
        }
        ((Db2Builder) getSqlBuilder()).dropPrimaryKey(changedTable);
        getSqlBuilder().createPrimaryKey(changedTable, newPKColumns);
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }
}
