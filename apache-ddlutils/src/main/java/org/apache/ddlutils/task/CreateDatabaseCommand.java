package org.apache.ddlutils.task;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.model.Database;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The sub task for creating the target database. Note that this is only supported on some database
 * platforms. See the database support documentation for details on which platforms support this.<br/>
 * This sub task does not require schema files. Therefore the <code>fileset</code> subelement and
 * the <code>schemaFile</code> attribute of the enclosing task can be omitted.
 * @version $Revision: 231306 $
 * @ant.task name="createDatabase"
 */
public class CreateDatabaseCommand extends DatabaseCommand {
    /**
     * The additional creation parameters.
     */
    private final ArrayList<Parameter> _parameters = new ArrayList<>();

    /**
     * Adds a parameter which is a name-value pair.
     * @param param The parameter
     */
    public void addConfiguredParameter(Parameter param) {
        _parameters.add(param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRequiringModel() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(DatabaseTaskBase task, Database model) throws RuntimeException {
        BasicDataSource dataSource = getDataSource();

        if (dataSource == null) {
            throw new RuntimeException("No database specified.");
        }

        DatabasePlatform platform = getPlatform();

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
     * @param platformName The name of the platform
     * @return The filtered parameters
     */
    private Map<String, String> getFilteredParameters(String platformName) {
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        for (Parameter param : _parameters) {
            if (param.isForPlatform(platformName)) {
                parameters.put(param.getName(), param.getValue());
            }
        }
        return parameters;
    }
}
