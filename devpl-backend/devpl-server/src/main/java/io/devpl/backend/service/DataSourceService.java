package io.devpl.backend.service;

import io.devpl.backend.domain.param.DataSourceMetadataSyncParam;
import org.apache.ddlutils.jdbc.meta.DatabaseMetadataReader;
import org.apache.ddlutils.platform.DatabaseType;

import java.sql.Connection;

public interface DataSourceService {

    /**
     * 获取数据库元数据加载器实例
     *
     * @param connection 数据库连接
     * @param dbType     数据库类型
     * @return DatabaseMetadataLoader
     */
    DatabaseMetadataReader getDatabaseMetadataLoader(Connection connection, DatabaseType dbType);

    void syncTableMetadata(DataSourceMetadataSyncParam param);
}
