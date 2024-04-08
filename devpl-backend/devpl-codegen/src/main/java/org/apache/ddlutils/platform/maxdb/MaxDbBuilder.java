package org.apache.ddlutils.platform.maxdb;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.sapdb.SapDbBuilder;

import java.io.IOException;

/**
 * The SQL Builder for MaxDB.
 */
public class MaxDbBuilder extends SapDbBuilder {
    /**
     * Creates a new builder instance.
     *
     * @param platform The plaftform this builder belongs to
     */
    public MaxDbBuilder(Platform platform) {
        super(platform);
    }

    @Override
    public void createPrimaryKey(Table table, Column[] primaryKeyColumns) throws IOException {
        if ((primaryKeyColumns.length > 0) && shouldGeneratePrimaryKeys(primaryKeyColumns)) {
            print("ALTER TABLE ");
            printlnIdentifier(getTableName(table));
            printIndent();
            print("ADD CONSTRAINT ");
            printIdentifier(getConstraintName(null, table, "PK", null));
            print(" ");
            writePrimaryKeyStmt(table, primaryKeyColumns);
            printEndOfStatement();
        }
    }

    @Override
    public void dropForeignKey(Table table, ForeignKey foreignKey) throws IOException {
        writeTableAlterStmt(table);
        print("DROP CONSTRAINT ");
        printIdentifier(getForeignKeyName(table, foreignKey));
        printEndOfStatement();
    }
}
