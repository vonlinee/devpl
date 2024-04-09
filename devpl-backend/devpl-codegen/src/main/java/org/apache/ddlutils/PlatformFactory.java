package org.apache.ddlutils;

import org.apache.ddlutils.platform.DBType;
import org.apache.ddlutils.platform.DBTypeEnum;
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
import org.apache.ddlutils.platform.mysql.MySql5xPlatform;
import org.apache.ddlutils.platform.mysql.MySqlPlatform;
import org.apache.ddlutils.platform.oracle.Oracle10Platform;
import org.apache.ddlutils.platform.oracle.Oracle8Platform;
import org.apache.ddlutils.platform.oracle.Oracle9Platform;
import org.apache.ddlutils.platform.postgresql.PostgreSqlPlatform;
import org.apache.ddlutils.platform.sapdb.SapDbPlatform;
import org.apache.ddlutils.platform.sybase.SybaseASE15Platform;
import org.apache.ddlutils.platform.sybase.SybasePlatform;
import org.apache.ddlutils.util.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A factory of {@link org.apache.ddlutils.Platform} instances based on a case- * insensitive database name. Note that this is a convenience class as the platforms
 * can also simply be created via their constructors.
 */
public class PlatformFactory {

    /**
     * Returns the platform map.
     *
     * @return The platform list
     */
    private static synchronized Map<String, Class<? extends Platform>> getPlatforms() {
        return PlatformHolder._platforms;
    }

    /**
     * Creates a new platform for the given (case-insensitive) database name
     * or returns null if the database is not recognized.
     *
     * @param databaseName The name of the database (case is not important)
     * @return The platform or <code>null</code> if the database is not supported
     */
    public static synchronized Platform createNewPlatformInstance(String databaseName) throws DdlUtilsException {
        Class<? extends Platform> platformClass = getPlatforms().get(databaseName.toLowerCase());
        try {
            return Utils.newInstance(platformClass);
        } catch (Exception ex) {
            throw new DdlUtilsException("Could not create platform for database " + databaseName, ex);
        }
    }

    /**
     * Creates a new platform for the specified database. This is a shortcut method that uses
     * {@link PlatformUtils#determineDatabaseType(String, String)} to determine the parameter
     * for {@link #createNewPlatformInstance(String)}. Note that no database connection is
     * established when using this method.
     *
     * @param jdbcDriver        The jdbc driver
     * @param jdbcConnectionUrl The connection url
     * @return The platform or <code>null</code> if the database is not supported
     */
    public static synchronized Platform createNewPlatformInstance(String jdbcDriver, String jdbcConnectionUrl) throws DdlUtilsException {
        return createNewPlatformInstance(new PlatformUtils().determineDatabaseType(jdbcDriver, jdbcConnectionUrl));
    }

    /**
     * Returns a list of all supported platforms.
     *
     * @return The names of the currently registered platforms
     */
    public static synchronized String[] getSupportedPlatforms() {
        return getPlatforms().keySet().toArray(new String[0]);
    }

    /**
     * Determines whether the indicated platform is supported.
     *
     * @param platformName The name of the platform
     * @return <code>true</code> if the platform is supported
     */
    public static boolean isPlatformSupported(String platformName) {
        return getPlatforms().containsKey(platformName.toLowerCase());
    }

    /**
     * Registers a new platform.
     *
     * @param platformName  The platform name
     * @param platformClass The platform class which must implement the {@link Platform} interface
     */
    public static synchronized void registerPlatform(String platformName, Class<? extends Platform> platformClass) {
        addPlatform(getPlatforms(), platformName, platformClass);
    }

    /**
     * Registers a new platform.
     *
     * @param platformMap   The map to add the platform info to
     * @param platformName  The platform name
     * @param platformClass The platform class which must implement the {@link Platform} interface
     */
    private static synchronized void addPlatform(Map<String, Class<? extends Platform>> platformMap, String platformName, Class<? extends Platform> platformClass) {
        if (!Platform.class.isAssignableFrom(platformClass)) {
            throw new IllegalArgumentException("Cannot register class " + platformClass.getName() + " because it does not implement the " + Platform.class.getName() + " interface");
        }
        platformMap.put(platformName.toLowerCase(), platformClass);
    }

    /**
     * Registers a new platform.
     *
     * @param platformMap   The map to add the platform info to
     * @param dbType        The platform database type
     * @param platformClass The platform class which must implement the {@link Platform} interface
     */
    private static synchronized void addPlatform(Map<String, Class<? extends Platform>> platformMap, DBType dbType, Class<? extends Platform> platformClass) {
        if (!Platform.class.isAssignableFrom(platformClass)) {
            throw new IllegalArgumentException("Cannot register class " + platformClass.getName() + " because it does not implement the " + Platform.class.getName() + " interface");
        }
        platformMap.put(dbType.getName().toLowerCase(), platformClass);
    }

    static class PlatformHolder {

        /**
         * The database name -> platform map.
         */
        static Map<String, Class<? extends Platform>> _platforms;

        static {
            _platforms = new ConcurrentHashMap<>();
            registerPlatforms();
        }

        /**
         * Registers the known platforms.
         */
        private static void registerPlatforms() {
            addPlatform(_platforms, DBTypeEnum.AXION.getName(), AxionPlatform.class);
            addPlatform(_platforms, DBTypeEnum.CLOUDSCAPE.getName(), CloudscapePlatform.class);
            addPlatform(_platforms, DBTypeEnum.DB2.getName(), Db2Platform.class);
            addPlatform(_platforms, DBTypeEnum.DB2V8.getName(), Db2v8Platform.class);
            addPlatform(_platforms, DBTypeEnum.DERBY.getName(), DerbyPlatform.class);
            addPlatform(_platforms, DBTypeEnum.FIREBIRD.getName(), FirebirdPlatform.class);
            addPlatform(_platforms, DBTypeEnum.HSQLDB.getName(), HsqlDbPlatform.class);
            addPlatform(_platforms, DBTypeEnum.INTERBASE.getName(), InterbasePlatform.class);
            addPlatform(_platforms, DBTypeEnum.MAXDB.getName(), MaxDbPlatform.class);
            addPlatform(_platforms, DBTypeEnum.MCKOI.getName(), MckoiPlatform.class);
            addPlatform(_platforms, DBTypeEnum.MSSQL.getName(), MSSqlPlatform.class);
            addPlatform(_platforms, DBTypeEnum.MYSQL.getName(), MySqlPlatform.class);
            addPlatform(_platforms, DBTypeEnum.MYSQL5.getName(), MySql5xPlatform.class);
            addPlatform(_platforms, DBTypeEnum.ORACLE8.getName(), Oracle8Platform.class);
            addPlatform(_platforms, DBTypeEnum.ORACLE9.getName(), Oracle9Platform.class);
            addPlatform(_platforms, DBTypeEnum.ORACLE10.getName(), Oracle10Platform.class);
            addPlatform(_platforms, DBTypeEnum.POSTGRE_SQL.getName(), PostgreSqlPlatform.class);
            addPlatform(_platforms, DBTypeEnum.SAPDB.getName(), SapDbPlatform.class);
            addPlatform(_platforms, DBTypeEnum.SYBASE.getName(), SybasePlatform.class);
            addPlatform(_platforms, DBTypeEnum.SYBASE_ASE15.getName(), SybaseASE15Platform.class);
        }
    }
}
