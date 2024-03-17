package io.devpl.backend.service.impl;

import io.devpl.backend.domain.param.DataSourceMetadataSyncParam;
import io.devpl.backend.service.DataSourceService;
import io.devpl.backend.service.RdbmsConnectionInfoService;
import io.devpl.codegen.db.DBType;
import io.devpl.codegen.db.query.AbstractQueryDatabaseMetadataLoader;
import io.devpl.codegen.jdbc.JdbcDatabaseMetadataLoader;
import io.devpl.codegen.jdbc.meta.DatabaseMetadataLoader;
import io.devpl.codegen.jdbc.meta.TableMetadata;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Resource
    RdbmsConnectionInfoService connectionInfoService;

    @Override
    public DatabaseMetadataLoader getDatabaseMetadataLoader(Connection connection, DBType dbType) {
        if (dbType == null) {
            return new JdbcDatabaseMetadataLoader(connection);
        }
        return new AbstractQueryDatabaseMetadataLoader(connection, dbType);
    }

    @Override
    public void syncTableMetadata(DataSourceMetadataSyncParam param) {
        try (Connection connection = connectionInfoService.getConnection(param.getDataSourceId())) {
            DatabaseMetadataLoader loader = getDatabaseMetadataLoader(connection, DBType.MYSQL);

            List<TableMetadata> tableMetadataList = loader.getTables(param.getDatabaseName(), null, param.getTableName(), null);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
