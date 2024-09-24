package org.apache.ddlutils.platform.sapdb;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.util.ContextMap;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;

import java.sql.SQLException;
import java.sql.Types;

/**
 * Reads a database model from a SapDb database.
 */
public class SapDbModelReader extends JdbcModelReader {
    /**
     * Creates a new model reader for SapDb databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public SapDbModelReader(Platform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
        setDefaultTablePattern("%");
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, ContextMap values) throws SQLException {
        Column column = super.readColumn(metaData, values);

        if (column.getDefaultValue() != null) {
            // SapDb pads the default value with spaces
            column.setDefaultValue(column.getDefaultValue().trim());
            // SapDb uses the default value for the auto-increment specification
            if (column.getDefaultValue().startsWith("DEFAULT SERIAL")) {
                column.setAutoIncrement(true);
                column.setDefaultValue(null);
            }
        }
        if (column.getTypeCode() == Types.DECIMAL) {
            // We also perform back-mapping to BIGINT
            if ((column.getSizeAsInt() == 38) && (column.getScale() == 0)) {
                column.setTypeCode(Types.BIGINT);
            }
        }
        return column;
    }
}
