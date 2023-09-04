package org.apache.ddlutils.platform.hsqldb;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.util.ValueMap;

import java.sql.SQLException;

/**
 * Reads a database model from a HsqlDb database.
 * @version $Revision: $
 */
public class HsqlDbModelReader extends JdbcModelReader {
    /**
     * Creates a new model reader for HsqlDb databases.
     * @param platform The platform that this model reader belongs to
     */
    public HsqlDbModelReader(DatabasePlatform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
    }


    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, ValueMap values) throws SQLException {
        Table table = super.readTable(metaData, values);

        if (table != null) {
            // For at least version 1.7.2 we have to determine the auto-increment columns
            // from a result set metadata because the database does not put this info
            // into the database metadata
            // Since Hsqldb only allows IDENTITY for primary key columns, we restrict
            // our search to those columns
            determineAutoIncrementFromResultSetMetaData(table, table.getPrimaryKeyColumns());
        }

        return table;
    }


    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, ValueMap values) throws SQLException {
        Column column = super.readColumn(metaData, values);

        if (TypeMap.isTextType(column.getJdbcTypeCode()) &&
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

        return (name != null) && (name.startsWith("SYS_PK_") || name.startsWith("SYS_IDX_"));
    }
}
