package org.apache.ddlutils;

import org.apache.ddlutils.platform.axion.AxionPlatform;
import org.apache.ddlutils.platform.cloudscape.CloudscapePlatform;
import org.apache.ddlutils.platform.db2.Db2Platform;
import org.apache.ddlutils.platform.db2.Db2v8Platform;
import org.apache.ddlutils.platform.derby.DerbyPlatform;
import org.apache.ddlutils.platform.firebird.FirebirdPlatform;
import org.apache.ddlutils.platform.hsqldb.HsqlDbPlatform;
import org.apache.ddlutils.platform.interbase.InterbasePlatform;
import org.apache.ddlutils.platform.maxdb.MaxDbPlatform;
import org.apache.ddlutils.platform.mckoi.MckoiPlatform;
import org.apache.ddlutils.platform.mssql.MSSqlPlatform;
import org.apache.ddlutils.platform.mysql.MySql50Platform;
import org.apache.ddlutils.platform.mysql.MySqlPlatform;
import org.apache.ddlutils.platform.oracle.Oracle10Platform;
import org.apache.ddlutils.platform.oracle.Oracle8Platform;
import org.apache.ddlutils.platform.oracle.Oracle9Platform;
import org.apache.ddlutils.platform.postgresql.PostgreSqlPlatform;
import org.apache.ddlutils.platform.sapdb.SapDbPlatform;
import org.apache.ddlutils.platform.sybase.SybaseASE15Platform;
import org.apache.ddlutils.platform.sybase.SybasePlatform;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A factory of {@link DatabasePlatform} instances based on a case- * insensitive database name. Note that this is a convenience class as the platforms
 * can also simply be created via their constructors.
 * @version $Revision: 209952 $
 */
public class PlatformFactory {
    /**
     * The database name -> platform map.
     */
    private static Map<String, Class<? extends DatabasePlatform>> platforms = null;

    /**
     * Returns the platform map.
     * @return The platform list
     */
    private static synchronized Map<String, Class<? extends DatabasePlatform>> getPlatforms() {
        if (platforms == null) {
            // lazy initialization
            platforms = new HashMap<>();
            registerPlatforms();
        }
        return platforms;
    }

    /**
     * Creates a new platform for the given (case-insensitive) database name
     * or returns null if the database is not recognized.
     * @param databaseName The name of the database (case is not important)
     * @return The platform or <code>null</code> if the database is not supported
     */
    public static synchronized DatabasePlatform createNewPlatformInstance(String databaseName) throws DdlUtilsException {
        Class<? extends DatabasePlatform> platformClass = getPlatforms().get(databaseName.toLowerCase());
        try {
            return platformClass != null ? platformClass.newInstance() : null;
        } catch (Exception ex) {
            throw new RuntimeException("Could not create platform for database " + databaseName, ex);
        }
    }

    /**
     * Creates a new platform for the specified database. This is a shortcut method that uses
     * {@link PlatformUtils#determineDatabaseType(String, String)} to determine the parameter
     * for {@link #createNewPlatformInstance(String)}. Note that no database connection is
     * established when using this method.
     * @param jdbcDriver        The jdbc driver
     * @param jdbcConnectionUrl The connection url
     * @return The platform or <code>null</code> if the database is not supported
     */
    public static synchronized DatabasePlatform createNewPlatformInstance(String jdbcDriver, String jdbcConnectionUrl) throws RuntimeException {
        String dbTypeName = PlatformUtils.determineDatabaseType(jdbcDriver, jdbcConnectionUrl);
        return createNewPlatformInstance(Objects.requireNonNull(dbTypeName));
    }

    /**
     * Creates a new platform for the specified database. This is a shortcut method that uses
     * {@link PlatformUtils#determineDatabaseType(DataSource)} to determine the parameter
     * for {@link #createNewPlatformInstance(String)}. Note that this method sets the data source
     * at the returned platform instance (method {@link DatabasePlatform#setDataSource(DataSource)}).
     * @param dataSource The data source for the database
     * @return The platform or <code>null</code> if the database is not supported
     */
    public static synchronized DatabasePlatform createNewPlatformInstance(DataSource dataSource) throws DdlUtilsException {
        DatabasePlatform platform = createNewPlatformInstance(new PlatformUtils().determineDatabaseType(dataSource));
        platform.setDataSource(dataSource);
        return platform;
    }

    /**
     * Creates a new platform for the specified database. This is a shortcut method that uses
     * {@link PlatformUtils#determineDatabaseType(DataSource)} to determine the parameter
     * for {@link #createNewPlatformInstance(String)}. Note that this method sets the data source
     * at the returned platform instance (method {@link DatabasePlatform#setDataSource(DataSource)}).
     * @param dataSource The data source for the database
     * @param username   The username to use for connecting to the database
     * @param password   The password to use for connecting to the database
     * @return The platform or <code>null</code> if the database is not supported
     */
    public static synchronized DatabasePlatform createNewPlatformInstance(DataSource dataSource, String username, String password) throws DdlUtilsException {
        DatabasePlatform platform = createNewPlatformInstance(PlatformUtils.determineDatabaseType(dataSource, username, password));
        platform.setDataSource(dataSource);
        platform.setUsername(username);
        platform.setPassword(password);
        return platform;
    }

    /**
     * Returns a list of all supported platforms.
     * @return The names of the currently registered platforms
     */
    public static synchronized String[] getSupportedPlatforms() {
        return getPlatforms().keySet().toArray(new String[0]);
    }

    /**
     * Determines whether the indicated platform is supported.
     * @param platformName The name of the platform
     * @return <code>true</code> if the platform is supported
     */
    public static boolean isPlatformSupported(String platformName) {
        return getPlatforms().containsKey(platformName.toLowerCase());
    }

    /**
     * Registers a new platform.
     * @param platformName  The platform name
     * @param platformClass The platform class which must implement the {@link DatabasePlatform} interface
     */
    public static synchronized void registerPlatform(String platformName, Class<? extends DatabasePlatform> platformClass) {
        addPlatform(getPlatforms(), platformName, platformClass);
    }

    /**
     * Registers the known platforms.
     */
    private static void registerPlatforms() {
        addPlatform(platforms, AxionPlatform.DATABASENAME, AxionPlatform.class);
        addPlatform(platforms, CloudscapePlatform.DATABASENAME, CloudscapePlatform.class);
        addPlatform(platforms, Db2Platform.DATABASENAME, Db2Platform.class);
        addPlatform(platforms, Db2v8Platform.DATABASENAME, Db2v8Platform.class);
        addPlatform(platforms, DerbyPlatform.DATABASENAME, DerbyPlatform.class);
        addPlatform(platforms, FirebirdPlatform.DATABASENAME, FirebirdPlatform.class);
        addPlatform(platforms, HsqlDbPlatform.DATABASENAME, HsqlDbPlatform.class);
        addPlatform(platforms, InterbasePlatform.DATABASENAME, InterbasePlatform.class);
        addPlatform(platforms, MaxDbPlatform.DATABASENAME, MaxDbPlatform.class);
        addPlatform(platforms, MckoiPlatform.DATABASENAME, MckoiPlatform.class);
        addPlatform(platforms, MSSqlPlatform.DATABASENAME, MSSqlPlatform.class);
        addPlatform(platforms, MySqlPlatform.DATABASENAME, MySqlPlatform.class);
        addPlatform(platforms, MySql50Platform.DATABASENAME, MySql50Platform.class);
        addPlatform(platforms, Oracle8Platform.DATABASENAME, Oracle8Platform.class);
        addPlatform(platforms, Oracle9Platform.DATABASENAME, Oracle9Platform.class);
        addPlatform(platforms, Oracle10Platform.DATABASENAME, Oracle10Platform.class);
        addPlatform(platforms, PostgreSqlPlatform.DATABASENAME, PostgreSqlPlatform.class);
        addPlatform(platforms, SapDbPlatform.DATABASENAME, SapDbPlatform.class);
        addPlatform(platforms, SybasePlatform.DATABASENAME, SybasePlatform.class);
        addPlatform(platforms, SybaseASE15Platform.DATABASENAME, SybaseASE15Platform.class);
    }

    /**
     * Registers a new platform.
     * @param platformMap   The map to add the platform info to
     * @param platformName  The platform name
     * @param platformClass The platform class which must implement the {@link DatabasePlatform} interface
     */
    private static synchronized void addPlatform(Map<String, Class<? extends DatabasePlatform>> platformMap, String platformName, Class<? extends DatabasePlatform> platformClass) {
        if (!DatabasePlatform.class.isAssignableFrom(platformClass)) {
            throw new IllegalArgumentException("Cannot register class " + platformClass.getName() + " because it does not implement the " + DatabasePlatform.class.getName() + " interface");
        }
        platformMap.put(platformName.toLowerCase(), platformClass);
    }
}
