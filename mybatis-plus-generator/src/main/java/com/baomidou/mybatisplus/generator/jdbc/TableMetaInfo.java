package com.baomidou.mybatisplus.generator.jdbc;

import org.hibernate.mapping.ForeignKey;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * JDBC table metadata
 */
public class TableMetaInfo {

    private final String catalog;
    private final String schema;
    private final String name;
    private final Map<String, ColumnMetaInfo> columns = new HashMap<>();
    private final Map<String, ForeignKeyMetadata> foreignKeys = new HashMap<>();
    private final Map<String, IndexMetadata> indexes = new HashMap<>();

    TableMetaInfo(ResultSet rs, DatabaseMetaData meta, boolean extras) throws SQLException {
        catalog = rs.getString("TABLE_CAT");
        schema = rs.getString("TABLE_SCHEM");
        name = rs.getString("TABLE_NAME");
        initColumns(meta);
        if (extras) {
            initForeignKeys(meta);
            initIndexes(meta);
        }
        String cat = catalog == null ? "" : catalog + '.';
        String schem = schema == null ? "" : schema + '.';
    }

    public String getName() {
        return name;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return "TableMetaInfo(" + name + ')';
    }

    public ColumnMetaInfo getColumnMetadata(String columnName) {
        return columns.get(columnName.toLowerCase(Locale.ROOT));
    }

    public ForeignKeyMetadata getForeignKeyMetadata(String keyName) {
        return foreignKeys.get(keyName.toLowerCase(Locale.ROOT));
    }

    public ForeignKeyMetadata getForeignKeyMetadata(ForeignKey fk) {
        for (ForeignKeyMetadata existingFk : foreignKeys.values()) {
            if (existingFk.matches(fk)) {
                return existingFk;
            }
        }
        return null;
    }

    public IndexMetadata getIndexMetadata(String indexName) {
        return indexes.get(indexName.toLowerCase(Locale.ROOT));
    }

    private void addForeignKey(ResultSet rs) throws SQLException {
        String fk = rs.getString("FK_NAME");

        if (fk == null) {
            return;
        }

        ForeignKeyMetadata info = getForeignKeyMetadata(fk);
        if (info == null) {
            info = new ForeignKeyMetadata(rs);
            foreignKeys.put(info.getName().toLowerCase(Locale.ROOT), info);
        }

        info.addReference(rs);
    }

    private void addIndex(ResultSet rs) throws SQLException {
        String index = rs.getString("INDEX_NAME");

        if (index == null) {
            return;
        }

        IndexMetadata info = getIndexMetadata(index);
        if (info == null) {
            info = new IndexMetadata(rs);
            indexes.put(info.getName().toLowerCase(Locale.ROOT), info);
        }

        info.addColumn(getColumnMetadata(rs.getString("COLUMN_NAME")));
    }

    public void addColumn(ResultSet rs) throws SQLException {
        String column = rs.getString("COLUMN_NAME");
        if (column == null) {
            return;
        }
        if (getColumnMetadata(column) == null) {
            ColumnMetaInfo info = new ColumnMetaInfo(rs);
            columns.put(info.getName().toLowerCase(Locale.ROOT), info);
        }
    }

    private void initForeignKeys(DatabaseMetaData meta) throws SQLException {
        try (ResultSet rs = meta.getImportedKeys(catalog, schema, name)) {
            while (rs.next()) {
                addForeignKey(rs);
            }
        }
    }

    private void initIndexes(DatabaseMetaData meta) throws SQLException {
        try (ResultSet rs = meta.getIndexInfo(catalog, schema, name, false, true)) {
            while (rs.next()) {
                if (rs.getShort("TYPE") == DatabaseMetaData.tableIndexStatistic) {
                    continue;
                }
                addIndex(rs);
            }
        }
    }

    private void initColumns(DatabaseMetaData meta) throws SQLException {
        try (ResultSet rs = meta.getColumns(catalog, schema, name, "%")) {
            while (rs.next()) {
                addColumn(rs);
            }
        }
    }
}
