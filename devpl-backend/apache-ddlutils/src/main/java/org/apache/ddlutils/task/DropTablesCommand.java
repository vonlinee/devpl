package org.apache.ddlutils.task;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.model.CloneHelper;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ModelHelper;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.PooledDataSource;

/**
 * Sub task for dropping tables.
 */
public class DropTablesCommand extends DatabaseCommand {
    /**
     * The names of the tables to be dropped.
     */
    private String[] _tableNames;
    /**
     * The regular expression matching the names of the tables to be dropped.
     */
    private String _tableNameRegExp;

    /**
     * Sets the names of the tables to be removed, as a comma-separated list. Escape a
     * comma via '\,' if it is part of the table name. Please note that table names are
     * not trimmed which means that whitespace characters should only be present in
     * this string if they are actually part of the table name (i.e. in delimited
     * identifier mode).
     * @param tableNameList The comma-separated list of table names
     *                      If no table filter is specified, then all tables will be dropped.
     */
    public void setTables(String tableNameList) {
        _tableNames = new TaskHelper().parseCommaSeparatedStringList(tableNameList);
    }

    /**
     * Sets the regular expression matching the names of the tables to be removed.
     * For case-insensitive matching, an uppercase name can be assumed. If no
     * regular expressions specified
     * @param tableNameRegExp The regular expression; see {@link java.util.regex.Pattern}
     *                        for details
     *                        If no table filter is specified, then all tables will be dropped.
     */
    public void setTableFilter(String tableNameRegExp) {
        _tableNameRegExp = tableNameRegExp;
    }


    @Override
    public void execute(DatabaseTaskBase task, Database model) throws RuntimeException {
        PooledDataSource dataSource = getDataSource();

        if (dataSource == null) {
            throw new RuntimeException("No database specified.");
        }

        DatabasePlatform platform = getPlatform();
        Database targetModel = new Database();

        if ((_tableNames != null) || (_tableNameRegExp != null)) {
            targetModel = new CloneHelper().clone(model);
            targetModel.initialize();

            Table[] tables = _tableNames != null ? targetModel.findTables(_tableNames, task.isUseDelimitedSqlIdentifiers())
                : targetModel.findTables(_tableNameRegExp, task.isUseDelimitedSqlIdentifiers());

            new ModelHelper().removeForeignKeysToAndFromTables(targetModel, tables);
            targetModel.removeTables(tables);
        }
        try {
            platform.alterModel(model, targetModel, isFailOnError());

            _log.info("Dropped tables");
        } catch (Exception ex) {
            handleException(ex, ex.getMessage());
        }
    }
}
