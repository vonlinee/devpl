package org.apache.ddlutils.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.*;

public class JdbcUtils {

    public static final Logger LOG = LoggerFactory.getLogger(JdbcUtils.class);

    public static void closeQuitely(Connection x) {
        if (x == null) {
            return;
        }

        try {
            if (x.isClosed()) {
                return;
            }

            x.close();
        } catch (SQLRecoverableException e) {
            // skip
        } catch (Exception e) {
            LOG.debug("close connection error", e);
        }
    }

    public static void closeQuitely(Statement x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (Exception e) {
            boolean printError = !(e instanceof SQLRecoverableException) || !"Closed Connection".equals(e.getMessage());
            if (printError) {
                LOG.debug("close statement error", e);
            }
        }
    }

    public static void closeQuitely(ResultSet x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (Exception e) {
            LOG.debug("close result set error", e);
        }
    }

    public static void closeQuitely(Closeable x) {
        if (x == null) {
            return;
        }

        try {
            x.close();
        } catch (Exception e) {
            LOG.debug("close error", e);
        }
    }

    public static void closeQuitely(Blob x) {
        if (x == null) {
            return;
        }

        try {
            x.free();
        } catch (Exception e) {
            LOG.debug("close error", e);
        }
    }

    public static void closeQuitely(Clob x) {
        if (x == null) {
            return;
        }

        try {
            x.free();
        } catch (Exception e) {
            LOG.debug("close error", e);
        }
    }
}
