package org.apache.ddlutils.task;

import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.CreationParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Base type for database commands that use creation parameters.
 */
public abstract class DatabaseCommandWithCreationParameters extends DatabaseCommand {
    /**
     * The additional creation parameters.
     */
    private final List<TableSpecificParameter> _parameters = new ArrayList<>();

    /**
     * Adds a parameter which is a name-value pair.
     *
     * @param param The parameter
     */
    public void addConfiguredParameter(TableSpecificParameter param) {
        _parameters.add(param);
    }

    /**
     * Filters the parameters for the given model and platform.
     *
     * @param model           The database model
     * @param platformName    The name of the platform
     * @param isCaseSensitive Whether case is relevant when comparing names of tables
     * @return The filtered parameters
     */
    protected CreationParameters getFilteredParameters(Database model, String platformName, boolean isCaseSensitive) {
        CreationParameters parameters = new CreationParameters();
        for (TableSpecificParameter param : _parameters) {
            if (param.isForPlatform(platformName)) {
                for (int idx = 0; idx < model.getTableCount(); idx++) {
                    Table table = model.getTable(idx);

                    if (param.isForTable(table, isCaseSensitive)) {
                        parameters.addParameter(table, param.getName(), param.getValue());
                    }
                }
            }
        }
        return parameters;
    }
}
