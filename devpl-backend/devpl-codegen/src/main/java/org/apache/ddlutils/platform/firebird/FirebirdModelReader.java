package org.apache.ddlutils.platform.firebird;

import io.devpl.codegen.jdbc.meta.DatabaseMetadataReader;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.util.ListOrderedMap;
import org.apache.ddlutils.util.PojoMap;

import java.sql.*;
import java.util.*;

/**
 * The Jdbc Model Reader for Firebird.
 */
public class FirebirdModelReader extends JdbcModelReader {
    /**
     * Creates a new model reader for Firebird databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public FirebirdModelReader(Platform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
        setDefaultTablePattern("%");
    }

    @Override
    protected Collection<Table> readTables(String catalog, String schemaPattern, String[] tableTypes) throws SQLException {
        Collection<Table> tables = super.readTables(catalog, schemaPattern, tableTypes);
        for (Table table : tables) {
            determineAutoIncrementColumns(table);
        }
        return tables;
    }

    @Override
    protected Collection<Column> readColumns(DatabaseMetadataReader reader, String catalog, String schema, String tableName) throws SQLException {
        if (getPlatform().isDelimitedIdentifierModeOn()) {
            // Jaybird has a problem when delimited identifiers are used as
            // it is not able to find the columns for the table
            // So we have to filter manually below
            catalog = getDefaultTablePattern();
            schema = getDefaultColumnPattern();
        } else {
            catalog = escapeForSearch(reader, tableName);
        }
        return super.readColumns(reader, catalog, schema, tableName);
    }

    @Override
    protected Collection<Column> readColumns(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        ResultSet columnData = null;

        try {
            List<Column> columns = new ArrayList<>();

            if (getPlatform().isDelimitedIdentifierModeOn()) {
                // Jaybird has a problem when delimited identifiers are used as
                // it is not able to find the columns for the table
                // So we have to filter manually below
                columnData = metaData.getColumns(getDefaultTablePattern(), getDefaultColumnPattern());

                while (columnData.next()) {
                    PojoMap values = readColumns(columnData, getColumnsForColumn());

                    if (tableName.equals(values.get("TABLE_NAME"))) {
                        columns.add(readColumn(metaData, values));
                    }
                }
            } else {
                columnData = metaData.getColumns(metaData.escapeForSearch(tableName), getDefaultColumnPattern());

                while (columnData.next()) {
                    PojoMap values = readColumns(columnData, getColumnsForColumn());

                    columns.add(readColumn(metaData, values));
                }
            }

            return columns;
        } finally {
            closeResultSet(columnData);
        }
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, PojoMap values) throws SQLException {
        Column column = super.readColumn(metaData, values);

        if (column.getTypeCode() == Types.FLOAT) {
            column.setTypeCode(Types.REAL);
        } else if (TypeMap.isTextType(column.getTypeCode())) {
            column.setDefaultValue(unescape(column.getDefaultValue(), "'", "''"));
        }
        return column;
    }

    /**
     * Helper method that determines the auto increment status using Firebird's system tables.
     *
     * @param table The table
     */
    protected void determineAutoIncrementColumns(Table table) throws SQLException {
        // Since for long table and column names, the generator name will be shortened
        // we have to determine for each column whether there is a generator for it
        final String query = "SELECT RDB$GENERATOR_NAME FROM RDB$GENERATORS WHERE RDB$GENERATOR_NAME NOT LIKE '%$%'";

        FirebirdBuilder builder = (FirebirdBuilder) getPlatform().getSqlBuilder();
        Column[] columns = table.getColumns();
        HashMap<String, Column> names = new HashMap<>();

        for (Column value : columns) {
            String name = builder.getGeneratorName(table, value);
            if (!getPlatform().isDelimitedIdentifierModeOn()) {
                name = name.toUpperCase();
            }
            names.put(name, value);
        }

        try (Statement stmt = getConnection().createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    String generatorName = rs.getString(1).trim();
                    Column column = names.get(generatorName);
                    if (column != null) {
                        column.setAutoIncrement(true);
                    }
                }
            }
        }
    }

    @Override
    protected Collection<String> readPrimaryKeyNames(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        List<String> pks = new ArrayList<>();
        ResultSet pkData = null;

        try {
            if (getPlatform().isDelimitedIdentifierModeOn()) {
                // Jaybird has a problem when delimited identifiers are used as
                // it is not able to find the primary key info for the table
                // So we have to filter manually below
                pkData = metaData.getPrimaryKeys(getDefaultTablePattern());
                while (pkData.next()) {
                    PojoMap values = readColumns(pkData, getColumnsForPK());

                    if (tableName.equals(values.get("TABLE_NAME"))) {
                        pks.add(readPrimaryKeyName(metaData, values));
                    }
                }
            } else {
                pkData = metaData.getPrimaryKeys(metaData.escapeForSearch(tableName));
                while (pkData.next()) {
                    PojoMap values = readColumns(pkData, getColumnsForPK());

                    pks.add(readPrimaryKeyName(metaData, values));
                }
            }
        } finally {
            closeResultSet(pkData);
        }
        return pks;
    }

    @Override
    protected Collection<ForeignKey> readForeignKeys(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        Map<String, ForeignKey> fks = new ListOrderedMap<>();
        ResultSet fkData = null;

        try {
            if (getPlatform().isDelimitedIdentifierModeOn()) {
                // Jaybird has a problem when delimited identifiers are used as
                // it is not able to find the foreign key info for the table
                // So we have to filter manually below
                fkData = metaData.getForeignKeys(getDefaultTablePattern());
                while (fkData.next()) {
                    PojoMap values = readColumns(fkData, getColumnsForFK());

                    if (tableName.equals(values.get("FKTABLE_NAME"))) {
                        readForeignKey(metaData, values, fks);
                    }
                }
            } else {
                fkData = metaData.getForeignKeys(metaData.escapeForSearch(tableName));
                while (fkData.next()) {
                    PojoMap values = readColumns(fkData, getColumnsForFK());

                    readForeignKey(metaData, values, fks);
                }
            }
        } finally {
            closeResultSet(fkData);
        }
        return fks.values();
    }

    @Override
    protected Collection<Index> readIndices(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        // Jaybird is not able to read indices when delimited identifiers are turned on,
        // so we gather the data manually using Firebird's system tables
        final String query = "SELECT a.RDB$INDEX_NAME INDEX_NAME, b.RDB$RELATION_NAME TABLE_NAME, b.RDB$UNIQUE_FLAG NON_UNIQUE, " + "a.RDB$FIELD_POSITION ORDINAL_POSITION, a.RDB$FIELD_NAME COLUMN_NAME, 3 INDEX_TYPE " + "FROM RDB$INDEX_SEGMENTS a, RDB$INDICES b WHERE a.RDB$INDEX_NAME=b.RDB$INDEX_NAME AND b.RDB$RELATION_NAME = ?";
        Map<String, Index> indices = new LinkedHashMap<>();
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(query);
            stmt.setString(1, getPlatform().isDelimitedIdentifierModeOn() ? tableName : tableName.toUpperCase());

            ResultSet indexData = stmt.executeQuery();
            while (indexData.next()) {
                PojoMap values = readColumns(indexData, getColumnsForIndex());

                // we have to reverse the meaning of the unique flag; also, null means false
                values.add("NON_UNIQUE", (values.get("NON_UNIQUE") == null) || Boolean.FALSE.equals(values.get("NON_UNIQUE")) ? Boolean.TRUE : Boolean.FALSE);
                // and trim the names
                values.add("INDEX_NAME", ((String) values.get("INDEX_NAME")).trim());
                values.add("TABLE_NAME", ((String) values.get("TABLE_NAME")).trim());
                values.add("COLUMN_NAME", ((String) values.get("COLUMN_NAME")).trim());
                readIndex(metaData, values, indices);
            }
        } finally {
            closeStatement(stmt);
        }
        return indices.values();
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) throws SQLException {
        final String query = "SELECT RDB$CONSTRAINT_NAME FROM RDB$RELATION_CONSTRAINTS " + "WHERE RDB$RELATION_NAME=? AND RDB$CONSTRAINT_TYPE=? AND RDB$INDEX_NAME=?";

        String tableName = getPlatform().getSqlBuilder().getTableName(table);
        String indexName = getPlatform().getSqlBuilder().getIndexName(index);
        PreparedStatement stmt = null;

        try {
            stmt = getConnection().prepareStatement(query);
            stmt.setString(1, getPlatform().isDelimitedIdentifierModeOn() ? tableName : tableName.toUpperCase());
            stmt.setString(2, "PRIMARY KEY");
            stmt.setString(3, indexName);

            ResultSet resultSet = stmt.executeQuery();

            return resultSet.next();
        } finally {
            closeStatement(stmt);
        }
    }

    @Override
    protected boolean isInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk, Index index) throws SQLException {
        final String query = "SELECT RDB$CONSTRAINT_NAME FROM RDB$RELATION_CONSTRAINTS " + "WHERE RDB$RELATION_NAME=? AND RDB$CONSTRAINT_TYPE=? AND RDB$CONSTRAINT_NAME=? AND RDB$INDEX_NAME=?";

        String tableName = getPlatform().getSqlBuilder().getTableName(table);
        String indexName = getPlatform().getSqlBuilder().getIndexName(index);
        String fkName = getPlatform().getSqlBuilder().getForeignKeyName(table, fk);

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, getPlatform().isDelimitedIdentifierModeOn() ? tableName : tableName.toUpperCase());
            stmt.setString(2, "FOREIGN KEY");
            stmt.setString(3, fkName);
            stmt.setString(4, indexName);

            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public String determineSchemaOf(Connection connection, String schemaPattern, Table table) throws SQLException {
        ResultSet tableData = null;
        ResultSet columnData = null;
        try {
            DatabaseMetaDataWrapper metaData = new DatabaseMetaDataWrapper();
            metaData.setMetaData(connection.getMetaData());
            metaData.setCatalog(getDefaultCatalogPattern());
            metaData.setSchemaPattern(schemaPattern == null ? getDefaultSchemaPattern() : schemaPattern);
            metaData.setTableTypes(getDefaultTableTypes());

            String tablePattern = table.getName();

            if (getPlatform().isDelimitedIdentifierModeOn()) {
                tablePattern = tablePattern.toUpperCase();
            }

            tableData = metaData.getTables(metaData.escapeForSearch(tablePattern));

            boolean found = false;
            String schema = null;

            while (!found && tableData.next()) {
                PojoMap values = readColumns(tableData, getColumnsForTable());
                String tableName = (String) values.get("TABLE_NAME");

                if ((tableName != null) && (!tableName.isEmpty())) {
                    schema = (String) values.get("TABLE_SCHEM");
                    found = true;

                    if (getPlatform().isDelimitedIdentifierModeOn()) {
                        // Jaybird has a problem when delimited identifiers are used as
                        // it is not able to find the columns for the table
                        // So we have to filter manually below
                        columnData = metaData.getColumns(getDefaultTablePattern(), getDefaultColumnPattern());
                    } else {
                        columnData = metaData.getColumns(metaData.escapeForSearch(tableName), getDefaultColumnPattern());
                    }

                    while (found && columnData.next()) {
                        values = readColumns(columnData, getColumnsForColumn());

                        if (getPlatform().isDelimitedIdentifierModeOn() && !tableName.equals(values.getString("TABLE_NAME"))) {
                            continue;
                        }

                        if (table.findColumn(values.getString("COLUMN_NAME"), getPlatform().isDelimitedIdentifierModeOn()) == null) {
                            found = false;
                        }
                    }
                    closeResultSet(columnData);
                    columnData = null;
                }
            }
            return found ? schema : null;
        } finally {
            closeResultSet(columnData);
            closeResultSet(tableData);
        }
    }
}
