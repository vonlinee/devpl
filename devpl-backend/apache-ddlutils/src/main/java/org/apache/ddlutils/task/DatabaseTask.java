package org.apache.ddlutils.task;

import org.apache.ddlutils.DdlUtilsTaskException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.jdbc.PooledDataSourceWrapper;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for DdlUtils Ant tasks that operate on a database.
 */
public abstract class DatabaseTask extends Task {
    /**
     * The platform configuration.
     */
    private final PlatformConfiguration _platformConf = new PlatformConfiguration();
    /**
     * The sub-tasks to execute.
     */
    private final List<Command> _commands = new ArrayList<>();
    /**
     * The log.
     */
    protected Logger _log = LoggerFactory.getLogger(getClass());
    /**
     * the environment when task is executing.
     */
    private TaskEnvironment environment;
    /**
     * Whether to use simple logging (that the Ant task configures itself via the {@link #_verbosity} setting.
     */
    private boolean _simpleLogging = true;
    /**
     * The verbosity of the task's debug output.
     */
    private VerbosityLevel _verbosity;

    /**
     * Specifies whether simple logging (configured by the task via the <code>verbosity</code>
     * setting) shall be used, or whether logging is configured outside the task
     * (e.g. via a log4j properties file).
     *
     * @param simpleLogging Whether to use simple logging or not
     *                      Per default, simple logging is enabled.
     */
    public void setSimpleLogging(boolean simpleLogging) {
        _simpleLogging = simpleLogging;
    }

    /**
     * Specifies the verbosity of the task's debug output.
     *
     * @param level The verbosity level
     *              Default is <code>INFO</code>.
     */
    public void setVerbosity(VerbosityLevel level) {
        _verbosity = level;
    }

    /**
     * Returns the database type.
     *
     * @return The database type
     */
    public String getDatabaseType() {
        return _platformConf.getDatabaseType();
    }

    /**
     * Specifies the database type. You should only need to set this if DdlUtils is not able to
     * derive the setting from the name of the used jdbc driver or the jdbc connection url.
     * If you have to specify this, please post your jdbc driver and connection url combo
     * to the user mailing list so that DdlUtils can be enhanced to support this combo.<br/>
     * Valid values are currently:<br/><code>axion, cloudscape, db2, derby, firebird, hsqldb, interbase,
     * maxdb, mckoi, mssql, mysql, mysql5, oracle, oracle9, oracle10, postgresql, sapdb, sybase</code>
     *
     * @param type The database type
     *             Per default, DdlUtils tries to determine the database type via JDBC.
     */
    public void setDatabaseType(String type) {
        if ((type != null) && (!type.isEmpty())) {
            _platformConf.setDatabaseType(type);
        }
    }

    public TaskEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(TaskEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Returns the data source.
     *
     * @return The data source
     */
    public PooledDataSourceWrapper getDataSource() {
        return _platformConf.getDataSource();
    }

    /**
     * Adds the data source to use for accessing the database.
     *
     * @param dataSource The data source
     */
    public void addConfiguredDatabase(PooledDataSourceWrapper dataSource) {
        _platformConf.setDataSource(dataSource);
    }

    /**
     * Specifies a pattern that defines which database catalogs to use. For some
     * more info on catalog patterns and JDBC, see
     * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/sql/DatabaseMetaData.html">java.sql.DatabaseMetaData</a>.
     *
     * @param catalogPattern The catalog pattern
     *                       Per default no specific catalog is used.
     */
    public void setCatalogPattern(String catalogPattern) {
        if (!StringUtils.isEmpty(catalogPattern)) {
            _platformConf.setCatalogPattern(catalogPattern);
        }
    }

    /**
     * Specifies a pattern that defines which database schemas to use. For some
     * more info on schema patterns and JDBC, see
     * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/sql/DatabaseMetaData.html">java.sql.DatabaseMetaData</a>.
     *
     * @param schemaPattern The schema pattern
     *                      Per default no specific schema is used.
     */
    public void setSchemaPattern(String schemaPattern) {
        if (!StringUtils.isEmpty(schemaPattern)) {
            _platformConf.setSchemaPattern(schemaPattern);
        }
    }

    /**
     * Determines whether delimited SQL identifiers shall be used (the default).
     *
     * @return <code>true</code> if delimited SQL identifiers shall be used
     */
    public boolean isUseDelimitedSqlIdentifiers() {
        return _platformConf.isUseDelimitedSqlIdentifiers();
    }

    /**
     * Specifies whether DdlUtils shall use delimited (quoted) identifiers (such as table and column
     * names). Most databases convert unlimited identifiers to uppercase and ignore the case of
     * identifiers when performing any SQL command. Unlimited identifiers also cannot be reserved
     * words and can only contain alphanumerical characters and the underscore.<br/>
     * These limitations do not exist for delimited identifiers where identifiers have to be enclosed
     * in double quotes. Delimited identifiers can contain unicode characters, and even reserved
     * words can be used as identifiers. Please be aware though, that they always have to enclosed
     * in double quotes, and that the case of the identifier will be important in every SQL command
     * executed against the database.
     *
     * @param useDelimitedSqlIdentifiers <code>true</code> if delimited SQL identifiers shall be used
     *                                   Default is <code>false</code>.
     */
    public void setUseDelimitedSqlIdentifiers(boolean useDelimitedSqlIdentifiers) {
        _platformConf.setUseDelimitedSqlIdentifiers(useDelimitedSqlIdentifiers);
    }

    /**
     * Determines whether a table's foreign keys read from a live database
     * shall be sorted alphabetically. Is <code>false</code> by default.
     *
     * @return <code>true</code> if the foreign keys shall be sorted
     */
    public boolean isSortForeignKeys() {
        return _platformConf.isSortForeignKeys();
    }

    /**
     * Specifies whether DdlUtils shall sort the foreign keys of a table read from a live database or
     * leave them in the order in which they are returned by the database/JDBC driver. Note that
     * the sort is case-sensitive only if delimited identifier mode is on
     * (<code>useDelimitedSqlIdentifiers</code> is set to <code>true</code>).
     *
     * @param sortForeignKeys <code>true</code> if the foreign keys shall be sorted
     *                        Default is <code>false</code>.
     */
    public void setSortForeignKeys(boolean sortForeignKeys) {
        _platformConf.setSortForeignKeys(sortForeignKeys);
    }

    /**
     * Determines whether the database shall be shut down after the task has finished.
     *
     * @return <code>true</code> if the database shall be shut down
     */
    public boolean isShutdownDatabase() {
        return _platformConf.isShutdownDatabase();
    }

    /**
     * Specifies whether DdlUtils shall shut down the database after the task has finished.
     * This is mostly useful for embedded databases.
     *
     * @param shutdownDatabase <code>true</code> if the database shall be shut down
     *                         Default is <code>false</code>.
     */
    public void setShutdownDatabase(boolean shutdownDatabase) {
        _platformConf.setShutdownDatabase(shutdownDatabase);
    }

    /**
     * Adds a command.
     *
     * @param command The command
     */
    protected void addCommand(Command command) {
        _commands.add(command);
    }

    /**
     * Determines whether there are commands to perform.
     *
     * @return <code>true</code> if there are commands
     */
    protected boolean hasCommands() {
        return !_commands.isEmpty();
    }

    /**
     * Returns the commands.
     *
     * @return The commands
     */
    protected List<Command> getCommands() {
        return _commands;
    }

    /**
     * Creates the platform configuration.
     *
     * @return The platform configuration
     */
    protected PlatformConfiguration getPlatformConfiguration() {
        return _platformConf;
    }

    /**
     * Creates the platform for the configured database.
     *
     * @return The platform
     */
    protected Platform getPlatform() {
        return _platformConf.getPlatform();
    }

    /**
     * Reads the database model on which the commands will work.
     *
     * @return The database model
     */
    protected abstract Database readModel();

    /**
     * Executes the commands.
     *
     * @param model The database model
     */
    protected void executeCommands(Database model) throws DdlUtilsTaskException {
        for (Command cmd : getCommands()) {
            if (cmd.isRequiringModel() && (model == null)) {
                throw new DdlUtilsTaskException("No database model specified");
            }
            if (cmd instanceof DatabaseCommand) {
                ((DatabaseCommand) cmd).setPlatformConfiguration(_platformConf);
            }
            cmd.execute(this, model);
        }
    }

    @Override
    public void execute() throws DdlUtilsTaskException {
        if (!hasCommands()) {
            _log.info("No sub tasks specified, so there is nothing to do.");
            return;
        }
        ClassLoader sysClassLoader;
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            ClassLoader newClassLoader;
            if (environment == null) {
                newClassLoader = contextClassLoader;
            } else {
                newClassLoader = environment.getClassloader(getClass().getClassLoader(), true);
            }
            // we're changing the thread classloader so that we can access resources
            // from the classpath used to load this task's class
            Thread.currentThread().setContextClassLoader(newClassLoader);
            sysClassLoader = contextClassLoader;
        } catch (SecurityException ex) {
            throw new DdlUtilsTaskException("Could not change the context clas loader", ex);
        }

        try {
            executeCommands(readModel());
        } finally {
            if ((getDataSource() != null) && isShutdownDatabase()) {
                getPlatform().shutdownDatabase();
            }
            // rollback of our classloader change
            Thread.currentThread().setContextClassLoader(sysClassLoader);
        }
    }
}
