package org.apache.ddlutils.task;

import org.apache.ddlutils.DdlUtilsTaskException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.CreationParameters;

/**
 * Parses the schema XML files specified for the enclosing task, and creates the corresponding
 * schema in the database.
 */
public class WriteSchemaToDatabaseCommand extends DatabaseCommandWithCreationParameters {
    /**
     * Whether to alter or re-set the database if it already exists.
     */
    private boolean _alterDb = true;
    /**
     * Whether to drop tables and the associated constraints if necessary.
     */
    private boolean _doDrops = true;

    /**
     * Determines whether to alter the database if it already exists, or re-set it.
     *
     * @return <code>true</code> if to alter the database
     */
    protected boolean isAlterDatabase() {
        return _alterDb;
    }

    /**
     * Specifies whether DdlUtils shall alter an existing database rather than clearing it and
     * creating it new.
     *
     * @param alterTheDb <code>true</code> if to alter the database
     *                   Per default an existing database is altered
     */
    public void setAlterDatabase(boolean alterTheDb) {
        _alterDb = alterTheDb;
    }

    /**
     * Determines whether to drop tables and the associated constraints before re-creating them
     * (this implies <code>alterDatabase</code> is <code>false</code>).
     *
     * @return <code>true</code> if drops shall be performed
     */
    protected boolean isDoDrops() {
        return _doDrops;
    }

    /**
     * Specifies whether tables, external constraints, etc. can be dropped if necessary.
     * Note that this is only relevant when <code>alterDatabase</code> is <code>false</code>.
     *
     * @param doDrops <code>true</code> if drops shall be performed
     *                Per default database structures are dropped if necessary
     */
    public void setDoDrops(boolean doDrops) {
        _doDrops = doDrops;
    }

    @Override
    public void execute(DatabaseTask task, Database model) throws DdlUtilsTaskException {
        if (getDataSource() == null) {
            throw new DdlUtilsTaskException("No database specified.");
        }

        Platform platform = getPlatform();
        boolean isCaseSensitive = platform.isDelimitedIdentifierModeOn();
        CreationParameters params = getFilteredParameters(model, platform.getName(), isCaseSensitive);

        platform.setScriptModeOn(false);
        // we're disabling the comment generation because we're writing directly to the database
        platform.setSqlCommentsOn(false);
        try {
            if (isAlterDatabase()) {
                Database currentModel = platform.readModelFromDatabase(model.getName(), getCatalogPattern(), getSchemaPattern(), null);

                platform.alterModel(currentModel, model, params, true);
            } else {
                platform.createModel(model,
                    params,
                    _doDrops,
                    true);
            }

            _log.info("Written schema to database");
        } catch (Exception ex) {
            handleException(ex, ex.getMessage());
        }
    }
}
