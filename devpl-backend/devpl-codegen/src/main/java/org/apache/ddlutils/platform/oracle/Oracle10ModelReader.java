package org.apache.ddlutils.platform.oracle;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.util.PojoMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Reads a database model from an Oracle 10 database.
 */
public class Oracle10ModelReader extends Oracle8ModelReader {
    /**
     * Creates a new model reader for Oracle 10 databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public Oracle10ModelReader(Platform platform) {
        super(platform);
    }

    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, PojoMap values) throws SQLException {
        // Oracle 10 added the recycle bin which contains dropped database objects not yet purged
        // Since we don't want entries from the recycle bin, we filter them out
        final String query = "SELECT * FROM RECYCLEBIN WHERE OBJECT_NAME = ?";
        boolean deletedObj = false;
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, values.getString("TABLE_NAME"));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // we found the table in the recycle bin, so it's a deleted one which we ignore
                    deletedObj = true;
                }
            }
        }
        return deletedObj ? null : super.readTable(metaData, values);
    }
}
