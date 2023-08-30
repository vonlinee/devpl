package org.apache.ddlutils.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ddlutils.DatabaseOperationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

/**
 * JdbcSupport is an abstract base class for objects which need to
 * perform JDBC operations. It contains a number of useful methods
 * for implementation inheritence..
 * @version $Revision$
 */
public abstract class JdbcSupport {
    /**
     * The Log to which logging calls will be made.
     */
    private final Logger _log = LoggerFactory.getLogger(JdbcSupport.class);
    /**
     * The data source.
     */
    private DataSource dataSource;
    /**
     * The username for accessing the database.
     */
    private String username;
    /**
     * The password for accessing the database.
     */
    private String password;
    /**
     * The names of the currently borrowed connections (for debugging).
     */
    private final HashSet<String> openConnectionNames = new HashSet<>();

    // Properties
    //-------------------------------------------------------------------------

    /**
     * Returns the data source used for communicating with the database.
     * @return The data source
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Sets the DataSource used for communicating with the database.
     * @param dataSource The data source
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Returns the username used to access the database.
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username to be used to access the database.
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password used to access the database.
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password to be used to access the database.
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    // Implementation methods
    //-------------------------------------------------------------------------

    /**
     * Returns a (new) JDBC connection from the data source.
     * @return The connection
     */
    public Connection borrowConnection() throws DatabaseOperationException {
        try {
            Connection connection;
            if (username == null) {
                connection = getDataSource().getConnection();
            } else {
                connection = getDataSource().getConnection(username, password);
            }
            if (_log.isDebugEnabled()) {
                String connName = connection.toString();
                _log.debug("Borrowed connection " + connName + " from data source");
                openConnectionNames.add(connName);
            }
            return connection;
        } catch (SQLException ex) {
            throw new DatabaseOperationException("Could not get a connection from the datasource", ex);
        }
    }

    /**
     * Closes the given JDBC connection (returns it back to the pool if the datasource is poolable).
     * @param connection The connection
     */
    public void returnConnection(Connection connection) {
        try {
            if ((connection != null) && !connection.isClosed()) {
                if (_log.isDebugEnabled()) {
                    String connName = connection.toString();

                    openConnectionNames.remove(connName);

                    StringBuilder logMsg = new StringBuilder();

                    logMsg.append("Returning connection ");
                    logMsg.append(connName);
                    logMsg.append(" to data source.\nRemaining connections:");
                    if (openConnectionNames.isEmpty()) {
                        logMsg.append(" None");
                    } else {
                        for (String openConnectionName : openConnectionNames) {
                            logMsg.append("\n    ");
                            logMsg.append(openConnectionName);
                        }
                    }
                    _log.debug(logMsg.toString());
                }
                connection.close();
            }
        } catch (Exception e) {
            _log.warn("Caught exception while returning connection to pool", e);
        }
    }

    /**
     * Closes the given statement (which also closes all result sets for this statement) and the
     * connection it belongs to.
     * @param statement The statement
     */
    public void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                _log.debug("Ignoring exception that occurred while closing statement", e);
            }
        }
    }
}
