package org.apache.ddlutils.platform.mysql;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.util.ObjectMap;

import java.io.IOException;

/**
 * The SQL Builder for MySQL version 5 and above.
 */
public class MySql50Builder extends MySqlBuilder {
    /**
     * Creates a new builder instance.
     * @param platform The plaftform this builder belongs to
     */
    public MySql50Builder(DatabasePlatform platform) {
        super(platform);
    }

    @Override
    protected void copyData(Table sourceTable, Table targetTable) throws IOException {
        print("SET sql_mode=''");
        printEndOfStatement();
        super.copyData(sourceTable, targetTable);
    }

    @Override
    protected String getSqlType(Column column, String nativeType) {
        return super.getSqlType(column, nativeType);
    }

    @Override
    protected void beforeTableCreationStmtEnding(Table table, ObjectMap parameters) {

    }
}
