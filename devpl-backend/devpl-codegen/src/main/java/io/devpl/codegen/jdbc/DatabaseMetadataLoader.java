package io.devpl.codegen.jdbc;

import io.devpl.codegen.jdbc.meta.TableMetadata;

import java.util.List;

public interface DatabaseMetadataLoader {

    /**
     * @return table metadata list
     * @see java.sql.DatabaseMetaData#getTables(String, String, String, String[])
     */
    List<TableMetadata> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types);
}
