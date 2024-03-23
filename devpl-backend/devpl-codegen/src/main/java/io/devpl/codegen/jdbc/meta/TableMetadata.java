package io.devpl.codegen.jdbc.meta;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 仅包含表的元数据信息，且所有字段和DatabaseMetaData#getTables返回值一致
 * <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/">JDBC specification</a>
 *
 * @see java.sql.DatabaseMetaData#getTables(String, String, String, String[])
 */
@Data
public class TableMetadata {

    /**
     * TABLE_CAT String => table catalog (may be null)
     */
    private String tableCatalog;

    /**
     * TABLE_SCHEM String => table schema (maybe null)
     */
    private String tableSchema;

    /**
     * TABLE_NAME String => table name
     */
    private String tableName;

    /**
     * TABLE_TYPE String => table type. Typical types are
     * "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
     */
    private String tableType;

    /**
     * String => explanatory comment on the table (maybe null)
     */
    private String remarks;

    /**
     * String => the types catalog (maybe null)
     */
    private String typeCatalog;

    /**
     * String => the types schema (maybe null)
     */
    private String typeSchema;

    /**
     * TYPE_NAME String => type name (maybe null)
     */
    private String typeName;

    /**
     * SELF_REFERENCING_COL_NAME String => name of the designated "identifier" column of a typed table (maybe null)
     */
    private String selfReferencingColumnName;

    /**
     * REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (maybe null)
     */
    private String refGeneration;

    public void initialize(ResultSet resultSet) throws SQLException {
        this.tableCatalog = resultSet.getString("TABLE_CAT");
        this.tableSchema = resultSet.getString("TABLE_SCHEM");
        this.tableName = resultSet.getString("TABLE_NAME");
        this.tableType = resultSet.getString("TABLE_TYPE");
        this.remarks = resultSet.getString("REMARKS");
        this.typeCatalog = resultSet.getString("TYPE_CAT");
        this.tableSchema = resultSet.getString("TYPE_SCHEM");
        this.typeName = resultSet.getString("TYPE_NAME");
        this.selfReferencingColumnName = resultSet.getString("SELF_REFERENCING_COL_NAME");
        this.refGeneration = resultSet.getString("REF_GENERATION");
    }
}
