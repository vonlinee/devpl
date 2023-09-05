package org.apache.ddlutils.task;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.PooledDataSource;

/**
 * Sub-task for dropping the target database. Note that this is only supported on some database
 * platforms. See the database support documentation for details on which platforms support this.<br/>
 * This sub-task does not require schema files. Therefore, the <code>fileset</code> subelement and
 * the <code>schemaFile</code> attribute of the enclosing task can be omitted.
 */
public class DropDatabaseCommand extends DatabaseCommand {

    @Override
    public boolean isRequiringModel() {
        return false;
    }

    @Override
    public void execute(DatabaseTaskBase task, Database model) throws RuntimeException {
        PooledDataSource dataSource = getDataSource();

        if (dataSource == null) {
            throw new RuntimeException("No database specified.");
        }

        DatabasePlatform platform = getPlatform();

        try {
            platform.dropDatabase(dataSource.getDriverClassName(),
                dataSource.getUrl(),
                dataSource.getUsername(),
                dataSource.getPassword());

            _log.info("Dropped database");
        } catch (UnsupportedOperationException ex) {
            _log.error("Database platform " + platform.getName() + " does not support database dropping via JDBC",
                ex);
        } catch (Exception ex) {
            handleException(ex, ex.getMessage());
        }
    }
}
