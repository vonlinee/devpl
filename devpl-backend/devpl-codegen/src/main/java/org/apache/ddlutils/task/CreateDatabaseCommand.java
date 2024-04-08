package org.apache.ddlutils.task;


import org.apache.ddlutils.Platform;
import org.apache.ddlutils.jdbc.PooledDataSourceWrapper;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.util.PojoMap;
import org.apache.tools.ant.BuildException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The sub-task for creating the target database. Note that this is only supported on some database
 * platforms. See the database support documentation for details on which platforms support this.<br/>
 * This sub-task does not require schema files. Therefore, the <code>fileset</code> sub element and
 * the <code>schemaFile</code> attribute of the enclosing task can be omitted.
 *
 * @version $Revision: 231306 $
 * @ant.task name="createDatabase"
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
    public void execute(DatabaseTaskBase task, Database model) throws BuildException {
        PooledDataSourceWrapper dataSource = getDataSource();

        if (dataSource == null) {
            throw new BuildException("No database specified.");
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
                       "via JDBC or there was an error while creating it.",
                ex);
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
    private PojoMap getFilteredParameters(String platformName) {
        PojoMap parameters = new PojoMap(new LinkedHashMap<>());
        for (Parameter param : _parameters) {
            if (param.isForPlatform(platformName)) {
                parameters.add(param.getName(), param.getValue());
            }
        }
        return parameters;
    }
}
