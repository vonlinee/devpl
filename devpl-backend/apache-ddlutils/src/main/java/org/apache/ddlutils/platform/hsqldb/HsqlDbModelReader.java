package org.apache.ddlutils.platform.hsqldb;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.util.ContextMap;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Reads a database model from a HsqlDb database.
 */
public class HsqlDbModelReader extends JdbcModelReader {
    /**
     * Creates a new model reader for HsqlDb databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public HsqlDbModelReader(Platform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
    }

    @Override
    protected Collection<Table> readTables(String catalog, String schemaPattern, String[] tableTypes) throws SQLException {
        Collection<Table> tables = super.readTables(catalog, schemaPattern, tableTypes);
        for (Table table : tables) {
            // For at least version 1.7.2 we have to determine the auto-increment columns
            // from a result set metadata because the database does not put this info
            // into the database metadata
            // Since Hsqldb only allows IDENTITY for primary key columns, we restrict
            // our search to those columns
            determineAutoIncrementFromResultSetMetaData(table, table.getPrimaryKeyColumns());
        }
        return tables;
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, ContextMap values) throws SQLException {
        Column column = super.readColumn(metaData, values);

        if (TypeMap.isTextType(column.getTypeCode()) &&
            (column.getDefaultValue() != null)) {
            column.setDefaultValue(unescape(column.getDefaultValue(), "'", "''"));
        }
        return column;
    }

    @Override
    protected boolean isInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk, Index index) {
        String name = index.getName();
        return (name != null) && name.startsWith("SYS_IDX_");
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) {
        String name = index.getName();
        return name != null && (name.startsWith("SYS_PK_") || name.startsWith("SYS_IDX_"));
    }
}
