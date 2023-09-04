package org.apache.ddlutils.task;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.PlatformUtils;
import org.apache.ddlutils.platform.PooledDataSource;

/**
 * Encloses the platform configuration for the Ant tasks.
 * @version $Revision: 329426 $
 * ant.type ignore="true"
 */
public class PlatformConfiguration {
    /**
     * The type of the database.
     */
    private String _databaseType;
    /**
     * The data source to use for accessing the database.
     */
    private PooledDataSource _dataSource;
    /**
     * Whether to use delimited SQL identifiers.
     */
    private boolean _useDelimitedSqlIdentifiers;
    /**
     * Whether read foreign keys shall be sorted.
     */
    private boolean _sortForeignKeys;
    /**
     * Whether to shut down the database after the task has finished.
     */
    private boolean _shutdownDatabase;
    /**
     * The catalog pattern.
     */
    private String _catalogPattern;
    /**
     * The schema pattern.
     */
    private String _schemaPattern;
    /**
     * The platform object.
     */
    private DatabasePlatform _platform;

    /**
     * Returns the database type.
     * @return The database type
     */
    public String getDatabaseType() {
        return _databaseType;
    }

    /**
     * Sets the database type.
     * @param type The database type
     */
    public void setDatabaseType(String type) {
        _databaseType = type;
    }

    /**
     * Returns the data source to use for accessing the database.
     * @return The data source
     */
    public PooledDataSource getDataSource() {
        return _dataSource;
    }

    /**
     * Sets the data source to use for accessing the database.
     * @param dataSource The data source pointing to the database
     */
    public void setDataSource(PooledDataSource dataSource) {
        _dataSource = dataSource;
    }

    /**
     * Returns the catalog pattern if any.
     * @return The catalog pattern
     */
    public String getCatalogPattern() {
        return _catalogPattern;
    }

    /**
     * Sets the catalog pattern.
     * @param catalogPattern The catalog pattern
     */
    public void setCatalogPattern(String catalogPattern) {
        _catalogPattern = catalogPattern;
    }

    /**
     * Returns the schema pattern if any.
     * @return The schema pattern
     */
    public String getSchemaPattern() {
        return _schemaPattern;
    }

    /**
     * Sets the schema pattern.
     * @param schemaPattern The schema pattern
     */
    public void setSchemaPattern(String schemaPattern) {
        _schemaPattern = schemaPattern;
    }

    /**
     * Determines whether delimited SQL identifiers shall be used (the default).
     * @return <code>true</code> if delimited SQL identifiers shall be used
     */
    public boolean isUseDelimitedSqlIdentifiers() {
        return _useDelimitedSqlIdentifiers;
    }

    /**
     * Specifies whether delimited SQL identifiers shall be used.
     * @param useDelimitedSqlIdentifiers <code>true</code> if delimited SQL identifiers shall be used
     */
    public void setUseDelimitedSqlIdentifiers(boolean useDelimitedSqlIdentifiers) {
        _useDelimitedSqlIdentifiers = useDelimitedSqlIdentifiers;
    }

    /**
     * Determines whether a table's foreign keys read from a live database
     * shall be sorted alphabetically. Is <code>false</code> by default.
     * @return <code>true</code> if the foreign keys shall be sorted
     */
    public boolean isSortForeignKeys() {
        return _sortForeignKeys;
    }

    /**
     * Specifies whether a table's foreign keys read from a live database
     * shall be sorted alphabetically.
     * @param sortForeignKeys <code>true</code> if the foreign keys shall be sorted
     */
    public void setSortForeignKeys(boolean sortForeignKeys) {
        _sortForeignKeys = sortForeignKeys;
    }

    /**
     * Determines whether the database shall be shut down after the task has finished.
     * @return <code>true</code> if the database shall be shut down
     */
    public boolean isShutdownDatabase() {
        return _shutdownDatabase;
    }

    /**
     * Specifies whether the database shall be shut down after the task has finished.
     * @param shutdownDatabase <code>true</code> if the database shall be shut down
     */
    public void setShutdownDatabase(boolean shutdownDatabase) {
        _shutdownDatabase = shutdownDatabase;
    }

    /**
     * Creates the platform for the configured database.
     * @return The platform
     */
    public DatabasePlatform getPlatform() throws RuntimeException {
        if (_platform == null) {
            if (_databaseType == null) {
                if (_dataSource == null) {
                    throw new RuntimeException("No database specified.");
                }
                _databaseType = PlatformUtils.determineDatabaseType(_dataSource.getDriverClassName(),
                    _dataSource.getUrl());
                if (_databaseType == null) {
                    _databaseType = new PlatformUtils().determineDatabaseType(_dataSource);
                }
            }
            try {
                _platform = PlatformFactory.createNewPlatformInstance(_databaseType);
            } catch (Exception ex) {
                throw new RuntimeException("Database type " + _databaseType + " is not supported.", ex);
            }
            if (_platform == null) {
                throw new RuntimeException("Database type " + _databaseType + " is not supported.");
            }
            _platform.setDataSource(_dataSource);
            _platform.setDelimitedIdentifierModeOn(isUseDelimitedSqlIdentifiers());
            _platform.setForeignKeysSorted(isSortForeignKeys());
        }

        return _platform;
    }
}
