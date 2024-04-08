package org.apache.ddlutils.platform.oracle;

import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.util.ListOrderedMap;
import org.apache.ddlutils.util.PojoMap;

import java.sql.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Reads a database model from an Oracle 8 database.
 */
public class Oracle8ModelReader extends JdbcModelReader {
    /**
     * The regular expression pattern for the Oracle conversion of ISO dates.
     */
    private final Pattern _oracleIsoDatePattern;
    /**
     * The regular expression pattern for the Oracle conversion of ISO times.
     */
    private final Pattern _oracleIsoTimePattern;
    /**
     * The regular expression pattern for the Oracle conversion of ISO timestamps.
     */
    private final Pattern _oracleIsoTimestampPattern;

    /**
     * Creates a new model reader for Oracle 8 databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public Oracle8ModelReader(Platform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
        setDefaultTablePattern("%");

        try {
            _oracleIsoDatePattern = Pattern.compile("TO_DATE\\('([^']*)', 'YYYY-MM-DD'\\)");
            _oracleIsoTimePattern = Pattern.compile("TO_DATE\\('([^']*)', 'HH24:MI:SS'\\)");
            _oracleIsoTimestampPattern = Pattern.compile("TO_DATE\\('([^']*)', 'YYYY-MM-DD HH24:MI:SS'\\)");
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
            String tableName = table.getName();
            // system table ?
            if (tableName.indexOf('$') > 0) {
                iterator.remove();
            } else {
                determineAutoIncrementColumns(table);
            }
        }
        return tables;
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, PojoMap values) throws SQLException {
        Column column = super.readColumn(metaData, values);

        if (column.getDefaultValue() != null) {
            // Oracle pads the default value with spaces
            column.setDefaultValue(column.getDefaultValue().trim());
        }
        if (column.getTypeCode() == Types.DECIMAL) {
            // We're back-mapping the NUMBER columns returned by Oracle
            // Note that the JDBC driver returns DECIMAL for these NUMBER columns
            switch (column.getSizeAsInt()) {
                case 1 -> {
                    if (column.getScale() == 0) {
                        column.setTypeCode(Types.BIT);
                    }
                }
                case 3 -> {
                    if (column.getScale() == 0) {
                        column.setTypeCode(Types.TINYINT);
                    }
                }
                case 5 -> {
                    if (column.getScale() == 0) {
                        column.setTypeCode(Types.SMALLINT);
                    }
                }
                case 18 -> column.setTypeCode(Types.REAL);
                case 22 -> {
                    if (column.getScale() == 0) {
                        column.setTypeCode(Types.INTEGER);
                    }
                }
                case 38 -> {
                    if (column.getScale() == 0) {
                        column.setTypeCode(Types.BIGINT);
                    } else {
                        column.setTypeCode(Types.DOUBLE);
                    }
                }
            }
        } else if (column.getTypeCode() == Types.FLOAT) {
            // Same for REAL, FLOAT, DOUBLE PRECISION, which all back-map to FLOAT but with
            // different sizes (63 for REAL, 126 for FLOAT/DOUBLE PRECISION)
            switch (column.getSizeAsInt()) {
                case 63 -> column.setTypeCode(Types.REAL);
                case 126 -> column.setTypeCode(Types.DOUBLE);
            }
        } else if ((column.getTypeCode() == Types.DATE) || (column.getTypeCode() == Types.TIMESTAMP)) {
            // Oracle has only one DATE/TIME type, so we can't know which it is and thus map
            // it back to TIMESTAMP
            column.setTypeCode(Types.TIMESTAMP);

            // we also reverse the ISO-format adaptation, and adjust the default value to timestamp
            if (column.getDefaultValue() != null) {
                Matcher matcher = _oracleIsoTimestampPattern.matcher(column.getDefaultValue());
                Timestamp timestamp = null;

                if (matcher.matches()) {
                    String timestampVal = matcher.group(1);

                    timestamp = Timestamp.valueOf(timestampVal);
                } else {
                    matcher = _oracleIsoDatePattern.matcher(column.getDefaultValue());
                    if (matcher.matches()) {
                        String dateVal = matcher.group(1);

                        timestamp = new Timestamp(Date.valueOf(dateVal).getTime());
                    } else {
                        matcher = _oracleIsoTimePattern.matcher(column.getDefaultValue());
                        if (matcher.matches()) {
                            String timeVal = matcher.group(1);

                            timestamp = new Timestamp(Time.valueOf(timeVal).getTime());
                        }
                    }
                }
                if (timestamp != null) {
                    column.setDefaultValue(timestamp.toString());
                }
            }
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
        Column[] columns = table.getColumns();

        for (Column column : columns) {
            column.setAutoIncrement(isAutoIncrement(table, column));
        }
    }

    /**
     * Tries to determine whether the given column is an identity column.
     *
     * @param table  The table
     * @param column The column
     * @return <code>true</code> if the column is an identity column
     */
    protected boolean isAutoIncrement(Table table, Column column) throws SQLException {
        // TODO: For now, we only check whether there is a sequence & trigger as generated by DdlUtils
        //       But once sequence/trigger support is in place, it might be possible to 'parse' the
        //       trigger body (via SELECT trigger_name, trigger_body FROM user_triggers) in order to
        //       determine whether it fits our auto-increment definition
        final String triggerQuery = "SELECT * FROM user_triggers WHERE trigger_name = ?";
        final String sequenceQuery = "SELECT * FROM user_sequences WHERE sequence_name = ?";

        PreparedStatement prepStmt = null;
        String triggerName = getPlatform().getSqlBuilder().getConstraintName("trg", table, column.getName(), null);
        String seqName = getPlatform().getSqlBuilder().getConstraintName("seq", table, column.getName(), null);

        if (!getPlatform().isDelimitedIdentifierModeOn()) {
            triggerName = triggerName.toUpperCase();
            seqName = seqName.toUpperCase();
        }
        try {
            prepStmt = getConnection().prepareStatement(triggerQuery);
            prepStmt.setString(1, triggerName);

            ResultSet resultSet = prepStmt.executeQuery();

            if (!resultSet.next()) {
                return false;
            }
            // we have a trigger, so lets check the sequence
            closeStatement(prepStmt);

            prepStmt = getConnection().prepareStatement(sequenceQuery);
            prepStmt.setString(1, seqName);

            resultSet = prepStmt.executeQuery();
            return resultSet.next();
        } finally {
            closeStatement(prepStmt);
        }
    }

    @Override
    protected Collection<Index> readIndices(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        // Oracle has a bug in the DatabaseMetaData#getIndexInfo method which fails when
        // delimited identifiers are being used
        // Therefore, we're rather accessing the user_indexes table which contains the same info
        // This also allows us to filter system-generated indices which are identified by either
        // having GENERATED='Y' in the query result, or by their index names being equal to the
        // name of the primary key of the table

        final String query =
            "SELECT a.INDEX_NAME, a.INDEX_TYPE, a.UNIQUENESS, b.COLUMN_NAME, b.COLUMN_POSITION FROM USER_INDEXES a, USER_IND_COLUMNS b WHERE " +
                "a.TABLE_NAME=? AND a.GENERATED=? AND a.TABLE_TYPE=? AND a.TABLE_NAME=b.TABLE_NAME AND a.INDEX_NAME=b.INDEX_NAME AND " +
                "a.INDEX_NAME NOT IN (SELECT DISTINCT c.CONSTRAINT_NAME FROM USER_CONSTRAINTS c WHERE c.CONSTRAINT_TYPE=? AND c.TABLE_NAME=a.TABLE_NAME)";
        final String queryWithSchema =
            query.substring(0, query.length() - 1) + " AND c.OWNER LIKE ?) AND a.TABLE_OWNER LIKE ?";

        Map<String, Index> indices = new ListOrderedMap<>();
        PreparedStatement stmt = null;

        try {
            stmt = getConnection().prepareStatement(metaData.getSchemaPattern() == null ? query : queryWithSchema);
            stmt.setString(1, getPlatform().isDelimitedIdentifierModeOn() ? tableName : tableName.toUpperCase());
            stmt.setString(2, "N");
            stmt.setString(3, "TABLE");
            stmt.setString(4, "P");
            if (metaData.getSchemaPattern() != null) {
                stmt.setString(5, metaData.getSchemaPattern().toUpperCase());
                stmt.setString(6, metaData.getSchemaPattern().toUpperCase());
            }

            ResultSet rs = stmt.executeQuery();
            PojoMap values = new PojoMap();

            while (rs.next()) {
                values.put("INDEX_NAME", rs.getString(1));
                values.put("INDEX_TYPE", DatabaseMetaData.tableIndexOther);
                values.put("NON_UNIQUE", "UNIQUE".equalsIgnoreCase(rs.getString(3)) ? Boolean.FALSE : Boolean.TRUE);
                values.put("COLUMN_NAME", rs.getString(4));
                values.put("ORDINAL_POSITION", rs.getShort(5));

                readIndex(metaData, values, indices);
            }
        } finally {
            closeStatement(stmt);
        }
        return indices.values();
    }
}
