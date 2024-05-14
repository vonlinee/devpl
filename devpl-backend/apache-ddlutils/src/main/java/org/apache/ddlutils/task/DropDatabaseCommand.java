package org.apache.ddlutils.task;

import org.apache.ddlutils.DdlUtilsTaskException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.jdbc.PooledDataSourceWrapper;
import org.apache.ddlutils.model.Database;

/**
 * Sub-task for dropping the target database. Note that this is only supported on some database
 * platforms. See the database support documentation for details on which platforms support this.<br/>
 * This sub-task does not require schema files. Therefore, the <code>fileset</code> sub element and
 * the <code>schemaFile</code> attribute of the enclosing task can be omitted.
 */
public class DropDatabaseCommand extends DatabaseCommand {

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
