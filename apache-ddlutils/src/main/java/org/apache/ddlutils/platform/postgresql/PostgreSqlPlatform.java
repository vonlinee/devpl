package org.apache.ddlutils.platform.postgresql;

import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.alteration.*;
import org.apache.ddlutils.dynabean.DynaBean;
import org.apache.ddlutils.dynabean.SqlDynaProperty;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.DefaultTableDefinitionChangesPredicate;
import org.apache.ddlutils.platform.GenericDatabasePlatform;
import org.apache.ddlutils.platform.SqlBuildContext;

import java.io.IOException;
import java.sql.*;
import java.util.Map;

/**
 * The platform implementation for PostgresSql.
 */
public class PostgreSqlPlatform extends GenericDatabasePlatform {
    /**
     * Database name of this platform.
     */
    public static final String DATABASENAME = "PostgreSql";
    /**
     * The standard PostgreSQL jdbc driver.
     */
    public static final String JDBC_DRIVER = "org.postgresql.Driver";
    /**
     * The subprotocol used by the standard PostgreSQL driver.
     */
    public static final String JDBC_SUBPROTOCOL = "postgresql";

    /**
     * Creates a new platform instance.
     */
    public PostgreSqlPlatform() {
        PlatformInfo info = getPlatformInfo();

        info.setPrimaryKeyColumnAutomaticallyRequired(true);
        // this is the default length though it might be changed when building PostgreSQL
        // in file src/include/postgres_ext.h
        info.setMaxIdentifierLength(31);

        info.addNativeTypeMapping(Types.ARRAY, "BYTEA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BINARY, "BYTEA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BIT, "BOOLEAN");
        info.addNativeTypeMapping(Types.BLOB, "BYTEA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BOOLEAN, "BOOLEAN", Types.BIT);
        info.addNativeTypeMapping(Types.CLOB, "TEXT", Types.LONGVARCHAR);
        info.addNativeTypeMapping(Types.DATALINK, "BYTEA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.DECIMAL, "NUMERIC", Types.NUMERIC);
        info.addNativeTypeMapping(Types.DISTINCT, "BYTEA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.DOUBLE, "DOUBLE PRECISION");
        info.addNativeTypeMapping(Types.FLOAT, "DOUBLE PRECISION", Types.DOUBLE);
        info.addNativeTypeMapping(Types.JAVA_OBJECT, "BYTEA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.LONGVARBINARY, "BYTEA");
        info.addNativeTypeMapping(Types.LONGVARCHAR, "TEXT", Types.LONGVARCHAR);
        info.addNativeTypeMapping(Types.NULL, "BYTEA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.OTHER, "BYTEA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.REF, "BYTEA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.STRUCT, "BYTEA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.TINYINT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.VARBINARY, "BYTEA", Types.LONGVARBINARY);

        info.setDefaultSize(Types.CHAR, 254);
        info.setDefaultSize(Types.VARCHAR, 254);

        // no support for specifying the size for these types (because they are mapped
        // to BYTEA which back-maps to BLOB)
        info.setHasSize(Types.BINARY, false);
        info.setHasSize(Types.VARBINARY, false);

        setSqlBuilder(new PostgreSqlBuilder(this));
        setModelReader(new PostgreSqlModelReader(this));
    }


    @Override
    public String getName() {
        return DATABASENAME;
    }

    /**
     * Creates or drops the database referenced by the given connection url.
     * @param jdbcDriverClassName The jdbc driver class name
     * @param connectionUrl       The url to connect to the database if it were already created
     * @param username            The username for creating the database
     * @param password            The password for creating the database
     * @param parameters          Additional parameters for the operation
     * @param createDb            Whether to create or drop the database
     */
    private void createOrDropDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password, Map<String, String> parameters, boolean createDb) throws DatabaseOperationException, UnsupportedOperationException {
        if (JDBC_DRIVER.equals(jdbcDriverClassName)) {
            int slashPos = connectionUrl.lastIndexOf('/');

            if (slashPos < 0) {
                throw new DatabaseOperationException("Cannot parse the given connection url " + connectionUrl);
            }

            int paramPos = connectionUrl.lastIndexOf('?');
            String baseDb = connectionUrl.substring(0, slashPos + 1) + "template1";
            String dbName = (paramPos > slashPos ? connectionUrl.substring(slashPos + 1, paramPos) : connectionUrl.substring(slashPos + 1));
            Connection connection = null;
            Statement stmt = null;
            StringBuffer sql = new StringBuffer();

            sql.append(createDb ? "CREATE" : "DROP");
            sql.append(" DATABASE ");
            sql.append(dbName);
            if ((parameters != null) && !parameters.isEmpty()) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    sql.append(" ");
                    sql.append(entry.getKey());
                    if (entry.getValue() != null) {
                        sql.append(" ");
                        sql.append(entry.getValue());
                    }
                }
            }
            if (getLog().isDebugEnabled()) {
                getLog().debug("About to create database via " + baseDb + " using this SQL: " + sql);
            }
            try {
                Class.forName(jdbcDriverClassName);

                connection = DriverManager.getConnection(baseDb, username, password);
                stmt = connection.createStatement();
                stmt.execute(sql.toString());
                logWarnings(connection);
            } catch (Exception ex) {
                throw new DatabaseOperationException("Error while trying to " + (createDb ? "create" : "drop") + " a database: " + ex.getLocalizedMessage(), ex);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                    }
                }
            }
        } else {
            throw new UnsupportedOperationException("Unable to " + (createDb ? "create" : "drop") + " a PostgreSQL database via the driver " + jdbcDriverClassName);
        }
    }


    @Override
    public void createDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password, Map<String, String> parameters) throws DatabaseOperationException, UnsupportedOperationException {
        // With PostgreSQL, you create a database by executing "CREATE DATABASE" in an existing database (usually
        // the template1 database because it usually exists)
        createOrDropDatabase(jdbcDriverClassName, connectionUrl, username, password, parameters, true);
    }


    @Override
    public void dropDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password) throws DatabaseOperationException, UnsupportedOperationException {
        // With PostgreSQL, you create a database by executing "DROP DATABASE" in an existing database (usually
        // the template1 database because it usually exists)
        createOrDropDatabase(jdbcDriverClassName, connectionUrl, username, password, null, false);
    }


    @Override
    protected void setObject(PreparedStatement statement, int sqlIndex, DynaBean dynaBean, SqlDynaProperty property) throws SQLException {
        int typeCode = property.getColumn().getJdbcTypeCode();
        Object value = dynaBean.get(property.getName());

        // PostgreSQL doesn't like setNull for BYTEA columns
        if (value == null) {
            switch (typeCode) {
                case Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY, Types.BLOB ->
                    statement.setBytes(sqlIndex, null);
                default -> statement.setNull(sqlIndex, typeCode);
            }
        } else {
            super.setObject(statement, sqlIndex, dynaBean, property);
        }
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
            protected boolean isSupported(Table intermediateTable, TableChange change) {
                if (change instanceof RemoveColumnChange) {
                    return true;
                } else if (change instanceof AddColumnChange addColumnChange) {

                    // We can only handle this if
                    // * the column is not set to NOT NULL (the constraint would be applied immediately
                    //   which will not work if there is already data in the table)
                    // * the column has no default value (it would be applied after the change which
                    //   means that PostgreSQL would behave differently from other databases where the
                    //   default is applied to every column)
                    // * the column is added at the end of the table (PostgreSQL does not support
                    //   insertion of a column)
                    return !addColumnChange.getNewColumn().isRequired() &&
                        (addColumnChange.getNewColumn().getDefaultValue() == null) &&
                        (addColumnChange.getNextColumn() == null);
                } else {
                    // TODO: PK changes ?
                    return false;
                }
            }
        };
    }

    /**
     * Processes the removal of a column from a table.
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

        ((PostgreSqlBuilder) getSqlBuilder()).dropColumn(changedTable, removedColumn);
        change.apply(currentModel, isDelimitedIdentifierModeOn());
    }
}
