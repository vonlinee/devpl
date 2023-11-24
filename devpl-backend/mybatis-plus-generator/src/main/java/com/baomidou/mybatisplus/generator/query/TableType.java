package com.baomidou.mybatisplus.generator.query;

import java.sql.DatabaseMetaData;

/**
 * TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
 *
 * @see DatabaseMetaData#getTableTypes()
 */
public enum TableType {

    TABLE,
    VIEW,
    SYSTEM_TABLE,
    GLOBAL_TEMPORARY,
    LOCAL_TEMPORARY,
    ALIAS,
    SYNONYM
}
