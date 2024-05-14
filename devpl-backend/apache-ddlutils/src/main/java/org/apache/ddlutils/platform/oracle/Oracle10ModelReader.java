package org.apache.ddlutils.platform.oracle;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Reads a database model from an Oracle 10 database.
 */
public class Oracle10ModelReader extends Oracle8ModelReader {
    /**
     * Creates a new model reader for Oracle 10 databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public Oracle10ModelReader(Platform platform) {
        super(platform);
    }

    /**
     * 作为Oracle10g的新特性之一，Recycle Bin存储着被删除对象（这些对象都是BIN$开头）。
     * 该对象可以是表及表相关的所有对象（如索引、约束、嵌套表等），这些被删除对象仍然占据着原先对象的物理空间。
     * 在以下来两种情况下，Oracle会回收该删除对象占用的空间：
     * （用户）从回收站中显式的purge该对象（specifically purged from the recycle bin）；
     * 当表空间不足时，Oracle根据DROPSCN自动的逐一删除这些对象（由于表空间的限制，数据库必须清除它们）
     */
    @Override
    protected Collection<Table> readTables(String catalog, String schemaPattern, String[] tableTypes) throws SQLException {
        Collection<Table> tables = super.readTables(catalog, schemaPattern, tableTypes);
        if (tables == null || tables.isEmpty()) {
            return Collections.emptyList();
        }
        /**
         * Oracle 10 added the recycle bin which contains dropped database objects not yet purged
         * Since we don't want entries from the recycle bin, we filter them out
         * 注意：具体的列名和数量可能因Oracle的版本而异：
         * 1. OBJECT_NAME：已删除对象的名称。
         * 2. ORIGINAL_NAME：对象的原始名称（如果重命名后删除）。
         * 3. OPERATION：导致对象进入回收站的操作（例如，DROP 或 TRUNCATE）。
         * 4. TYPE：对象的类型（例如，TABLE、INDEX 等）。
         * 5. TS_NAME：包含对象的表空间名称。
         * 6. CREATETIME：对象的创建时间。
         * 7. DROPTIME：对象被删除的时间。
         * 8. PARTITION_NAME：如果是分区表或分区索引，这是分区的名称。
         * 9. CAN_UNDROP：一个标志，表示是否可以“撤消删除”该对象。
         * 10.CAN_PURGE：一个标志，表示是否可以清除回收站中的该对象。
         * 11.PURGE_OBJECT：一个SQL语句，用于清除回收站中的该对象。
         * 12.SPACE：对象占用的空间大小。
         */
        StringJoiner inSql = new StringJoiner(",", "(", ")");
        for (Table table : tables) {
            inSql.add(table.getName());
        }
        final String query = "SELECT * FROM RECYCLEBIN WHERE OBJECT_NAME IN ?";
        Set<String> deletedTableNames = new HashSet<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, inSql.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // we found the table in the recycle bin, so it's a deleted one which we ignore
                    deletedTableNames.add(rs.getString("OBJECT_NAME"));
                }
            }
        }
        tables.removeIf(t -> deletedTableNames.contains(t.getName()));
        return tables;
    }
}
