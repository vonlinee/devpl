package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @see org.apache.ddlutils.jdbc.meta.ColumnMetadata
 */
@Getter
@Setter
@TableName(value = "table_info")
public class TableInfo {

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

    /**
     * 该表的列信息
     */
    private List<ColumnInfo> columns;
}
