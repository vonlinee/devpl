package org.apache.ddlutils.platform.axion;


import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Reads a database model from an Axion database.
 */
public class AxionModelReader extends JdbcModelReader {
    /**
     * Creates a new model reader for Axion databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public AxionModelReader(Platform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
        setDefaultTablePattern("%");
    }

    protected Collection readPrimaryKeyNames(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        // Axion still does not support DatabaseMetaData#getPrimaryKeys
        return new ArrayList<>();
    }

    protected Collection readForeignKeys(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        // Axion still does not support DatabaseMetaData#getImportedKeys or #getExportedKeys
        return new ArrayList<>();
    }

    protected void removeSystemIndices(DatabaseMetaDataWrapper metaData, Table table) throws SQLException {
        // Axion's JDBC driver does not support primary key reading, so we have to filter at this level
        for (int indexIdx = 0; indexIdx < table.getIndexCount(); ) {
            Index index = table.getIndex(indexIdx);

            // also, Axion's internal indices are not unique
            if (index.getName().startsWith("SYS_")) {
                table.removeIndex(indexIdx);
            } else {
                indexIdx++;
            }
        }
    }
}
