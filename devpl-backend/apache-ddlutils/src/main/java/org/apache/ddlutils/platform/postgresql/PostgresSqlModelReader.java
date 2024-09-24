package org.apache.ddlutils.platform.postgresql;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.util.ContextMap;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads a database model from a PostgresSql database.
 */
public class PostgresSqlModelReader extends JdbcModelReader {
    /**
     * Creates a new model reader for PostgresSql databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public PostgresSqlModelReader(Platform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
        setDefaultTablePattern(null);
    }

    @Override
    protected Collection<Table> readTables(String catalog, String schemaPattern, String[] tableTypes) throws SQLException {
        Collection<Table> tables = super.readTables(catalog, schemaPattern, tableTypes);
        for (Table table : tables) {
            // PostgresSQL also returns unique indexes for pk and non-pk auto-increment columns
            // which are of the form "[table]_[column]_key"
            Map<String, Index> uniquesByName = new HashMap<>();
            for (int indexIdx = 0; indexIdx < table.getIndexCount(); indexIdx++) {
                Index index = table.getIndex(indexIdx);
                if (index.isUnique() && (index.getName() != null)) {
                    uniquesByName.put(index.getName(), index);
                }
            }
            for (int columnIdx = 0; columnIdx < table.getColumnCount(); columnIdx++) {
                Column column = table.getColumn(columnIdx);

                if (column.isAutoIncrement()) {
                    String indexName = table.getName() + "_" + column.getName() + "_key";

                    if (uniquesByName.containsKey(indexName)) {
                        table.removeIndex(uniquesByName.get(indexName));
                        uniquesByName.remove(indexName);
                    }
                }
            }
        }
        return tables;
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, ContextMap values) throws SQLException {
        Column column = super.readColumn(metaData, values);
        if (column.getSize() != null) {
            if (column.getSizeAsInt() <= 0) {
                column.setSize(null);
                // PostgresSQL reports BYTEA and TEXT as BINARY(-1) and VARCHAR(-1) respectively
                // Since we cannot currently use the Blob/Clob interface with BYTEA, we instead
                // map them to LONGVARBINARY/LONGVARCHAR
                if (column.getTypeCode() == Types.BINARY) {
                    column.setTypeCode(Types.LONGVARBINARY);
                } else if (column.getTypeCode() == Types.VARCHAR) {
                    column.setTypeCode(Types.LONGVARCHAR);
                }
            }
            // fix issue DDLUTILS-165 as postgresql-8.2-504-jdbc3.jar seems to return Integer.MAX_VALUE
            // on columns defined as TEXT.
            else if (column.getSizeAsInt() == Integer.MAX_VALUE) {
                column.setSize(null);
                if (column.getTypeCode() == Types.VARCHAR) {
                    column.setTypeCode(Types.LONGVARCHAR);
                } else if (column.getTypeCode() == Types.BINARY) {
                    column.setTypeCode(Types.LONGVARBINARY);
                }
            }
        }

        String defaultValue = column.getDefaultValue();

        if ((defaultValue != null) && (!defaultValue.isEmpty())) {
            // If the default value looks like "nextval('ROUNDTRIP_VALUE_seq'::text)"
            // then it is an auto-increment column
            if (defaultValue.startsWith("nextval(")) {
                column.setAutoIncrement(true);
                defaultValue = null;
            } else {
                // PostgreSQL returns default values in the forms "-9000000000000000000::bigint" or
                // "'some value'::character varying" or "'2000-01-01'::date"
                defaultValue = switch (column.getTypeCode()) {
                    case Types.INTEGER, Types.BIGINT, Types.DECIMAL, Types.NUMERIC ->
                        extractUndelimitedDefaultValue(defaultValue);
                    case Types.CHAR, Types.VARCHAR, Types.LONGVARCHAR, Types.DATE, Types.TIME, Types.TIMESTAMP ->
                        extractDelimitedDefaultValue(defaultValue);
                    default -> defaultValue;
                };
                if (TypeMap.isTextType(column.getTypeCode())) {
                    // We assume escaping via double quote (see also the backslash_quote setting:
                    // http://www.postgresql.org/docs/7.4/interactive/runtime-config.html#RUNTIME-CONFIG-COMPATIBLE)
                    defaultValue = unescape(defaultValue, "'", "''");
                }
            }
            column.setDefaultValue(defaultValue);
        }
        return column;
    }

    /**
     * Extractes the default value from a default value spec of the form
     * "'some value'::character varying" or "'2000-01-01'::date".
     *
     * @param defaultValue The default value spec
     * @return The default value
     */
    private String extractDelimitedDefaultValue(String defaultValue) {
        if (defaultValue.startsWith("'")) {
            int valueEnd = defaultValue.indexOf("'::");

            if (valueEnd > 0) {
                return defaultValue.substring("'".length(), valueEnd);
            }
        }
        return defaultValue;
    }

    /**
     * Extractes the default value from a default value spec of the form
     * "-9000000000000000000::bigint".
     *
     * @param defaultValue The default value spec
     * @return The default value
     */
    private String extractUndelimitedDefaultValue(String defaultValue) {
        int valueEnd = defaultValue.indexOf("::");

        if (valueEnd > 0) {
            return defaultValue.substring(0, valueEnd);
        } else {
            return defaultValue;
        }
    }

    @Override
    protected boolean isInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk, Index index) {
        // PostgreSQL does not return an index for a foreign key
        return false;
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) {
        // PostgreSql uses the form "[table name]_pkey"
        return (table.getName() + "_pkey").equals(index.getName());
    }

}
