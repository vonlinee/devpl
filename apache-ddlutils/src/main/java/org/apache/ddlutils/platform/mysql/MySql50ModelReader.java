package org.apache.ddlutils.platform.mysql;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.util.ValueMap;

import java.sql.SQLException;

/**
 * Reads a database model from a MySql 5 database.
 * @version $Revision: $
 */
public class MySql50ModelReader extends MySqlModelReader {
    /**
     * Creates a new model reader for MySql 5 databases.
     * @param platform The platform that this model reader belongs to
     */
    public MySql50ModelReader(DatabasePlatform platform) {
        super(platform);
    }


    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, ValueMap values) throws SQLException {
        Column column = super.readColumn(metaData, values);

        // make sure the defaultvalue is null when an empty is returned.
        if ("".equals(column.getDefaultValue())) {
            column.setDefaultValue(null);
        }
        return column;
    }
}
