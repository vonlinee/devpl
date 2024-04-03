package io.devpl.backend.service;

import io.devpl.backend.domain.param.DataSourceMetadataSyncParam;
import io.devpl.codegen.db.DBType;
import io.devpl.codegen.jdbc.meta.DatabaseMetadataReader;

import java.sql.Connection;

public interface DataSourceService {

    /**
     * 获取数据库元数据加载器实例
     *
     * @param connection 数据库连接
     * @param dbType     数据库类型
     * @return DatabaseMetadataLoader
     */
    DatabaseMetadataReader getDatabaseMetadataLoader(Connection connection, DBType dbType);

    void syncTableMetadata(DataSourceMetadataSyncParam param);
}
