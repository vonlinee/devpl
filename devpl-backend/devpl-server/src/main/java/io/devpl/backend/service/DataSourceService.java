package io.devpl.backend.service;

import io.devpl.backend.domain.param.DataSourceMetadataSyncParam;
import io.devpl.codegen.db.DBType;
import io.devpl.codegen.jdbc.meta.DatabaseMetadataLoader;

import java.sql.Connection;

public interface DataSourceService {

    DatabaseMetadataLoader getDatabaseMetadataLoader(Connection connection, DBType dbType);

    void syncTableMetadata(DataSourceMetadataSyncParam param);
}
