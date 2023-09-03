package org.apache.ddlutils.platform.mysql;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.util.ValueMap;

import java.sql.SQLException;
import java.sql.Types;

/**
 * Reads a database model from a MySql database.
 */
public class MySqlModelReader extends JdbcModelReader {
    /**
     * Creates a new model reader for MySql databases.
     * @param platform The platform that this model reader belongs to
     */
    public MySqlModelReader(DatabasePlatform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
        setDefaultTablePattern(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, ValueMap values) throws SQLException {
        // TODO This needs some more work, since table names can be case-sensitive or lowercase
        //      depending on the platform (really cute).
        //      See http://dev.mysql.com/doc/refman/4.1/en/name-case-sensitivity.html for more info.

        Table table = super.readTable(metaData, values);

        if (table != null) {
            determineAutoIncrementFromResultSetMetaData(table, table.getPrimaryKeyColumns());
        }
        return table;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, ValueMap values) throws SQLException {
        Column column = super.readColumn(metaData, values);

        // MySQL converts illegal date/time/timestamp values to "0000-00-00 00:00:00", but this
        // is an illegal ISO value, so we replace it with NULL
        if ((column.getJdbcTypeCode() == Types.TIMESTAMP) && "0000-00-00 00:00:00".equals(column.getDefaultValue())) {
            column.setDefaultValue(null);
        }
        return column;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) {
        // MySql defines a unique index "PRIMARY" for primary keys
        return "PRIMARY".equals(index.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk, Index index) {
        // MySql defines a non-unique index of the same name as the fk
        return getPlatform().getSqlBuilder().getForeignKeyName(table, fk).equals(index.getName());
    }
}
