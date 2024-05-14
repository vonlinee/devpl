package org.apache.ddlutils.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;

public abstract class AbstractDataSource implements DataSource {

    /**
     * Returns 0, indicating the default system timeout is to be used.
     */
    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    /**
     * Setting a login timeout is not supported.
     */
    @Override
    public void setLoginTimeout(int timeout) throws SQLException {
        throw new UnsupportedOperationException("setLoginTimeout");
    }

    /**
     * LogWriter methods are not supported.
     */
    @Override
    public PrintWriter getLogWriter() {
        throw new UnsupportedOperationException("getLogWriter");
    }

    /**
     * LogWriter methods are not supported.
     */
    @Override
    public void setLogWriter(PrintWriter pw) throws SQLException {
        throw new UnsupportedOperationException("setLogWriter");
    }

    @Override
    public Logger getParentLogger() {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> face) throws SQLException {
        if (face.isInstance(this)) {
            return (T) this;
        }
        throw new SQLException("DataSource of type [" + getClass().getName() +
                               "] cannot be unwrapped as [" + face.getName() + "]");
    }

    @Override
    public boolean isWrapperFor(Class<?> face) throws SQLException {
        return face.isInstance(this);
    }
}
