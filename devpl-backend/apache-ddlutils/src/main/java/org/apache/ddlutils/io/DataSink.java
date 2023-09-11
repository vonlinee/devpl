package org.apache.ddlutils.io;

import org.apache.ddlutils.dynabean.DynaBean;

/**
 * Marks classes that can receive dyna beans read by the {@link org.apache.ddlutils.io.DataReader}.
 * @version $Revision: 289996 $
 */
public interface DataSink {
    /**
     * Notifies the sink that beans will be added.
     */
    void start() throws DataSinkException;

    /**
     * Adds a dyna bean.
     * @param bean The dyna bean to add
     */
    void addBean(DynaBean bean) throws DataSinkException;

    /**
     * Notifies the sink that all beans have been added.
     */
    void end() throws DataSinkException;
}