package org.apache.ddlutils.io;

import org.apache.ddlutils.model.TableRow;

/**
 * Marks classes that can receive table rows read by the {@link org.apache.ddlutils.io.DataReader}.
 */
public interface DataSink {
    /**
     * Notifies the sink that rows will be added.
     */
    void start() throws DataSinkException;

    /**
     * Adds a table row.
     *
     * @param row The table row to add
     */
    void addRow(TableRow row) throws DataSinkException;

    /**
     * Notifies the sink that all rows have been added.
     */
    void end() throws DataSinkException;
}
