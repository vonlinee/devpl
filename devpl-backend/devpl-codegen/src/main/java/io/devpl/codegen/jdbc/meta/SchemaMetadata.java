package io.devpl.codegen.jdbc.meta;

import lombok.Data;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @see DatabaseMetaData#getSchemas()
 */
@Data
public class SchemaMetadata {
    /**
     * TABLE_SCHEM String => schema name
     */
    private String tableSchem;
    /**
     * TABLE_CATALOG String => catalog name (may be null)
     */
    private String tableCatalog;

    public void initialize(ResultSet resultSet) throws SQLException {
        this.tableSchem = resultSet.getString("TABLE_SCHEM");
        this.tableCatalog = resultSet.getString("TABLE_CATALOG");
    }
}
