package org.apache.ddlutils.platform.oracle;


import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;

import java.io.IOException;

/**
 * The SQL builder for Oracle 10.
 */
public class Oracle10Builder extends Oracle8Builder {
    /**
     * Creates a new builder instance.
     *
     * @param platform The platform this builder belongs to
     */
    public Oracle10Builder(Platform platform) {
        super(platform);
    }

    @Override
    public void dropTable(Table table) throws IOException {
        // The only difference to the Oracle 8/9 variant is the purge which prevents the
        // table from being moved to the recycle bin (which is new in Oracle 10)
        Column[] columns = table.getAutoIncrementColumns();

        for (Column column : columns) {
            dropAutoIncrementTrigger(table, column);
            dropAutoIncrementSequence(table, column);
        }

        print("DROP TABLE ");
        printIdentifier(getTableName(table));
        print(" CASCADE CONSTRAINTS PURGE");
        printEndOfStatement();
    }
}
