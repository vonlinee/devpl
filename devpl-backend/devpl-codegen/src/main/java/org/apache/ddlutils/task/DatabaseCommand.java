package org.apache.ddlutils.task;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.jdbc.PooledDataSourceWrapper;
import org.apache.tools.ant.BuildException;

/**
 * Base type for commands that have the database info embedded.
 */
public abstract class DatabaseCommand extends Command {
    /**
     * The platform configuration.
     */
    private PlatformConfiguration _platformConf = new PlatformConfiguration();

    /**
     * Returns the database type.
     *
     * @return The database type
     */
    protected String getDatabaseType() {
        return _platformConf.getDatabaseType();
    }

    /**
     * Returns the data source to use for accessing the database.
     *
     * @return The data source
     */
    protected PooledDataSourceWrapper getDataSource() {
        return _platformConf.getDataSource();
    }

    /**
     * Returns the catalog pattern if any.
     *
     * @return The catalog pattern
     */
    public String getCatalogPattern() {
        return _platformConf.getCatalogPattern();
    }

    /**
     * Returns the schema pattern if any.
     *
     * @return The schema pattern
     */
    public String getSchemaPattern() {
        return _platformConf.getSchemaPattern();
    }

    /**
     * Sets the platform configuration.
     *
     * @param platformConf The platform configuration
     */
    protected void setPlatformConfiguration(PlatformConfiguration platformConf) {
        _platformConf = platformConf;
    }

    /**
     * Creates the platform for the configured database.
     *
     * @return The platform
     */
    protected Platform getPlatform() throws BuildException {
        return _platformConf.getPlatform();
    }

    @Override
    public boolean isRequiringModel() {
        return true;
    }
}
