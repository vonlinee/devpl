package org.apache.ddlutils.jdbc.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @see DatabaseMetaData#getSchemas()
 */
public class SchemaMetadata implements JdbcMetadataObject {
    /**
     * TABLE_SCHEM String => schema name
     */
    private String tableSchema;
    /**
     * TABLE_CATALOG String => catalog name (may be null)
     */
    private String tableCatalog;

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    @Override
    public void initialize(ResultSet resultSet) throws SQLException {
        this.tableSchema = resultSet.getString("TABLE_SCHEM");
        this.tableCatalog = resultSet.getString("TABLE_CATALOG");
    }
}
