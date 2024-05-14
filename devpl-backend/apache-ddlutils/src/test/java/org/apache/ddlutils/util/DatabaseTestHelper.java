package org.apache.ddlutils.util;

import junit.framework.AssertionFailedError;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TableRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

public class DatabaseTestHelper {

    /**
     * The log for this class.
     */
    private final Logger _log = LoggerFactory.getLogger(DatabaseTestHelper.class);

    public static String getAbsolutePath(String name) {
        Path path = Paths.get(new File("").getAbsolutePath(), "src/test/resources", name);
        return path.toAbsolutePath().toString();
    }

    /**
     * Asserts that the data in the tables described by the given model is the same in the
     * database accessed by the second platform as is in the database accessed by the first platform.
     * Note that it is not tested whether the second database has more data.<br/>
     * All differences will be printed via logging in DEBUG level.
     *
     * @param model            The database model to check
     * @param origDbPlatform   The first platform
     * @param testedDbPlatform The second platform
     */
    public void assertHasSameData(Database model, Platform origDbPlatform, Platform testedDbPlatform) {
        assertHasSameData(null, model, origDbPlatform, testedDbPlatform);
    }

    /**
     * Asserts that the data in the tables described by the given model is the same in the
     * database accessed by the second platform as is in the database accessed by the first platform.
     * Note that it is not tested whether the second database has more data.<br/>
     * All differences will be printed via logging in DEBUG level.
     *
     * @param failureMsg       The failure message to issue if the data is not the same
     * @param model            The database model to check
     * @param origDbPlatform   The first platform
     * @param testedDbPlatform The second platform
     */
    public void assertHasSameData(String failureMsg, Database model, Platform origDbPlatform, Platform testedDbPlatform) {
        boolean hasError = false;

        for (int idx = 0; idx < model.getTableCount(); idx++) {
            Table table = model.getTable(idx);
            Column[] pkCols = table.getPrimaryKeyColumns();

            for (Iterator<TableRow> it = origDbPlatform.query(model, buildQueryString(origDbPlatform, table, null, null), new Table[]{table}); it.hasNext(); ) {
                TableRow obj = it.next();
                Collection<TableRow> result = testedDbPlatform.fetch(model, buildQueryString(origDbPlatform, table, pkCols, obj), new Table[]{table});

                if (result.isEmpty()) {
                    if (_log.isDebugEnabled()) {
                        hasError = true;
                        _log.debug("Row " + obj.toString() + " is not present in second database");
                    } else {
                        throw new AssertionFailedError(failureMsg);
                    }
                } else if (result.size() > 1) {
                    if (_log.isDebugEnabled()) {
                        hasError = true;

                        StringBuilder debugMsg = new StringBuilder();

                        debugMsg.append("Row ");
                        debugMsg.append(obj.toString());
                        debugMsg.append(" is present more than once in the second database:\n");
                        for (TableRow dynaBean : result) {
                            debugMsg.append("  ");
                            debugMsg.append(dynaBean.toString());
                        }
                        _log.debug(debugMsg.toString());
                    } else {
                        throw new AssertionFailedError(failureMsg);
                    }
                } else {
                    TableRow otherObj = result.iterator().next();

                    if (!obj.equals(otherObj)) {
                        if (_log.isDebugEnabled()) {
                            hasError = true;

                            _log.debug("Row " + obj + " is different in the second database: " + otherObj.toString());
                        } else {
                            throw new AssertionFailedError(failureMsg);
                        }
                    }
                }
            }
        }
        if (hasError) {
            throw new AssertionFailedError(failureMsg);
        }
    }

    /**
     * Helper method for build a SELECT statement.
     *
     * @param targetPlatform The platform for the queried database
     * @param table          The queried table
     * @param whereCols      The optional columns that make up the WHERE clause
     * @param whereValues    The optional column value that make up the WHERE clause
     * @return The query string
     */
    private String buildQueryString(Platform targetPlatform, Table table, Column[] whereCols, TableRow whereValues) {
        StringBuilder result = new StringBuilder();
        result.append("SELECT * FROM ");
        if (targetPlatform.isDelimitedIdentifierModeOn()) {
            result.append(targetPlatform.getPlatformInfo().getDelimiterToken());
        }
        result.append(table.getName());
        if (targetPlatform.isDelimitedIdentifierModeOn()) {
            result.append(targetPlatform.getPlatformInfo().getDelimiterToken());
        }
        if ((whereCols != null) && (whereCols.length > 0)) {
            result.append(" WHERE ");
            for (int idx = 0; idx < whereCols.length; idx++) {
                Object value = (whereValues == null ? null : whereValues.getColumnValue(whereCols[idx].getName()));

                if (idx > 0) {
                    result.append(" AND ");
                }
                if (targetPlatform.isDelimitedIdentifierModeOn()) {
                    result.append(targetPlatform.getPlatformInfo().getDelimiterToken());
                }
                result.append(whereCols[idx].getName());
                if (targetPlatform.isDelimitedIdentifierModeOn()) {
                    result.append(targetPlatform.getPlatformInfo().getDelimiterToken());
                }
                result.append(" = ");
                if (value == null) {
                    result.append("NULL");
                } else {
                    if (!whereCols[idx].isOfNumericType()) {
                        result.append(targetPlatform.getPlatformInfo().getValueQuoteToken());
                    }
                    result.append(value);
                    if (!whereCols[idx].isOfNumericType()) {
                        result.append(targetPlatform.getPlatformInfo().getValueQuoteToken());
                    }
                }
            }
        }
        return result.toString();
    }
}
