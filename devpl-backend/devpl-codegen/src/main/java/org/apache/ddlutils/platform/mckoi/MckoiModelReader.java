package org.apache.ddlutils.platform.mckoi;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.util.PojoMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reads a database model from a Mckoi database.
 */
public class MckoiModelReader extends JdbcModelReader {
    /**
     * The log.
     */
    protected Logger _log = LoggerFactory.getLogger(MckoiModelReader.class);

    /**
     * Creates a new model reader for Mckoi databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public MckoiModelReader(Platform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
    }

    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, PojoMap values) throws SQLException {
        // Mckoi does not currently return unique indices in the metadata, so we have to query
        // internal tables to get this info

        Table table = super.readTable(metaData, values);

        if (table != null) {
            String query = """
                SELECT uniqueColumns.column, uniqueColumns.seq_no, uniqueInfo.name
                FROM SYS_INFO.sUSRUniqueColumns uniqueColumns, SYS_INFO.sUSRUniqueInfo uniqueInfo
                WHERE uniqueColumns.un_id = uniqueInfo.id AND uniqueInfo.table = ?
                """;
            if (table.getSchema() != null) {
                query = query + " AND uniqueInfo.schema = ?";
            }
            Map<String, Index> indices = new LinkedHashMap<>();
            try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
                stmt.setString(1, table.getName());
                if (table.getSchema() != null) {
                    stmt.setString(2, table.getSchema());
                }

                ResultSet resultSet = stmt.executeQuery();
                PojoMap indexValues = new PojoMap();

                indexValues.put("NON_UNIQUE", Boolean.FALSE);
                while (resultSet.next()) {
                    indexValues.put("COLUMN_NAME", resultSet.getString(1));
                    indexValues.put("ORDINAL_POSITION", resultSet.getShort(2));
                    indexValues.put("INDEX_NAME", resultSet.getString(3));
                    readIndex(metaData, indexValues, indices);
                }
            }
            table.addIndices(indices.values());
        }
        return table;
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, PojoMap values) throws SQLException {
        Column column = super.readColumn(metaData, values);
        if (column.getSize() != null) {
            if (column.getSizeAsInt() <= 0) {
                column.setSize(null);
            }
        }
        String defaultValue = column.getDefaultValue();

        if (defaultValue != null) {
            if (defaultValue.toLowerCase().startsWith("nextval('") || defaultValue.toLowerCase().startsWith("uniquekey('")) {
                column.setDefaultValue(null);
                column.setAutoIncrement(true);
            } else if (TypeMap.isTextType(column.getTypeCode())) {
                column.setDefaultValue(unescape(column.getDefaultValue(), "'", "\\'"));
            }
        }
        return column;
    }
}
