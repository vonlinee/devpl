package io.devpl.codegen.jdbc;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 表类型
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
    SYNONYM;

    public static TableType[] valueOf(String[] names) {
        if (names == null || names.length == 0) {
            return new TableType[0];
        }
        List<TableType> tableTypes = new ArrayList<>(names.length);
        for (String name : names) {
            for (TableType type : values()) {
                if (type.name().equalsIgnoreCase(name)) {
                    tableTypes.add(type);
                }
            }
        }
        return tableTypes.toArray(TableType[]::new);
    }
}
