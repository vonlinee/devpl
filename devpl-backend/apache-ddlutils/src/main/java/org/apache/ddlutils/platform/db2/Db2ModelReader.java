package org.apache.ddlutils.platform.db2;

import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.jdbc.meta.DatabaseMetadataReader;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Reads a database model from a Db2 UDB database.
 */
public class Db2ModelReader extends JdbcModelReader {
    /**
     * Known system tables that Db2 creates (e.g. automatic maintenance).
     */
    private static final String[] KNOWN_SYSTEM_TABLES = {"STMG_DBSIZE_INFO", "HMON_ATM_INFO", "HMON_COLLECTION", "POLICY"};
    /**
     * The regular expression pattern for the time values that Db2 returns.
     */
    private final Pattern _db2TimePattern;
    /**
     * The regular expression pattern for the timestamp values that Db2 returns.
     */
    private final Pattern _db2TimestampPattern;

    /**
     * Creates a new model reader for Db2 databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public Db2ModelReader(Platform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
        try {
            _db2TimePattern = Pattern.compile("'(\\d{2}).(\\d{2}).(\\d{2})'");
            _db2TimestampPattern = Pattern.compile("'(\\d{4}-\\d{2}-\\d{2})-(\\d{2}).(\\d{2}).(\\d{2})(\\.\\d{1,8})?'");
        } catch (PatternSyntaxException ex) {
            throw new DdlUtilsException(ex);
        }
    }

    @Override
    protected Collection<Table> readTables(String catalog, String schemaPattern, String[] tableTypes) throws SQLException {
        Collection<Table> tables = super.readTables(catalog, schemaPattern, tableTypes);
        Iterator<Table> iterator = tables.iterator();
        while (iterator.hasNext()) {
            Table table = iterator.next();
            for (String knownSystemTable : KNOWN_SYSTEM_TABLES) {
                if (knownSystemTable.equals(table.getName())) {
                    iterator.remove();
                    break;
                }
            }
        }
        for (Table table : tables) {
            // Db2 does not return the auto-increment status via the database metadata
            determineAutoIncrementColumns(table);
        }
        return tables;
    }

    @Override
    protected Collection<Column> readColumns(DatabaseMetadataReader reader, String catalog, String schema, String tableName) throws SQLException {
        Collection<Column> columns = super.readColumns(reader, catalog, schema, tableName);
        for (Column column : columns) {
            if (column.getDefaultValue() != null) {
                if (column.getTypeCode() == Types.TIME) {
                    Matcher matcher = _db2TimePattern.matcher(column.getDefaultValue());
                    // Db2 returns "HH24.MI.SS"
                    if (matcher.matches()) {
                        // the hour minute second
                        String newDefault = "'" + matcher.group(1) + ":" + matcher.group(2) + ":" + matcher.group(3) + "'";
                        column.setDefaultValue(newDefault);
                    }
                } else if (column.getTypeCode() == Types.TIMESTAMP) {
                    Matcher matcher = _db2TimestampPattern.matcher(column.getDefaultValue());
                    // Db2 returns "YYYY-MM-DD-HH24.MI.SS.FF"
                    if (matcher.matches()) {
                        StringBuilder newDefault = new StringBuilder();
                        newDefault.append("'");
                        // group 1 is the date which has the correct format
                        newDefault.append(matcher.group(1));
                        newDefault.append(" ");
                        // the hour
                        newDefault.append(matcher.group(2));
                        newDefault.append(":");
                        // the minute
                        newDefault.append(matcher.group(3));
                        newDefault.append(":");
                        // the second
                        newDefault.append(matcher.group(4));
                        // optionally, the fraction
                        if ((matcher.groupCount() >= 5) && (matcher.group(5) != null)) {
                            newDefault.append(matcher.group(5));
                        }
                        newDefault.append("'");

                        column.setDefaultValue(newDefault.toString());
                    }
                } else if (TypeMap.isTextType(column.getTypeCode())) {
                    column.setDefaultValue(unescape(column.getDefaultValue(), "'", "''"));
                }
            }
        }
        return columns;
    }

    /**
     * Helper method that determines the auto increment status using Firebird's system tables.
     *
     * @param table The table
     */
    protected void determineAutoIncrementColumns(Table table) throws SQLException {
        final String query = "SELECT COLNAME FROM SYSCAT.COLUMNS WHERE TABNAME = ? AND IDENTITY = 'Y' AND HIDDEN != 'S'";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, table.getName());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String colName = rs.getString(1).trim();
                Column column = table.findColumn(colName, getPlatform().isDelimitedIdentifierModeOn());
                if (column != null) {
                    column.setAutoIncrement(true);
                }
            }
        }
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) throws SQLException {
        // Db2 uses the form "SQL060205225246220" if the primary key was defined during table creation
        // When the ALTER TABLE way was used however, the index has the name of the primary key
        if (index.getName().startsWith("SQL")) {
            try {
                Long.parseLong(index.getName().substring(3));
                return true;
            } catch (NumberFormatException ex) {
                // we ignore it
            }
            return false;
        } else {
            // we'll compare the index name to the names of all primary keys
            // TODO: Once primary key names are supported, this can be done easier via the table object
            ResultSet pkData = null;
            HashSet<String> pkNames = new HashSet<>();

            try {
                pkData = metaData.getPrimaryKeys(metaData.escapeForSearch(table.getName()));
                while (pkData.next()) {
                    Map<String, Object> values = readColumns(pkData, getColumnsForPK());

                    pkNames.add((String) values.get("PK_NAME"));
                }
            } finally {
                closeResultSet(pkData);
            }

            return pkNames.contains(index.getName());
        }
    }
}
