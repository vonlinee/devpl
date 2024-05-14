package org.apache.ddlutils.task;

import org.apache.ddlutils.io.XmlWriter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Defines an interface for a callback that retrieves a specific result set from the metadata, and
 * also writes rows to a given xml writer as well as handles errors.
 */
public interface ResultSetXmlOperation {
    /**
     * Returns the result set to work on.
     *
     * @return The result set
     */
    ResultSet getResultSet() throws SQLException;

    /**
     * Writes the row currently maintained by the given result set to the given xml writer.
     *
     * @param xmlWriter The xml writer to write to
     * @param result    The row to write
     */
    void handleRow(XmlWriter xmlWriter, ResultSet result) throws SQLException;

    /**
     * Handles the given exception.
     *
     * @param ex The sql exception
     */
    void handleError(SQLException ex);
}
