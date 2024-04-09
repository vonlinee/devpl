package org.apache.ddlutils.platform;

import org.apache.ddlutils.model.Database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseModelReader {

    /**
     * get jdbc connection
     *
     * @return 数据库连接
     */
    Connection getConnection();

    /**
     * set database connection to read database model from live connection.
     *
     * @param connection 数据库连接
     */
    void setConnection(Connection connection);

    /**
     * Reads the database model from the given connection.
     *
     * @param connection The connection
     * @param name       The name of the resulting database; <code>null</code> when the default name (the catalog)
     *                   is desired which might be <code>null</code> itself though
     * @param catalog    The catalog to access in the database; use <code>null</code> for the default value
     * @param schema     The schema to access in the database; use <code>null</code> for the default value
     * @param tableTypes The table types to process; use <code>null</code> or an empty list for the default ones
     * @return The database model
     */
    Database getDatabase(Connection connection, String name, String catalog, String schema, String[] tableTypes) throws SQLException;
}
