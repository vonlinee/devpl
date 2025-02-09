package org.apache.ddlutils.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Abstract base class for JDBC {@link javax.sql.DataSource} implementations
 * that operate on a JDBC {@link java.sql.Driver}.
 *
 * @see DriverManagerDataSource
 */
public abstract class AbstractDriverBasedDataSource extends AbstractDataSource {

    private String url;

    private String username;

    private String password;

    private String catalog;

    private String schema;

    private Properties connectionProperties;

    /**
     * Return the JDBC URL to use for connecting through the Driver.
     */

    public String getUrl() {
        return this.url;
    }

    /**
     * Set the JDBC URL to use for connecting through the Driver.
     *
     * @see java.sql.Driver#connect(String, java.util.Properties)
     */
    public void setUrl(String url) {
        this.url = (url != null ? url.trim() : null);
    }

    /**
     * Return the JDBC username to use for connecting through the Driver.
     */

    public String getUsername() {
        return this.username;
    }

    /**
     * Set the JDBC username to use for connecting through the Driver.
     *
     * @see java.sql.Driver#connect(String, java.util.Properties)
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Return the JDBC password to use for connecting through the Driver.
     */

    public String getPassword() {
        return this.password;
    }

    /**
     * Set the JDBC password to use for connecting through the Driver.
     *
     * @see java.sql.Driver#connect(String, java.util.Properties)
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return the database catalog to be applied to each Connection, if any.
     *
     * @since 4.3.2
     */

    public String getCatalog() {
        return this.catalog;
    }

    /**
     * Specify a database catalog to be applied to each Connection.
     *
     * @see Connection#setCatalog
     * @since 4.3.2
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     * Return the database schema to be applied to each Connection, if any.
     *
     * @since 4.3.2
     */

    public String getSchema() {
        return this.schema;
    }

    /**
     * Specify a database schema to be applied to each Connection.
     *
     * @see Connection#setSchema
     * @since 4.3.2
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * Return the connection properties to be passed to the Driver, if any.
     */

    public Properties getConnectionProperties() {
        return this.connectionProperties;
    }

    /**
     * Specify arbitrary connection properties as key/value pairs,
     * to be passed to the Driver.
     * <p>Can also contain "user" and "password" properties. However,
     * any "username" and "password" bean properties specified on this
     * DataSource will override the corresponding connection properties.
     *
     * @see java.sql.Driver#connect(String, java.util.Properties)
     */
    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    /**
     * This implementation delegates to {@code getConnectionFromDriver},
     * using the default username and password of this DataSource.
     *
     * @see #getConnectionFromDriver(String, String)
     * @see #setUsername
     * @see #setPassword
     */
    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionFromDriver(getUsername(), getPassword());
    }

    /**
     * This implementation delegates to {@code getConnectionFromDriver},
     * using the given username and password.
     *
     * @see #getConnectionFromDriver(String, String)
     */
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnectionFromDriver(username, password);
    }

    /**
     * Build properties for the Driver, including the given username and password (if any),
     * and obtain a corresponding Connection.
     *
     * @param username the name of the user
     * @param password the password to use
     * @return the obtained Connection
     * @throws SQLException in case of failure
     * @see java.sql.Driver#connect(String, java.util.Properties)
     */
    protected Connection getConnectionFromDriver(String username, String password) throws SQLException {
        Properties mergedProps = new Properties();
        Properties connProps = getConnectionProperties();
        if (connProps != null) {
            mergedProps.putAll(connProps);
        }
        if (username != null) {
            mergedProps.setProperty("user", username);
        }
        if (password != null) {
            mergedProps.setProperty("password", password);
        }

        Connection con = getConnectionFromDriver(mergedProps);
        if (this.catalog != null) {
            con.setCatalog(this.catalog);
        }
        if (this.schema != null) {
            con.setSchema(this.schema);
        }
        return con;
    }

    /**
     * Obtain a Connection using the given properties.
     * <p>Template method to be implemented by subclasses.
     *
     * @param props the merged connection properties
     * @return the obtained Connection
     * @throws SQLException in case of failure
     */
    protected abstract Connection getConnectionFromDriver(Properties props) throws SQLException;

}
