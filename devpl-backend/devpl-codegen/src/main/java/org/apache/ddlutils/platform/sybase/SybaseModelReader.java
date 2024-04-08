package org.apache.ddlutils.platform.sybase;


import io.devpl.codegen.jdbc.meta.DatabaseMetadataReader;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.util.PojoMap;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Reads a database model from a Sybase database.
 */
public class SybaseModelReader extends JdbcModelReader {
    /**
     * The regular expression pattern for the ISO dates.
     */
    private final Pattern _isoDatePattern;
    /**
     * The regular expression pattern for the ISO times.
     */
    private final Pattern _isoTimePattern;

    /**
     * Creates a new model reader for Sybase databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public SybaseModelReader(Platform platform) {
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
    protected Collection<Table> readTables(String catalog, String schemaPattern, String[] tableTypes) throws SQLException {
        Collection<Table> tables = super.readTables(catalog, schemaPattern, tableTypes);
        for (Table table : tables) {
            // Sybase does not return the auto-increment status via the database metadata
            determineAutoIncrementFromResultSetMetaData(table, table.getColumns());
        }
        return tables;
    }

    @Override
    protected Collection<Column> readColumns(DatabaseMetadataReader reader, String catalog, String schema, String tableName) throws SQLException {
        Collection<Column> columns = super.readColumns(reader, catalog, schema, tableName);
        for (Column column : columns) {
            if ((column.getTypeCode() == Types.DECIMAL) && (column.getSizeAsInt() == 19) && (column.getScale() == 0)) {
                // Back-mapping to BIGINT
                column.setTypeCode(Types.BIGINT);
            } else if (column.getDefaultValue() != null) {
                if (column.getTypeCode() == Types.TIMESTAMP) {
                    // Sybase maintains the default values for DATE/TIME jdbc types, so we have to
                    // migrate the default value to TIMESTAMP
                    Matcher matcher = _isoDatePattern.matcher(column.getDefaultValue());
                    Timestamp timestamp = null;

                    if (matcher.matches()) {
                        timestamp = new Timestamp(Date.valueOf(matcher.group(1)).getTime());
                    } else {
                        matcher = _isoTimePattern.matcher(column.getDefaultValue());
                        if (matcher.matches()) {
                            timestamp = new Timestamp(Time.valueOf(matcher.group(1)).getTime());
                        }
                    }
                    if (timestamp != null) {
                        column.setDefaultValue(timestamp.toString());
                    }
                } else if (TypeMap.isTextType(column.getTypeCode())) {
                    column.setDefaultValue(unescape(column.getDefaultValue(), "'", "''"));
                }
            }
        }
        return columns;
    }

    @Override
    protected void readIndex(DatabaseMetaDataWrapper metaData, PojoMap values, Map<String, Index> knownIndices) throws SQLException {
        if (getPlatform().isDelimitedIdentifierModeOn()) {
            String indexName = (String) values.get("INDEX_NAME");

            // Sometimes, Sybase keeps the delimiter quotes around the index names
            // when returning them in the metadata, so we strip them
            if (indexName != null) {
                String delimiter = getPlatformInfo().getDelimiterToken();

                if (indexName.startsWith(delimiter) && indexName.endsWith(delimiter)) {
                    indexName = indexName.substring(delimiter.length(), indexName.length() - delimiter.length());
                    values.put("INDEX_NAME", indexName);
                }
            }
        }
        super.readIndex(metaData, values, knownIndices);
    }

    @Override
    protected Collection<ForeignKey> readForeignKeys(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        // Sybase (or jConnect) does not return the foreign key names, thus we have to
        // read the foreign keys manually from the system tables
        final String colQuery =
            "SELECT refobjs.name, localtables.id, remotetables.name, remotetables.id," +
            "       refs.fokey1, refs.refkey1, refs.fokey2, refs.refkey2, refs.fokey3, refs.refkey3, refs.fokey4, refs.refkey4," +
            "       refs.fokey5, refs.refkey5, refs.fokey6, refs.refkey6, refs.fokey7, refs.refkey7, refs.fokey8, refs.refkey8," +
            "       refs.fokey9, refs.refkey9, refs.fokey10, refs.refkey10, refs.fokey11, refs.refkey11, refs.fokey12, refs.refkey12," +
            "       refs.fokey13, refs.refkey13, refs.fokey14, refs.refkey14, refs.fokey15, refs.refkey15, refs.fokey16, refs.refkey16," +
            " FROM sysreferences refs, sysobjects refobjs, sysobjects localtables, sysobjects remotetables" +
            " WHERE refobjs.type = 'RI' AND refs.constrid = refobjs.id AND" +
            "       localtables.type = 'U' AND refs.tableid = localtables.id AND localtables.name = ?" +
            "   AND remotetables.type = 'U' AND refs.reftabid = remotetables.id";
        final String refObjQuery =
            "SELECT name FROM syscolumns WHERE id = ? AND colid = ?";

        PreparedStatement colStmt = null;
        PreparedStatement refObjStmt = null;
        List<ForeignKey> result = new ArrayList<>();

        try {
            colStmt = getConnection().prepareStatement(colQuery);
            refObjStmt = getConnection().prepareStatement(refObjQuery);

            ResultSet fkRs = colStmt.executeQuery();

            while (fkRs.next()) {
                ForeignKey fk = new ForeignKey(fkRs.getString(1));
                int localTableId = fkRs.getInt(2);
                int remoteTableId = fkRs.getInt(4);

                fk.setForeignTableName(fkRs.getString(3));
                for (int idx = 0; idx < 16; idx++) {
                    short fkColIdx = fkRs.getShort(5 + idx + idx);
                    short pkColIdx = fkRs.getShort(6 + idx + idx);
                    Reference ref = new Reference();

                    if (fkColIdx == 0) {
                        break;
                    }

                    refObjStmt.setInt(1, localTableId);
                    refObjStmt.setShort(2, fkColIdx);

                    ResultSet colRs = refObjStmt.executeQuery();

                    if (colRs.next()) {
                        ref.setLocalColumnName(colRs.getString(1));
                    }
                    colRs.close();

                    refObjStmt.setInt(1, remoteTableId);
                    refObjStmt.setShort(2, pkColIdx);

                    colRs = refObjStmt.executeQuery();

                    if (colRs.next()) {
                        ref.setForeignColumnName(colRs.getString(1));
                    }
                    colRs.close();

                    fk.addReference(ref);
                }
                result.add(fk);
            }
        } finally {
            closeStatement(colStmt);
            closeStatement(refObjStmt);
        }

        return result;
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) throws SQLException {
        // We can simply check the sys indexes table where a specific flag is set for pk indexes
        final String query = "SELECT name = sysindexes.name FROM sysindexes, sysobjects WHERE sysobjects.name = ? " +
                             "AND sysindexes.name = ? AND sysobjects.id = sysindexes.id AND (sysindexes.status & 2048) > 0";

        PreparedStatement stmt = null;

        try {
            stmt = getConnection().prepareStatement(query);

            stmt.setString(1, table.getName());
            stmt.setString(2, index.getName());

            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } finally {
            closeStatement(stmt);
        }
    }
}
