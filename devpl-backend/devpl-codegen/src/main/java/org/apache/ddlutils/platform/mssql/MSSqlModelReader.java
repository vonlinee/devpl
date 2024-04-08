package org.apache.ddlutils.platform.mssql;

import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.util.PojoMap;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Reads a database model from a Microsoft Sql Server database.
 */
public class MSSqlModelReader extends JdbcModelReader {
    /**
     * Known system tables that Sql Server creates (e.g. automatic maintenance).
     */
    private static final String[] KNOWN_SYSTEM_TABLES = {"dtproperties"};
    /**
     * The regular expression pattern for the ISO dates.
     */
    private final Pattern _isoDatePattern;
    /**
     * The regular expression pattern for the ISO times.
     */
    private final Pattern _isoTimePattern;

    /**
     * Creates a new model reader for Microsoft Sql Server databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public MSSqlModelReader(Platform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
        setDefaultTablePattern("%");

        try {
            _isoDatePattern = Pattern.compile("'(\\d{4}-\\d{2}-\\d{2})'");
            _isoTimePattern = Pattern.compile("'(\\d{2}:\\d{2}:\\d{2})'");
        } catch (PatternSyntaxException ex) {
            throw new DdlUtilsException(ex);
        }
    }

    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, PojoMap values) throws SQLException {
        String tableName = (String) values.get("TABLE_NAME");

        for (String knownSystemTable : KNOWN_SYSTEM_TABLES) {
            if (knownSystemTable.equals(tableName)) {
                return null;
            }
        }

        Table table = super.readTable(metaData, values);

        if (table != null) {
            // Sql Server does not return the auto-increment status via the database metadata
            determineAutoIncrementFromResultSetMetaData(table, table.getColumns());

            // TODO: Replace this manual filtering using named pks once they are available
            //       This is then probably of interest to every platform
            for (int idx = 0; idx < table.getIndexCount(); ) {
                Index index = table.getIndex(idx);

                if (index.isUnique() && existsPKWithName(metaData, table, index.getName())) {
                    table.removeIndex(idx);
                } else {
                    idx++;
                }
            }
        }
        return table;
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) {
        // Sql Server generates an index "PK__[table name]__[hex number]"

        String pkIndexName = "PK__" + table.getName() + "__";

        return index.getName().toUpperCase().startsWith(pkIndexName.toUpperCase());
    }

    /**
     * Determines whether there is a pk for the table with the given name.
     *
     * @param metaData The database metadata
     * @param table    The table
     * @param name     The pk name
     * @return <code>true</code> if there is such a pk
     */
    private boolean existsPKWithName(DatabaseMetaDataWrapper metaData, Table table, String name) throws SQLException {
        ResultSet pks = null;

        try {
            pks = metaData.getPrimaryKeys(metaData.escapeForSearch(table.getName()));

            while (pks.next()) {
                if (name.equals(pks.getString("PK_NAME"))) {
                    return true;
                }
            }
            return false;
        } finally {
            closeResultSet(pks);
        }
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, PojoMap values) throws SQLException {
        Column column = super.readColumn(metaData, values);
        String defaultValue = column.getDefaultValue();

        // Sql Server tends to surround the returned default value with one or two sets of parentheses
        if (defaultValue != null) {
            while (defaultValue.startsWith("(") && defaultValue.endsWith(")")) {
                defaultValue = defaultValue.substring(1, defaultValue.length() - 1);
            }

            if (column.getTypeCode() == Types.TIMESTAMP) {
                // Sql Server maintains the default values for DATE/TIME jdbc types, so we have to
                // migrate the default value to TIMESTAMP
                Matcher matcher = _isoDatePattern.matcher(defaultValue);
                Timestamp timestamp = null;

                if (matcher.matches()) {
                    timestamp = new Timestamp(Date.valueOf(matcher.group(1)).getTime());
                } else {
                    matcher = _isoTimePattern.matcher(defaultValue);

                    if (matcher.matches()) {
                        timestamp = new Timestamp(Time.valueOf(matcher.group(1)).getTime());
                    }
                }
                if (timestamp != null) {
                    defaultValue = timestamp.toString();
                }
            } else if (column.getTypeCode() == Types.DECIMAL) {
                // For some reason, Sql Server 2005 always returns DECIMAL default values with a dot
                // even if the scale is 0, so we remove the dot
                if ((column.getScale() == 0) && defaultValue.endsWith(".")) {
                    defaultValue = defaultValue.substring(0, defaultValue.length() - 1);
                }
            } else if (TypeMap.isTextType(column.getTypeCode())) {
                defaultValue = unescape(defaultValue, "'", "''");
            }

            column.setDefaultValue(defaultValue);
        }
        if ((column.getTypeCode() == Types.DECIMAL) && (column.getSizeAsInt() == 19) && (column.getScale() == 0)) {
            column.setTypeCode(Types.BIGINT);
        }
        return column;
    }
}
