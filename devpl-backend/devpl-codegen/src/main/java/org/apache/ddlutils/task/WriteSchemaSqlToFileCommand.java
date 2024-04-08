package org.apache.ddlutils.task;


import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.CreationParameters;
import org.apache.tools.ant.BuildException;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Parses the schema XML files specified in the enclosing task, and writes the SQL statements
 * necessary to create this schema in the database, to a file. Note that this SQL is
 * database specific and hence this subtask requires that for the enclosing task, either a
 * data source is specified (via the <code>database</code> sub element) or the
 * <code>databaseType</code> attribute is used to specify the database type.
 *
 * @ant.task name="writeSchemaSqlToFile"
 */
public class WriteSchemaSqlToFileCommand extends DatabaseCommandWithCreationParameters {
    /**
     * The file to output the DTD to.
     */
    private File _outputFile;
    /**
     * Whether to alter or re-set the database if it already exists.
     */
    private boolean _alterDb = true;
    /**
     * Whether to drop tables and the associated constraints if necessary.
     */
    private boolean _doDrops = true;

    /**
     * Specifies the name of the file to write the SQL commands to.
     *
     * @param outputFile The output file
     * @ant.required
     */
    public void setOutputFile(File outputFile) {
        _outputFile = outputFile;
    }

    /**
     * Determines whether to alter the database if it already exists, or re-set it.
     *
     * @return <code>true</code> if to alter the database
     */
    protected boolean isAlterDatabase() {
        return _alterDb;
    }

    /**
     * Specifies whether DdlUtils shall generate SQL to alter an existing database rather
     * than SQL for clearing it and creating it new.
     *
     * @param alterTheDb <code>true</code> if SQL to alter the database shall be created
     *                   Per default SQL for altering the database is created
     */
    public void setAlterDatabase(boolean alterTheDb) {
        _alterDb = alterTheDb;
    }

    /**
     * Determines whether SQL is generated to drop tables and the associated constraints
     * if necessary.
     *
     * @return <code>true</code> if drops SQL shall be generated if necessary
     */
    protected boolean isDoDrops() {
        return _doDrops;
    }

    /**
     * Specifies whether SQL for dropping tables, external constraints, etc. is created if necessary.
     * Note that this is only relevant when <code>alterDatabase</code> is <code>false</code>.
     *
     * @param doDrops <code>true</code> if drops shall be performed if necessary
     *                Per default, drop SQL statements are created
     */
    public void setDoDrops(boolean doDrops) {
        _doDrops = doDrops;
    }

    @Override
    public void execute(DatabaseTaskBase task, Database model) throws BuildException {
        if (_outputFile == null) {
            throw new BuildException("No output file specified");
        }
        if (_outputFile.exists() && !_outputFile.canWrite()) {
            throw new BuildException("Cannot overwrite output file " + _outputFile.getAbsolutePath());
        }

        Platform platform = getPlatform();
        boolean isCaseSensitive = platform.isDelimitedIdentifierModeOn();
        CreationParameters params = getFilteredParameters(model, platform.getName(), isCaseSensitive);

        try (FileWriter writer = new FileWriter(_outputFile)) {

            platform.setScriptModeOn(true);
            if (platform.getPlatformInfo().isSqlCommentsSupported()) {
                // we're generating SQL comments if possible
                platform.setSqlCommentsOn(true);
            }

            boolean shouldAlter = isAlterDatabase();

            if (shouldAlter) {
                if (getDataSource() == null) {
                    shouldAlter = false;
                    _log.warn("Cannot alter the database because no database connection was specified. SQL for database creation will be generated instead.");
                } else {
                    try (Connection connection = getDataSource().getConnection()) {
                        _log.info("establish a connection successfully");
                    } catch (SQLException ex) {
                        shouldAlter = false;
                        _log.warn("Could not establish a connection to the specified database, so SQL for database creation will be generated instead.", ex);
                    }
                }
            }
            if (shouldAlter) {
                Database currentModel = (getCatalogPattern() != null) || (getSchemaPattern() != null) ? platform.readModelFromDatabase("unnamed", getCatalogPattern(), getSchemaPattern(), null) : platform.readModelFromDatabase("unnamed");

                writer.write(platform.getAlterModelSql(currentModel, model, params));
            } else {
                writer.write(platform.getCreateModelSql(model, params, _doDrops, !isFailOnError()));
            }
            _log.info("Written schema SQL to " + _outputFile.getAbsolutePath());
        } catch (Exception ex) {
            handleException(ex, ex.getMessage());
        }
    }
}
