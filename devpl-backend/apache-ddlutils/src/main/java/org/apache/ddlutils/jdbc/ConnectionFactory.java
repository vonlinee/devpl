package org.apache.ddlutils.jdbc;

import java.util.Properties;

public interface ConnectionFactory {

    ConnectionFactory getConnection(Properties properties);
}
