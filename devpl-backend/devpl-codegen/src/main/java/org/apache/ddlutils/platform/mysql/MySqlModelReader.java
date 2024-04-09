package org.apache.ddlutils.platform.mysql;

import io.devpl.codegen.jdbc.meta.DatabaseMetadataReader;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;

/**
 * Reads a database model from a MySql database.
 */
public class MySqlModelReader extends JdbcModelReader {
    /**
     * Creates a new model reader for MySql databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public MySqlModelReader(Platform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
        setDefaultTablePattern(null);
    }

    /**
     * TODO This needs some more work, since table names can be case-sensitive or lowercase depending on the platform (really cute).
     * See <a href="http://dev.mysql.com/doc/refman/4.1/en/name-case-sensitivity.html">...</a> for more info.
     *
     * @param catalog       The catalog to access in the database; use <code>null</code> for the default value
     *                      目录名称，一般都为空.
     * @param schemaPattern The schema(s) to access in the database; use <code>null</code> for the default value
     *                      schema:数据库名，对于oracle来说就用户名
     * @param tableTypes    The table types to process; use <code>null</code> or an empty list for the default ones
     * @return tables
     * @throws SQLException error
     */
    @Override
    protected Collection<Table> readTables(String catalog, String schemaPattern, String[] tableTypes) throws SQLException {
        Collection<Table> tables = super.readTables(catalog, schemaPattern, tableTypes);
        for (Table table : tables) {
            determineAutoIncrementFromResultSetMetaData(table, table.getPrimaryKeyColumns());
        }
        return tables;
    }

    @Override
    protected Collection<Column> readColumns(DatabaseMetadataReader reader, String catalog, String schema, String tableName) throws SQLException {
        Collection<Column> columns = super.readColumns(reader, catalog, schema, tableName);
        for (Column column : columns) {

            // MySQL converts illegal date/time/timestamp values to "0000-00-00 00:00:00", but this
            // is an illegal ISO value, so we replace it with NULL
            if ((column.getTypeCode() == Types.TIMESTAMP) && "0000-00-00 00:00:00".equals(column.getDefaultValue())) {
                column.setDefaultValue(null);
            }
        }
        return columns;
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) {
        // MySql defines a unique index "PRIMARY" for primary keys
        return "PRIMARY".equals(index.getName());
    }

    @Override
    protected boolean isInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk, Index index) {
        // MySql defines a non-unique index of the same name as the fk
        return getPlatform().getSqlBuilder().getForeignKeyName(table, fk).equals(index.getName());
    }
}
