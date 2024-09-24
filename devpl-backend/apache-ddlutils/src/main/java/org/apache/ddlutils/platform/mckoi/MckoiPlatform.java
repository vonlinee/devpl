package org.apache.ddlutils.platform.mckoi;


import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.alteration.*;
import org.apache.ddlutils.jdbc.JdbcUtils;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.util.ContextMap;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * The Mckoi database platform implementation.
 */
public class MckoiPlatform extends PlatformImplBase {

    /**
     * Creates a new platform instance.
     */
    public MckoiPlatform() {
        PlatformInfo info = getPlatformInfo();

        info.setIndicesSupported(false);
        info.setIndicesEmbedded(true);
        info.setDefaultValueUsedForIdentitySpec(true);
        info.setAutoCommitModeForLastIdentityValueReading(false);

        info.addNativeTypeMapping(Types.ARRAY, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.BIT, "BOOLEAN", Types.BOOLEAN);
        info.addNativeTypeMapping(Types.DATALINK, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.DISTINCT, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.FLOAT, "DOUBLE", Types.DOUBLE);
        info.addNativeTypeMapping(Types.NULL, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.OTHER, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.REF, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.STRUCT, "BLOB", Types.BLOB);

        info.setDefaultSize(Types.CHAR, 1024);
        info.setDefaultSize(Types.VARCHAR, 1024);
        info.setDefaultSize(Types.BINARY, 1024);
        info.setDefaultSize(Types.VARBINARY, 1024);

        setSqlBuilder(new MckoiBuilder(this));
        setModelReader(new MckoiModelReader(this));
    }

    @Override
    public DBType getDBType() {
        return BuiltinDBType.MCKOI;
    }

    @Override
    public void createDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password, ContextMap parameters) throws DatabaseOperationException, UnsupportedOperationException {
        // For McKoi, you create databases by simply appending "?create=true" to the connection url
        if (BuiltinJDBCDriver.MCKOI.getDriverClassName().equals(jdbcDriverClassName)) {
            StringBuilder creationUrl = new StringBuilder();
            Connection connection = null;

            creationUrl.append(connectionUrl);
            // TODO: It might be safer to parse the URN and check whether there is already a parameter there
            //       (in which case e'd have to use '&' instead)
            creationUrl.append("?create=true");
            if ((parameters != null) && !parameters.isEmpty()) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    // no need to specify create twice (and create=false wouldn't help anyway)
                    if (!"create".equalsIgnoreCase(entry.getKey())) {
                        creationUrl.append("&");
                        creationUrl.append(entry.getKey());
                        creationUrl.append("=");
                        if (entry.getValue() != null) {
                            creationUrl.append(entry.getValue());
                        }
                    }
                }
            }
            if (getLog().isDebugEnabled()) {
                getLog().debug("About to create database using this URL: " + creationUrl);
            }
            try {
                Class.forName(jdbcDriverClassName);
                connection = DriverManager.getConnection(creationUrl.toString(), username, password);
                logWarnings(connection);
            } catch (Exception ex) {
                throw new DatabaseOperationException("Error while trying to create a database", ex);
            } finally {
                JdbcUtils.closeQuietly(connection);
            }
        } else {
            throw new UnsupportedOperationException("Unable to create a McKoi database via the driver " + jdbcDriverClassName);
        }
    }

    @Override
    protected TableDefinitionChangesPredicate getTableDefinitionChangesPredicate() {
        return new DefaultTableDefinitionChangesPredicate() {
            @Override
            public boolean areSupported(Table intermediateTable, List<TableChange> changes) {
                // McKoi has this nice ALTER CREATE TABLE statement which saves us a lot of work
                // Thus, we reject all table level changes and instead redefine the handling of the
                // RecreateTableChange
                return false;
            }
        };
    }

    @Override
    public void processChange(Database currentModel, CreationParameters params, RecreateTableChange change) throws IOException {
        // McKoi has this nice ALTER CREATE TABLE statement which saves us a lot of work
        // We only have to handle auto-increment changes manually
        MckoiBuilder sqlBuilder = (MckoiBuilder) getSqlBuilder();
        Table changedTable = findChangedTable(currentModel, change);

        for (TableChange tableChange : change.getOriginalChanges()) {
            if (tableChange instanceof ColumnDefinitionChange colChange) {
                Column origColumn = changedTable.findColumn(colChange.getChangedColumn(), isDelimitedIdentifierModeOn());
                Column newColumn = colChange.getNewColumn();

                if (!origColumn.isAutoIncrement() && newColumn.isAutoIncrement()) {
                    sqlBuilder.createAutoIncrementSequence(changedTable, origColumn);
                }
            } else if (tableChange instanceof AddColumnChange addColumnChange) {

                if (addColumnChange.getNewColumn().isAutoIncrement()) {
                    sqlBuilder.createAutoIncrementSequence(changedTable, addColumnChange.getNewColumn());
                }
            }
        }

        ContextMap parameters = (params == null ? null : params.getParametersFor(changedTable));

        sqlBuilder.writeRecreateTableStmt(currentModel, change.getTargetTable(), parameters);

        // we have to defer removal of the sequences until they are no longer used
        for (TableChange tableChange : change.getOriginalChanges()) {
            if (tableChange instanceof ColumnDefinitionChange colChange) {
                Column origColumn = changedTable.findColumn(colChange.getChangedColumn(), isDelimitedIdentifierModeOn());
                Column newColumn = colChange.getNewColumn();

                if (origColumn.isAutoIncrement() && !newColumn.isAutoIncrement()) {
                    sqlBuilder.dropAutoIncrementSequence(changedTable, origColumn);
                }
            } else if (tableChange instanceof RemoveColumnChange removeColumnChange) {
                Column removedColumn = changedTable.findColumn(removeColumnChange.getChangedColumn(), isDelimitedIdentifierModeOn());

                if (removedColumn.isAutoIncrement()) {
                    sqlBuilder.dropAutoIncrementSequence(changedTable, removedColumn);
                }
            }
        }
    }
}
