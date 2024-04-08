package org.apache.ddlutils.task;


import org.apache.ddlutils.io.DataConverterRegistration;
import org.apache.ddlutils.io.DatabaseDataIO;

/**
 * Base type for database commands that use converters.
 *
 * @ant.type ignore="true"
 */
public abstract class ConvertingDatabaseCommand extends DatabaseCommand {
    /**
     * The database data io object.
     */
    private final DatabaseDataIO _dataIO = new DatabaseDataIO();

    /**
     * Returns the database data io object.
     *
     * @return The data io object
     */
    protected DatabaseDataIO getDataIO() {
        return _dataIO;
    }

    /**
     * Registers a converter.
     *
     * @param converterRegistration The registration info
     */
    public void addConfiguredConverter(DataConverterRegistration converterRegistration) {
        _dataIO.registerConverter(converterRegistration);
    }
}
