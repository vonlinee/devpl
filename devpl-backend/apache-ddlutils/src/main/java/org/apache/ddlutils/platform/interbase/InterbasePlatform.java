package org.apache.ddlutils.platform.interbase;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.alteration.*;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.*;
import org.apache.ddlutils.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.List;

/**
 * The platform implementation for the Interbase database.
 */
public class InterbasePlatform extends PlatformImplBase {

    /**
     * Creates a new platform instance.
     */
    public InterbasePlatform() {
        PlatformInfo info = getPlatformInfo();

        info.setMaxIdentifierLength(31);
        info.setCommentPrefix("/*");
        info.setCommentSuffix("*/");
        info.setSystemForeignKeyIndicesAlwaysNonUnique(true);
        info.setPrimaryKeyColumnsHaveToBeRequired(true);

        // BINARY and VARBINARY are also handled by the InterbaseBuilder.getSqlType method
        info.addNativeTypeMapping(Types.ARRAY, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BIGINT, "NUMERIC(18,0)");
        // Theoretically we could use (VAR)CHAR CHARACTER SET OCTETS but the JDBC driver is not
        // able to handle that properly (the byte[]/BinaryStream accessors do not work)
        info.addNativeTypeMapping(Types.BINARY, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BIT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.BLOB, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BOOLEAN, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.CLOB, "BLOB SUB_TYPE TEXT");
        info.addNativeTypeMapping(Types.DATALINK, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.DISTINCT, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.DOUBLE, "DOUBLE PRECISION");
        info.addNativeTypeMapping(Types.FLOAT, "DOUBLE PRECISION", Types.DOUBLE);
        info.addNativeTypeMapping(Types.JAVA_OBJECT, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.LONGVARBINARY, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.LONGVARCHAR, "BLOB SUB_TYPE TEXT", Types.CLOB);
        info.addNativeTypeMapping(Types.NULL, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.OTHER, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.REAL, "FLOAT");
        info.addNativeTypeMapping(Types.REF, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.STRUCT, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.TINYINT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.VARBINARY, "BLOB", Types.LONGVARBINARY);

        info.setDefaultSize(Types.CHAR, 254);
        info.setDefaultSize(Types.VARCHAR, 254);
        info.setHasSize(Types.BINARY, false);
        info.setHasSize(Types.VARBINARY, false);

        setSqlBuilder(new InterbaseBuilder(this));
        setModelReader(new InterbaseModelReader(this));
    }

    @Override
    public DBType getDBType() {
        return BuiltinDBType.INTERBASE;
    }

    @Override
    protected void setStatementParameterValue(PreparedStatement statement, int sqlIndex, int typeCode, Object value) throws SQLException {
        if (value != null) {
            if ((value instanceof byte[] bytes) &&
                ((typeCode == Types.BINARY) || (typeCode == Types.VARBINARY) || (typeCode == Types.BLOB))) {
                ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

                statement.setBinaryStream(sqlIndex, stream, bytes.length);
                return;
            } else if ((value instanceof String) && ((typeCode == Types.CLOB) || (typeCode == Types.LONGVARCHAR))) {
                // Clob is not supported directly
                statement.setString(sqlIndex, (String) value);
                return;
            }
        }
        super.setStatementParameterValue(statement, sqlIndex, typeCode, value);
    }

    @Override
    protected Object extractColumnValue(ResultSet resultSet, String columnName, int columnIdx, int jdbcType) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        boolean useIdx = (columnName == null);
        return switch (jdbcType) {
            case Types.BINARY, Types.VARBINARY, Types.BLOB ->
                IOUtils.readAllBytes(useIdx ? resultSet.getBinaryStream(columnIdx) : resultSet.getBinaryStream(columnName));
            case Types.LONGVARCHAR, Types.CLOB ->
                useIdx ? resultSet.getString(columnIdx) : resultSet.getString(columnName);
            default -> super.extractColumnValue(resultSet, columnName, columnIdx, jdbcType);
        };
    }

    @Override
    protected ModelComparator getModelComparator() {
        ModelComparator comparator = super.getModelComparator();
        comparator.setCanDropPrimaryKeyColumns(false);
        comparator.setGeneratePrimaryKeyChanges(false);
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
                    HashSet<String> addedColumns = new HashSet<>();
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
                if ((change instanceof RemoveColumnChange) ||
                    (change instanceof AddColumnChange)) {
                    return true;
                } else {
                    return super.isSupported(intermediateTable, change);
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
        ((InterbaseBuilder) getSqlBuilder()).insertColumn(currentModel,
            changedTable,
            change.getNewColumn(),
            prevColumn);
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

        ((InterbaseBuilder) getSqlBuilder()).dropColumn(changedTable, droppedColumn);
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }
}
