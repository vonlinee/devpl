package org.apache.ddlutils.task;

import org.apache.ddlutils.DdlUtilsTaskException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.jdbc.PooledDataSourceWrapper;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.util.ContextMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The sub-task for creating the target database. Note that this is only supported on some database
 * platforms. See the database support documentation for details on which platforms support this.<br/>
 * This sub-task does not require schema files. Therefore, the <code>fileset</code> sub element and
 * the <code>schemaFile</code> attribute of the enclosing task can be omitted.
 */
public class CreateDatabaseCommand extends DatabaseCommand {
    /**
     * The additional creation parameters.
     */
    private final List<Parameter> _parameters = new ArrayList<>();

    /**
     * Adds a parameter which is a name-value pair.
     *
     * @param param The parameter
     */
    public void addConfiguredParameter(Parameter param) {
        _parameters.add(param);
    }

    @Override
    public boolean isRequiringModel() {
        return false;
    }

    @Override
    public void execute(DatabaseTask task, Database model) throws DdlUtilsTaskException {
        PooledDataSourceWrapper dataSource = getDataSource();

        if (dataSource == null) {
            throw new DdlUtilsTaskException("No database specified.");
        }

        Platform platform = getPlatform();
        try {
            platform.createDatabase(dataSource.getDriverClassName(),
                dataSource.getUrl(),
                dataSource.getUsername(),
                dataSource.getPassword(),
                getFilteredParameters(platform.getName()));
            _log.info("Created database");
        } catch (UnsupportedOperationException ex) {
            _log.error("Database platform " + platform.getName() + " does not support database creation " +
                       "via JDBC or there was an error while creating it.", ex);
        } catch (Exception ex) {
            handleException(ex, ex.getMessage());
        }
    }

    /**
     * Filters the parameters for the indicated platform.
     *
     * @param platformName The name of the platform
     * @return The filtered parameters
     */
    private ContextMap getFilteredParameters(String platformName) {
        ContextMap parameters = new ContextMap(new LinkedHashMap<>());
        for (Parameter param : _parameters) {
            if (param.isForPlatform(platformName)) {
                parameters.add(param.getName(), param.getValue());
            }
        }
        return parameters;
    }
}
