package org.apache.ddlutils.platform.postgresql;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.util.ObjectMap;

import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

/**
 * Reads a database model from a PostgreSql database.
 * @version $Revision: $
 */
public class PostgreSqlModelReader extends JdbcModelReader {
    /**
     * Creates a new model reader for PostgreSql databases.
     * @param platform The platform that this model reader belongs to
     */
    public PostgreSqlModelReader(DatabasePlatform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
        setDefaultTablePattern(null);
    }

    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, ObjectMap values) throws SQLException {
        Table table = super.readTable(metaData, values);

        if (table != null) {
            // PostgreSQL also returns unique indexes for pk and non-pk auto-increment columns
            // which are of the form "[table]_[column]_key"
            HashMap<String, Index> uniquesByName = new HashMap<>();

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
        return table;
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, ObjectMap values) throws SQLException {
        Column column = super.readColumn(metaData, values);

        if (column.getSize() != null) {
            if (column.getSizeAsInt() <= 0) {
                column.setSize(null);
                // PostgreSQL reports BYTEA and TEXT as BINARY(-1) and VARCHAR(-1) respectively
                // Since we cannot currently use the Blob/Clob interface with BYTEA, we instead
                // map them to LONGVARBINARY/LONGVARCHAR
                if (column.getJdbcTypeCode() == Types.BINARY) {
                    column.setJdbcTypeCode(Types.LONGVARBINARY);
                } else if (column.getJdbcTypeCode() == Types.VARCHAR) {
                    column.setJdbcTypeCode(Types.LONGVARCHAR);
                }
            }
            // fix issue DDLUTILS-165 as postgresql-8.2-504-jdbc3.jar seems to return Integer.MAX_VALUE
            // on columns defined as TEXT.
            else if (column.getSizeAsInt() == Integer.MAX_VALUE) {
                column.setSize(null);
                if (column.getJdbcTypeCode() == Types.VARCHAR) {
                    column.setJdbcTypeCode(Types.LONGVARCHAR);
                } else if (column.getJdbcTypeCode() == Types.BINARY) {
                    column.setJdbcTypeCode(Types.LONGVARBINARY);
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
                switch (column.getJdbcTypeCode()) {
                    case Types.INTEGER:
                    case Types.BIGINT:
                    case Types.DECIMAL:
                    case Types.NUMERIC:
                        defaultValue = extractUndelimitedDefaultValue(defaultValue);
                        break;
                    case Types.CHAR:
                    case Types.VARCHAR:
                    case Types.LONGVARCHAR:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                        defaultValue = extractDelimitedDefaultValue(defaultValue);
                        break;
                }
                if (TypeMap.isTextType(column.getJdbcTypeCode())) {
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
        // PostgreSql uses the form "[tablename]_pkey"
        return (table.getName() + "_pkey").equals(index.getName());
    }

}
