package org.apache.ddlutils.platform.sapdb;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.alteration.ColumnDefinitionChange;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.util.StringUtils;

import java.io.IOException;

/**
 * The SQL Builder for SapDB.
 * @version $Revision$
 */
public class SapDbBuilder extends SqlBuilder {
    /**
     * Creates a new builder instance.
     * @param platform The plaftform this builder belongs to
     */
    public SapDbBuilder(DatabasePlatform platform) {
        super(platform);
        addEscapedCharSequence("'", "''");
    }


    @Override
    public void dropTable(Table table) throws IOException {
        print("DROP TABLE ");
        printIdentifier(getTableName(table));
        print(" CASCADE");
        printEndOfStatement();
    }


    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
        print("DEFAULT SERIAL(1)");
    }


    @Override
    public void createPrimaryKey(Table table, Column[] primaryKeyColumns) throws IOException {
        // Note that SapDB does not support the addition of named primary keys
        if ((primaryKeyColumns.length > 0) && shouldGeneratePrimaryKeys(primaryKeyColumns)) {
            print("ALTER TABLE ");
            printlnIdentifier(getTableName(table));
            printIndent();
            print("ADD ");
            writePrimaryKeyStmt(table, primaryKeyColumns);
            printEndOfStatement();
        }
    }


    @Override
    protected void writeForeignKeyOnDeleteAction(Table table, ForeignKey foreignKey) throws IOException {
        if (foreignKey.getOnDelete() != CascadeActionEnum.NONE) {
            super.writeForeignKeyOnDeleteAction(table, foreignKey);
        }
    }


    @Override
    public void dropForeignKey(Table table, ForeignKey foreignKey) throws IOException {
        writeTableAlterStmt(table);
        print("DROP FOREIGN KEY ");
        printIdentifier(getForeignKeyName(table, foreignKey));
        printEndOfStatement();
    }


    @Override
    public String getSelectLastIdentityValues(Table table) {
        return "SELECT " +
            getDelimitedIdentifier(getTableName(table)) +
            ".CURRVAL FROM DUAL";
    }


    @Override
    public void addColumn(Database model, Table table, Column newColumn) throws IOException {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("ADD ");
        writeColumn(table, newColumn);
        printEndOfStatement();
    }

    /**
     * Writes the SQL to drop a column.
     * @param table  The table
     * @param column The column to drop
     */
    public void dropColumn(Table table, Column column) throws IOException {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("DROP ");
        printIdentifier(getColumnName(column));
        print(" RELEASE SPACE");
        printEndOfStatement();
    }

    /**
     * Writes the SQL to drop the primary key of the given table.
     * @param table The table
     */
    public void dropPrimaryKey(Table table) throws IOException {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("DROP PRIMARY KEY");
        printEndOfStatement();
    }

    /**
     * Writes the SQL to set the required status of the given column.
     * @param table      The table
     * @param column     The column to change
     * @param isRequired Whether the column shall be required
     */
    public void changeColumnRequiredStatus(Table table, Column column, boolean isRequired) throws IOException {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("COLUMN ");
        printIdentifier(getColumnName(column));
        if (isRequired) {
            print(" NOT NULL");
        } else {
            print(" DEFAULT NULL");
        }
        printEndOfStatement();
    }

    /**
     * Writes the SQL to set the default value of the given column.
     * @param table           The table
     * @param column          The column to change
     * @param newDefaultValue The new default value
     */
    public void changeColumnDefaultValue(Table table, Column column, String newDefaultValue) throws IOException {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("COLUMN ");
        printIdentifier(getColumnName(column));

        boolean hasDefault = column.getParsedDefaultValue() != null;

        if (isValidDefaultValue(newDefaultValue, column.getJdbcTypeCode())) {
            if (hasDefault) {
                print(" ALTER DEFAULT ");
            } else {
                print(" ADD DEFAULT ");
            }
            printDefaultValue(newDefaultValue, column.getJdbcTypeCode());
        } else if (hasDefault) {
            print(" DROP DEFAULT");
        }
        printEndOfStatement();
    }


    @Override
    protected void writeCastExpression(Column sourceColumn, Column targetColumn) throws IOException {
        boolean charSizeChanged = TypeMap.isTextType(targetColumn.getJdbcTypeCode()) &&
            TypeMap.isTextType(targetColumn.getJdbcTypeCode()) &&
            ColumnDefinitionChange.isSizeChanged(getPlatformInfo(), sourceColumn, targetColumn) &&
            !StringUtils.isEmpty(targetColumn.getSize());

        if (charSizeChanged) {
            print("SUBSTR(");
        }
        printIdentifier(getColumnName(sourceColumn));
        if (charSizeChanged) {
            print(",1,");
            print(targetColumn.getSize());
            print(")");
        }
    }
}
