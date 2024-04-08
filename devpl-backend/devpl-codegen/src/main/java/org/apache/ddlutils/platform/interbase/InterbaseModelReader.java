package org.apache.ddlutils.platform.interbase;


import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.util.ContextMap;
import org.apache.ddlutils.util.ListOrderedMap;

import java.sql.*;
import java.util.*;

/**
 * The Jdbc Model Reader for Interbase.
 */
public class InterbaseModelReader extends JdbcModelReader {
    /**
     * Creates a new model reader for Interbase databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public InterbaseModelReader(Platform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
        setDefaultTablePattern("%");
        setDefaultColumnPattern("%");
    }

    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, ContextMap values) throws SQLException {
        Table table = super.readTable(metaData, values);

        if (table != null) {
            determineExtraColumnInfo(table);
            determineAutoIncrementColumns(table);
            adjustColumns(table);
        }

        return table;
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
                    ContextMap values = readColumns(columnData, getColumnsForColumn());

                    if (tableName.equals(values.get("TABLE_NAME"))) {
                        columns.add(readColumn(metaData, values));
                    }
                }
            } else {
                columnData = metaData.getColumns(metaData.escapeForSearch(tableName), getDefaultColumnPattern());

                while (columnData.next()) {
                    ContextMap values = readColumns(columnData, getColumnsForColumn());

                    columns.add(readColumn(metaData, values));
                }
            }

            return columns;
        } finally {
            closeResultSet(columnData);
        }
    }

    /**
     * Helper method that determines extra column info from the system tables: default value, precision, scale.
     *
     * @param table The table
     */
    protected void determineExtraColumnInfo(Table table) throws SQLException {
        final String query =
            "SELECT a.RDB$FIELD_NAME, a.RDB$DEFAULT_SOURCE, b.RDB$FIELD_PRECISION, b.RDB$FIELD_SCALE," +
            " b.RDB$FIELD_TYPE, b.RDB$FIELD_SUB_TYPE FROM RDB$RELATION_FIELDS a, RDB$FIELDS b" +
            " WHERE a.RDB$RELATION_NAME=? AND a.RDB$FIELD_SOURCE=b.RDB$FIELD_NAME";

        PreparedStatement prepStmt = null;

        try {
            prepStmt = getConnection().prepareStatement(query);
            prepStmt.setString(1, getPlatform().isDelimitedIdentifierModeOn() ? table.getName() : table.getName().toUpperCase());

            ResultSet rs = prepStmt.executeQuery();

            while (rs.next()) {
                String columnName = rs.getString(1).trim();
                Column column = table.findColumn(columnName, getPlatform().isDelimitedIdentifierModeOn());

                if (column != null) {
                    String defaultValue = rs.getString(2);

                    if (!rs.wasNull() && (defaultValue != null)) {
                        defaultValue = defaultValue.trim();
                        if (defaultValue.startsWith("DEFAULT ")) {
                            defaultValue = defaultValue.substring("DEFAULT ".length());
                        }
                        column.setDefaultValue(defaultValue);
                    }

                    short precision = rs.getShort(3);
                    boolean precisionSpecified = !rs.wasNull();
                    short scale = rs.getShort(4);
                    boolean scaleSpecified = !rs.wasNull();

                    if (precisionSpecified) {
                        // for some reason, Interbase stores the negative scale
                        column.setSizeAndScale(precision, scaleSpecified ? -scale : 0);
                    }

                    short dbType = rs.getShort(5);
                    short blobSubType = rs.getShort(6);

                    // CLOBs are returned by the driver as VARCHAR
                    if (!rs.wasNull() && (dbType == 261) && (blobSubType == 1)) {
                        column.setTypeCode(Types.CLOB);
                    }
                }
            }
        } finally {
            closeStatement(prepStmt);
        }
    }

    /**
     * Helper method that determines the auto increment status using Interbase's system tables.
     *
     * @param table The table
     */
    protected void determineAutoIncrementColumns(Table table) throws SQLException {
        // Since for long table and column names, the generator name will be shortened
        // we have to determine for each column whether there is a generator for it
        final String query = "SELECT RDB$GENERATOR_NAME FROM RDB$GENERATORS";

        InterbaseBuilder builder = (InterbaseBuilder) getPlatform().getSqlBuilder();
        Column[] columns = table.getColumns();
        HashMap<String, Column> names = new HashMap<>();
        String name;

        for (Column value : columns) {
            name = builder.getGeneratorName(table, value);
            if (!getPlatform().isDelimitedIdentifierModeOn()) {
                name = name.toUpperCase();
            }
            names.put(name, value);
        }

        Statement stmt = null;

        try {
            stmt = getConnection().createStatement();

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String generatorName = rs.getString(1).trim();
                Column column = names.get(generatorName);

                if (column != null) {
                    column.setAutoIncrement(true);
                }
            }
        } finally {
            closeStatement(stmt);
        }
    }

    /**
     * Adjusts the columns in the table by fixing types and default values.
     *
     * @param table The table
     */
    protected void adjustColumns(Table table) {
        Column[] columns = table.getColumns();

        for (Column column : columns) {
            if (column.getTypeCode() == Types.FLOAT) {
                column.setTypeCode(Types.REAL);
            } else if ((column.getTypeCode() == Types.NUMERIC) || (column.getTypeCode() == Types.DECIMAL)) {
                if ((column.getTypeCode() == Types.NUMERIC) && (column.getSizeAsInt() == 18) && (column.getScale() == 0)) {
                    column.setTypeCode(Types.BIGINT);
                }
            } else if (TypeMap.isTextType(column.getTypeCode())) {
                column.setDefaultValue(unescape(column.getDefaultValue(), "'", "''"));
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
                    ContextMap values = readColumns(pkData, getColumnsForPK());
                    if (tableName.equals(values.get("TABLE_NAME"))) {
                        pks.add(readPrimaryKeyName(metaData, values));
                    }
                }
            } else {
                pkData = metaData.getPrimaryKeys(metaData.escapeForSearch(tableName));
                while (pkData.next()) {
                    ContextMap values = readColumns(pkData, getColumnsForPK());

                    pks.add(readPrimaryKeyName(metaData, values));
                }
            }
        } finally {
            closeResultSet(pkData);
        }
        return pks;
    }

    @Override
    protected Collection readForeignKeys(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        Map fks = new ListOrderedMap<>();
        ResultSet fkData = null;

        try {
            if (getPlatform().isDelimitedIdentifierModeOn()) {
                // Jaybird has a problem when delimited identifiers are used as
                // it is not able to find the foreign key info for the table
                // So we have to filter manually below
                fkData = metaData.getForeignKeys(getDefaultTablePattern());
                while (fkData.next()) {
                    ContextMap values = readColumns(fkData, getColumnsForFK());

                    if (tableName.equals(values.get("FKTABLE_NAME"))) {
                        readForeignKey(metaData, values, fks);
                    }
                }
            } else {
                fkData = metaData.getForeignKeys(metaData.escapeForSearch(tableName));
                while (fkData.next()) {
                    ContextMap values = readColumns(fkData, getColumnsForFK());

                    readForeignKey(metaData, values, fks);
                }
            }
        } finally {
            closeResultSet(fkData);
        }
        return fks.values();
    }

    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) throws SQLException {
        final String query =
            "SELECT RDB$CONSTRAINT_NAME FROM RDB$RELATION_CONSTRAINTS " +
            "WHERE RDB$RELATION_NAME=? AND RDB$CONSTRAINT_TYPE=? AND RDB$INDEX_NAME=?";

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

    protected boolean isInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk, Index index) throws SQLException {
        final String query =
            "SELECT RDB$CONSTRAINT_NAME FROM RDB$RELATION_CONSTRAINTS " +
            "WHERE RDB$RELATION_NAME=? AND RDB$CONSTRAINT_TYPE=? AND RDB$CONSTRAINT_NAME=? AND RDB$INDEX_NAME=?";

        String tableName = getPlatform().getSqlBuilder().getTableName(table);
        String indexName = getPlatform().getSqlBuilder().getIndexName(index);
        String fkName = getPlatform().getSqlBuilder().getForeignKeyName(table, fk);
        PreparedStatement stmt = null;

        try {
            stmt = getConnection().prepareStatement(query);
            stmt.setString(1, getPlatform().isDelimitedIdentifierModeOn() ? tableName : tableName.toUpperCase());
            stmt.setString(2, "FOREIGN KEY");
            stmt.setString(3, fkName);
            stmt.setString(4, indexName);

            ResultSet resultSet = stmt.executeQuery();

            return resultSet.next();
        } finally {
            closeStatement(stmt);
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
                Map values = readColumns(tableData, getColumnsForTable());
                String tableName = (String) values.get("TABLE_NAME");

                if ((tableName != null) && (tableName.length() > 0)) {
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

                        if (getPlatform().isDelimitedIdentifierModeOn() &&
                            !tableName.equals(values.get("TABLE_NAME"))) {
                            continue;
                        }

                        if (table.findColumn((String) values.get("COLUMN_NAME"),
                            getPlatform().isDelimitedIdentifierModeOn()) == null) {
                            found = false;
                        }
                    }
                    columnData.close();
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
