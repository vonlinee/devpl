package org.apache.ddlutils.platform.sybase;


import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.alteration.*;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.*;
import org.apache.ddlutils.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Collection;
import java.util.List;

/**
 * The platform implementation for Sybase.
 */
public class SybasePlatform extends PlatformImplBase {

    /**
     * The maximum size that text and binary columns can have.
     */
    public static final long MAX_TEXT_SIZE = 2147483647;

    /**
     * Creates a new platform instance.
     */
    public SybasePlatform() {
        PlatformInfo info = getPlatformInfo();

        info.setMaxIdentifierLength(28);
        info.setNullAsDefaultValueRequired(true);
        info.setIdentityColumnAutomaticallyRequired(true);
        info.setMultipleIdentityColumnsSupported(false);
        info.setPrimaryKeyColumnsHaveToBeRequired(true);
        info.setCommentPrefix("/*");
        info.setCommentSuffix("*/");

        info.addNativeTypeMapping(Types.ARRAY, "IMAGE");
        // BIGINT is mapped back in the model reader
        info.addNativeTypeMapping(Types.BIGINT, "DECIMAL(19,0)");
        // we're not using the native BIT type because it is rather limited (cannot be NULL, cannot be indexed)
        info.addNativeTypeMapping(Types.BIT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.BLOB, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BOOLEAN, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.CLOB, "TEXT", Types.LONGVARCHAR);
        info.addNativeTypeMapping(Types.DATALINK, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.DATE, "DATETIME", Types.TIMESTAMP);
        info.addNativeTypeMapping(Types.DISTINCT, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.DOUBLE, "DOUBLE PRECISION");
        info.addNativeTypeMapping(Types.FLOAT, "DOUBLE PRECISION", Types.DOUBLE);
        info.addNativeTypeMapping(Types.INTEGER, "INT");
        info.addNativeTypeMapping(Types.JAVA_OBJECT, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.LONGVARBINARY, "IMAGE");
        info.addNativeTypeMapping(Types.LONGVARCHAR, "TEXT");
        info.addNativeTypeMapping(Types.NULL, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.OTHER, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.REF, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.STRUCT, "IMAGE", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.TIME, "DATETIME", Types.TIMESTAMP);
        info.addNativeTypeMapping(Types.TIMESTAMP, "DATETIME", Types.TIMESTAMP);
        info.addNativeTypeMapping(Types.TINYINT, "SMALLINT", Types.SMALLINT);

        info.setDefaultSize(Types.BINARY, 254);
        info.setDefaultSize(Types.VARBINARY, 254);
        info.setDefaultSize(Types.CHAR, 254);
        info.setDefaultSize(Types.VARCHAR, 254);

        setSqlBuilder(new SybaseBuilder(this));
        setModelReader(new SybaseModelReader(this));
    }

    @Override
    public DBType getDBType() {
        return BuiltinDBType.SYBASE;
    }

    /**
     * Sets the text size which is the maximum amount of bytes that Sybase returns in a SELECT statement
     * for binary/text columns (e.g. blob, longvarchar etc.).
     *
     * @param size The size to set
     */
    private void setTextSize(long size) {
        Connection connection = borrowConnection();
        Statement stmt = null;

        try {
            stmt = connection.createStatement();

            stmt.execute("SET textsize " + size);
        } catch (SQLException ex) {
            throw new DatabaseOperationException(ex);
        } finally {
            closeStatement(stmt);
            returnConnection(connection);
        }
    }

    @Override
    protected Object extractColumnValue(ResultSet resultSet, String columnName, int columnIdx, int jdbcType) throws DatabaseOperationException, SQLException {
        boolean useIdx = (columnName == null);
        if ((jdbcType == Types.LONGVARBINARY) || (jdbcType == Types.BLOB)) {
            InputStream stream = useIdx ? resultSet.getBinaryStream(columnIdx) : resultSet.getBinaryStream(columnName);
            if (stream == null) {
                return null;
            } else {
                byte[] buf = new byte[65536];
                byte[] result = new byte[0];
                int len;
                try {
                    do {
                        len = stream.read(buf);
                        if (len > 0) {
                            byte[] newResult = new byte[result.length + len];
                            System.arraycopy(result, 0, newResult, 0, result.length);
                            System.arraycopy(buf, 0, newResult, result.length, len);
                            result = newResult;
                        }
                    }
                    while (len > 0);
                    stream.close();
                    return result;
                } catch (IOException ex) {
                    throw new DatabaseOperationException("Error while extracting the value of column " + columnName + " of type " +
                                                         TypeMap.getJdbcTypeName(jdbcType) + " from a result set", ex);
                }
            }
        } else {
            return super.extractColumnValue(resultSet, columnName, columnIdx, jdbcType);
        }
    }

    @Override
    protected void setStatementParameterValue(PreparedStatement statement, int sqlIndex, int typeCode, Object value) throws SQLException {
        if ((typeCode == Types.BLOB) || (typeCode == Types.LONGVARBINARY)) {
            // jConnect doesn't like the BLOB type, but works without problems with LONGVARBINARY
            // even when using the Blob class
            if (value instanceof byte[] data) {

                statement.setBinaryStream(sqlIndex, new ByteArrayInputStream(data), data.length);
            } else {
                // Sybase doesn't like the BLOB type, but works without problems with LONGVARBINARY
                // even when using the Blob class
                super.setStatementParameterValue(statement, sqlIndex, Types.LONGVARBINARY, value);
            }
        } else if (typeCode == Types.CLOB) {
            // Same for CLOB and LONGVARCHAR
            super.setStatementParameterValue(statement, sqlIndex, Types.LONGVARCHAR, value);
        } else {
            super.setStatementParameterValue(statement, sqlIndex, typeCode, value);
        }
    }

    @Override
    public List<TableRow> fetch(Database model, String sql, Collection<?> parameters, Table[] queryHints, int start, int end) throws DatabaseOperationException {
        setTextSize(MAX_TEXT_SIZE);
        return super.fetch(model, sql, parameters, queryHints, start, end);
    }

    @Override
    public List<TableRow> fetch(Database model, String sql, Table[] queryHints, int start, int end) throws DatabaseOperationException {
        setTextSize(MAX_TEXT_SIZE);
        return super.fetch(model, sql, queryHints, start, end);
    }

    @Override
    public TableRowIterator query(Database model, String sql, Collection<?> parameters, Table[] queryHints) throws DatabaseOperationException {
        setTextSize(MAX_TEXT_SIZE);
        return super.query(model, sql, parameters, queryHints);
    }

    @Override
    public TableRowIterator query(Database model, String sql, Table[] queryHints) throws DatabaseOperationException {
        setTextSize(MAX_TEXT_SIZE);
        return super.query(model, sql, queryHints);
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
            SybaseBuilder builder = (SybaseBuilder) getSqlBuilder();
            String quotationOn = builder.getQuotationOnStatement();
            String identityInsertOn = builder.getEnableIdentityOverrideSql(table);
            Statement stmt = connection.createStatement();

            if (!quotationOn.isEmpty()) {
                stmt.execute(quotationOn);
            }
            stmt.execute(identityInsertOn);
            stmt.close();
        }
    }

    @Override
    protected void afterInsert(Connection connection, Table table) throws SQLException {
        if (useIdentityOverrideFor(table)) {
            SybaseBuilder builder = (SybaseBuilder) getSqlBuilder();
            String quotationOn = builder.getQuotationOnStatement();
            String identityInsertOff = builder.getDisableIdentityOverrideSql(table);
            Statement stmt = connection.createStatement();

            if (!quotationOn.isEmpty()) {
                stmt.execute(quotationOn);
            }
            stmt.execute(identityInsertOff);
            stmt.close();
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
        ModelComparator comparator = super.getModelComparator();

        comparator.setGeneratePrimaryKeyChanges(false);
        comparator.setCanDropPrimaryKeyColumns(false);
        return comparator;
    }

    @Override
    protected TableDefinitionChangesPredicate getTableDefinitionChangesPredicate() {
        return new DefaultTableDefinitionChangesPredicate() {
            @Override
            protected boolean isSupported(Table intermediateTable, TableChange change) {
                if ((change instanceof RemoveColumnChange) ||
                    (change instanceof AddPrimaryKeyChange) ||
                    (change instanceof RemovePrimaryKeyChange)) {
                    return true;
                } else if (change instanceof AddColumnChange addColumnChange) {

                    // Sybase can only add not insert columns, and they cannot be IDENTITY columns
                    // We also have to force recreation of the table if a required column is added
                    // that is neither IDENTITY nor has a default value
                    return (addColumnChange.getNextColumn() == null) &&
                           !addColumnChange.getNewColumn().isAutoIncrement() &&
                           (!addColumnChange.getNewColumn().isRequired() || !StringUtils.isEmpty(addColumnChange.getNewColumn().getDefaultValue()));
                } else if (change instanceof ColumnDefinitionChange columnChange) {
                    Column oldColumn = intermediateTable.findColumn(columnChange.getChangedColumn(), isDelimitedIdentifierModeOn());

                    // Sybase cannot change the IDENTITY state of a column via ALTER TABLE MODIFY
                    return oldColumn.isAutoIncrement() == columnChange.getNewColumn().isAutoIncrement();
                } else {
                    return false;
                }
            }
        };
    }

    @Override
    protected Database processChanges(Database model, Collection<ModelChange> changes, CreationParameters params) throws IOException, DdlUtilsException {
        if (!changes.isEmpty()) {
            ((SybaseBuilder) getSqlBuilder()).turnOnQuotation();
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

        ((SybaseBuilder) getSqlBuilder()).dropColumn(changedTable, removedColumn);
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

        ((SybaseBuilder) getSqlBuilder()).dropPrimaryKey(changedTable);
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }

    /**
     * Processes the change of a column definition.
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
        Column newColumn = change.getNewColumn();
        SybaseBuilder sqlBuilder = (SybaseBuilder) getSqlBuilder();

        // if we only change the default value, then we need to use different SQL
        if (!ColumnDefinitionChange.isTypeChanged(getPlatformInfo(), changedColumn, newColumn) &&
            !ColumnDefinitionChange.isSizeChanged(getPlatformInfo(), changedColumn, newColumn) &&
            !ColumnDefinitionChange.isRequiredStatusChanged(changedColumn, newColumn) &&
            !ColumnDefinitionChange.isAutoIncrementChanged(changedColumn, newColumn)) {
            sqlBuilder.changeColumnDefaultValue(changedTable, changedColumn, newColumn.getDefaultValue());
        } else {
            sqlBuilder.changeColumn(changedTable, changedColumn, newColumn);
        }
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }
}
