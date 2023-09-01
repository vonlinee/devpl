package org.apache.ddlutils.platform.db2;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;

import java.io.IOException;

/**
 * The SQL Builder for DB2 v8 and above.
 * @version $Revision: $
 */
public class Db2v8Builder extends Db2Builder {
    /**
     * Creates a new builder instance.
     * @param platform The plaftform this builder belongs to
     */
    public Db2v8Builder(DatabasePlatform platform) {
        super(platform);
    }

    /**
     * Generates the SQL to drop a column from a table.
     * @param table  The table where to drop the column from
     * @param column The column to drop
     */
    public void dropColumn(Table table, Column column) throws IOException {
        super.dropColumn(table, column);
        print("CALL ADMIN_CMD('REORG TABLE ");
        printIdentifier(getTableName(table));
        print("')");
        printEndOfStatement();
    }
}
