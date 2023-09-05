package org.apache.ddlutils.platform;

import javax.sql.DataSource;

/**
 * 数据库连接池的数据源
 */
public interface PooledDataSource extends DataSource {

    String getDriverClassName();

    String getUrl();

    String getUsername();

    String getPassword();
}
