package org.apache.ddlutils.task;

import org.apache.ddlutils.TestAgainstLiveDatabaseBase;
import org.apache.ddlutils.jdbc.PooledDataSourceWrapper;
import org.apache.tools.ant.Project;

import java.util.Properties;

/**
 * Base class for ant task tests.
 */
public abstract class TestTaskBase extends TestAgainstLiveDatabaseBase {
    /**
     * Returns an instance of the {@link DatabaseToDdlTask}, already configured with
     * a project and the tested database.
     *
     * @return The task object
     */
    protected DatabaseToDdlTask getDatabaseToDdlTaskInstance() {
        DatabaseToDdlTask task = new DatabaseToDdlTask();
        Properties props = getTestProperties();
        String catalog = props.getProperty(DDLUTILS_CATALOG_PROPERTY);
        String schema = props.getProperty(DDLUTILS_SCHEMA_PROPERTY);

        task.setProject(new Project());
        task.addConfiguredDatabase(getDataSource());
        task.setCatalogPattern(catalog);
        task.setSchemaPattern(schema);
        task.setUseDelimitedSqlIdentifiers(isUseDelimitedIdentifiers());
        return task;
    }

    /**
     * Returns an instance of the {@link DdlToDatabaseTask}, already configured with
     * a project and the tested database.
     *
     * @return The task object
     */
    protected DdlToDatabaseTask getDdlToDatabaseTaskInstance() {
        DdlToDatabaseTask task = new DdlToDatabaseTask();
        Properties props = getTestProperties();
        String catalog = props.getProperty(DDLUTILS_CATALOG_PROPERTY);
        String schema = props.getProperty(DDLUTILS_SCHEMA_PROPERTY);
        PooledDataSourceWrapper dataSource = getDataSource();
        task.setProject(new Project());
        task.addConfiguredDatabase(dataSource);
        task.setCatalogPattern(catalog);
        task.setSchemaPattern(schema);
        task.setUseDelimitedSqlIdentifiers(isUseDelimitedIdentifiers());
        return task;
    }
}
