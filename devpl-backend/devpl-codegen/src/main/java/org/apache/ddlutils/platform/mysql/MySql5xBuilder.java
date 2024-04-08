package org.apache.ddlutils.platform.mysql;


import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.util.ContextMap;

import java.io.IOException;

/**
 * The SQL Builder for MySQL version 5 and above.
 */
public class MySql5xBuilder extends MySqlBuilder {
    /**
     * Creates a new builder instance.
     *
     * @param platform The platform this builder belongs to
     */
    public MySql5xBuilder(Platform platform) {
        super(platform);
    }

    @Override
    protected void copyData(Table sourceTable, Table targetTable) throws IOException {
        print("SET sql_mode=''");
        printEndOfStatement();
        super.copyData(sourceTable, targetTable);
    }

    @Override
    protected void writeTableCreationStmtEnding(Table table, ContextMap parameters) throws IOException {
        print(" Engine = InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT= '" + table.getDescription() + "'");
    }

    @Override
    protected void writeTableComment(Table table) throws IOException {
        super.writeTableComment(table);
    }

    @Override
    protected void writeColumnDefStmtEnding(Table table, Column column, int columnIndex, ContextMap contextMap) throws IOException {
        print(" COMMENT '");
        print(column.getDescription());
        print("' ");

        super.writeColumnDefStmtEnding(table, column, columnIndex, contextMap);
    }
}
