package io.devpl.codegen.jdbc.meta;

import io.devpl.codegen.jdbc.RuntimeSQLException;

import java.util.List;

public interface DatabaseMetadataLoader extends AutoCloseable {

    /**
     * @return catalogs in rdbms
     * @see java.sql.DatabaseMetaData#getCatalogs()
     */
    List<String> getCatalogs();

    /**
     * @return table metadata list
     * @see java.sql.DatabaseMetaData#getTables(String, String, String, String[])
     */
    List<TableMetadata> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws RuntimeSQLException;

    /**
     * @see java.sql.DatabaseMetaData#getTableTypes()
     */
    List<String> getTableTypes() throws RuntimeSQLException;

    /**
     * @return column metadata list
     * @see java.sql.DatabaseMetaData#getColumns(String, String, String, String)
     */
    List<ColumnMetadata> getColumns(String catalog, String schemaPattern,
                                    String tableNamePattern, String columnNamePattern) throws RuntimeSQLException;

    /**
     * @param catalog           catalog
     * @param schema            schema
     * @param table             table
     * @param columnNamePattern columnNamePattern
     * @see java.sql.DatabaseMetaData#getColumnPrivileges(String, String, String, String)
     */
    List<ColumnPrivilegesMetadata> getColumnPrivileges(String catalog, String schema,
                                                       String table, String columnNamePattern) throws RuntimeSQLException;

    @Override
    default void close() {
    }
}
