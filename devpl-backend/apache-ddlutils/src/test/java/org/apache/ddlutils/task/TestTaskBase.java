package org.apache.ddlutils.task;

import org.apache.ddlutils.TestAgainstLiveDatabaseBase;
import org.apache.ddlutils.platform.PooledDataSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Base class for ant task tests.
 * @version $Revision: $
 */
public abstract class TestTaskBase extends TestAgainstLiveDatabaseBase {
    /**
     * Returns an instance of the {@link DatabaseToDdlTask}, already configured with
     * a project and the tested database.
     * @return The task object
     */
    protected DatabaseToDdlTask getDatabaseToDdlTaskInstance() {
        DatabaseToDdlTask task = new DatabaseToDdlTask();
        Properties props = getTestProperties();
        String catalog = props.getProperty(DDLUTILS_CATALOG_PROPERTY);
        String schema = props.getProperty(DDLUTILS_SCHEMA_PROPERTY);
        DataSource dataSource = getDataSource();

        if (!dataSource.getClass().getName().equals("org.apache.commons.dbcp.BasicDataSource")) {
            fail("Datasource needs to be of type org.apache.commons.dbcp.BasicDataSource");
        }
        task.setProject(new Project());
        task.addConfiguredDatabase((PooledDataSource) getDataSource());
        task.setCatalogPattern(catalog);
        task.setSchemaPattern(schema);
        task.setUseDelimitedSqlIdentifiers(isUseDelimitedIdentifiers());
        return task;
    }

    /**
     * Returns an instance of the {@link DdlToDatabaseTask}, already configured with
     * a project and the tested database.
     * @return The task object
     */
    protected DdlToDatabaseTask getDdlToDatabaseTaskInstance() {
        DdlToDatabaseTask task = new DdlToDatabaseTask();
        Properties props = getTestProperties();
        String catalog = props.getProperty(DDLUTILS_CATALOG_PROPERTY);
        String schema = props.getProperty(DDLUTILS_SCHEMA_PROPERTY);
        DataSource dataSource = getDataSource();

        if (!dataSource.getClass().getName().equals("org.apache.commons.dbcp.BasicDataSource")) {
            fail("Datasource needs to be of type org.apache.commons.dbcp.BasicDataSource");
        }
        task.setProject(new Project());
        task.addConfiguredDatabase((PooledDataSource) getDataSource());
        task.setCatalogPattern(catalog);
        task.setSchemaPattern(schema);
        task.setUseDelimitedSqlIdentifiers(isUseDelimitedIdentifiers());
        return task;
    }
}
