package org.apache.ddlutils.jdbc;

import org.apache.ddlutils.util.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

/**
 * Simple implementation of the standard JDBC {@link javax.sql.DataSource} interface,
 * configuring the plain old JDBC {@link java.sql.DriverManager} via bean properties, and
 * returning a new {@link java.sql.Connection} from every {@code getConnection} call.
 *
 * <p><b>NOTE: This class is not an actual connection pool; it does not actually
 * pool Connections.</b> It just serves as simple replacement for a full-blown
 * connection pool, implementing the same standard interface, but creating new
 * Connections on every call.
 *
 * <p>Useful for test or standalone environments outside a Jakarta EE container, either
 * as a DataSource bean in a corresponding ApplicationContext or in conjunction with
 * a simple JNDI environment. Pool-assuming {@code Connection.close()} calls will
 * simply close the Connection, so any DataSource-aware persistence code should work.
 * <p>
 * For tests, you can then either set up a mock JNDI environment through complete
 * solutions from third parties such as <a href="https://github.com/h-thurow/Simple-JNDI">Simple-JNDI</a>,
 * or switch the bean definition to a local DataSource (which is simpler and thus recommended).
 *
 * <p>This {@code DriverManagerDataSource} class was originally designed alongside
 * <a href="https://commons.apache.org/proper/commons-dbcp">Apache Commons DBCP</a>
 * and <a href="https://sourceforge.net/projects/c3p0">C3P0</a>, featuring bean-style
 * {@code BasicDataSource}/{@code ComboPooledDataSource} classes with configuration
 * properties for local resource setups. For a modern JDBC connection pool, consider
 * <a href="https://github.com/brettwooldridge/HikariCP">HikariCP</a> instead,
 * exposing a corresponding {@code HikariDataSource} instance to the application.
 */
public class DriverManagerDataSource extends AbstractDriverBasedDataSource {

    /**
     * Constructor for bean-style configuration.
     */
    public DriverManagerDataSource() {
    }

    /**
     * Create a new DriverManagerDataSource with the given JDBC URL,
     * not specifying a username or password for JDBC access.
     *
     * @param url the JDBC URL to use for accessing the DriverManager
     * @see java.sql.DriverManager#getConnection(String)
     */
    public DriverManagerDataSource(String url) {
        setUrl(url);
    }

    /**
     * Create a new DriverManagerDataSource with the given standard
     * DriverManager parameters.
     *
     * @param url      the JDBC URL to use for accessing the DriverManager
     * @param username the JDBC username to use for accessing the DriverManager
     * @param password the JDBC password to use for accessing the DriverManager
     * @see java.sql.DriverManager#getConnection(String, String, String)
     */
    public DriverManagerDataSource(String url, String username, String password) {
        setUrl(url);
        setUsername(username);
        setPassword(password);
    }

    /**
     * Create a new DriverManagerDataSource with the given JDBC URL,
     * not specifying a username or password for JDBC access.
     *
     * @param url      the JDBC URL to use for accessing the DriverManager
     * @param conProps the JDBC connection properties
     * @see java.sql.DriverManager#getConnection(String)
     */
    public DriverManagerDataSource(String url, Properties conProps) {
        setUrl(url);
        setConnectionProperties(conProps);
    }


    /**
     * Set the JDBC driver class name. This driver will get initialized
     * on startup, registering itself with the JDK's DriverManager.
     * <p><b>NOTE: DriverManagerDataSource is primarily intended for accessing
     * <i>pre-registered</i> JDBC drivers.</b> Alternatively, consider
     * initializing the JDBC driver yourself before instantiating this DataSource.
     * The "driverClassName" property is mainly preserved for backwards compatibility,
     * as well as for migrating between Commons DBCP and this DataSource.
     *
     * @see java.sql.DriverManager#registerDriver(java.sql.Driver)
     */
    public void setDriverClassName(String driverClassName) {
        Objects.requireNonNull(driverClassName, "Property 'driverClassName' must not be empty");
        String driverClassNameToUse = driverClassName.trim();
        try {
            Class.forName(driverClassNameToUse, true, Utils.getDefaultClassLoader());
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassNameToUse + "]", ex);
        }
    }

    @Override
    protected Connection getConnectionFromDriver(Properties props) throws SQLException {
        String url = getUrl();
        Objects.requireNonNull(url, "'url' not set");
        return getConnectionFromDriverManager(url, props);
    }

    /**
     * Getting a Connection using the nasty static from DriverManager is extracted
     * into a protected method to allow for easy unit testing.
     *
     * @see java.sql.DriverManager#getConnection(String, java.util.Properties)
     */
    protected Connection getConnectionFromDriverManager(String url, Properties props) throws SQLException {
        return DriverManager.getConnection(url, props);
    }
}
