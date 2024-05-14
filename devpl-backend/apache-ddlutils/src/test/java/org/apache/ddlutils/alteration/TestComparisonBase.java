package org.apache.ddlutils.alteration;


import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.TestBase;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.TestPlatform;
import org.junit.jupiter.api.Assertions;

import java.sql.Types;

/**
 * Base class for model comparison tests.
 */
public abstract class TestComparisonBase extends TestBase {
    /**
     * Creates a new platform object.
     *
     * @param delimitedIdentifierModeOn Whether delimited identifiers shall be used
     * @return The platform object
     */
    protected Platform getPlatform(boolean delimitedIdentifierModeOn) {
        TestPlatform platform = new TestPlatform() {
            protected TableDefinitionChangesPredicate getTableDefinitionChangesPredicate() {
                return null;
            }
        };

        PlatformInfo platformInfo = platform.getPlatformInfo();

        platform.setDelimitedIdentifierModeOn(delimitedIdentifierModeOn);
        platformInfo.setHasSize(Types.DECIMAL, true);
        platformInfo.setHasSize(Types.NUMERIC, true);
        platformInfo.setHasSize(Types.CHAR, true);
        platformInfo.setHasSize(Types.VARCHAR, true);

        return platform;
    }

    /**
     * Asserts the given table.
     *
     * @param name        The expected name
     * @param description The expected description
     * @param columnCount The expected number of columns
     * @param fkCount     The expected number of foreign keys
     * @param indexCount  The expected number of indexes
     * @param table       The table to assert
     */
    protected void assertTable(String name, String description, int columnCount, int fkCount, int indexCount, Table table) {
        Assertions.assertEquals(name, table.getName());
        Assertions.assertEquals(description, table.getDescription());
        Assertions.assertEquals(columnCount, table.getColumnCount());
        Assertions.assertEquals(fkCount, table.getForeignKeyCount());
        Assertions.assertEquals(indexCount, table.getIndexCount());
    }

    /**
     * Asserts the given column.
     *
     * @param name            The expected name
     * @param typeCode        The expected type code
     * @param sizeSpec        The expected size
     * @param defaultValue    The expected default value
     * @param isPrimaryKey    The expected primary key status
     * @param isRequired      The expected required status
     * @param isAutoIncrement The expected auto increment status
     * @param column          The column to assert
     */
    protected void assertColumn(String name, int typeCode, String sizeSpec, String defaultValue, boolean isPrimaryKey, boolean isRequired, boolean isAutoIncrement, Column column) {
        Assertions.assertEquals(name, column.getName());
        Assertions.assertEquals(typeCode, column.getTypeCode());
        Assertions.assertEquals(sizeSpec, column.getSize());
        Assertions.assertEquals(defaultValue, column.getDefaultValue());
        Assertions.assertEquals(isPrimaryKey, column.isPrimaryKey());
        Assertions.assertEquals(isRequired, column.isRequired());
        Assertions.assertEquals(isAutoIncrement, column.isAutoIncrement());
    }

    /**
     * Asserts the given index.
     *
     * @param name         The expected name
     * @param isUnique     Whether the index is expected to be a unique index
     * @param indexColumns The names of the columns expected to be in the index
     * @param index        The index to assert
     */
    protected void assertIndex(String name, boolean isUnique, String[] indexColumns, Index index) {
        Assertions.assertEquals(name, index.getName());
        Assertions.assertEquals(isUnique, index.isUnique());
        Assertions.assertEquals(indexColumns.length, index.getColumnCount());
        for (int idx = 0; idx < indexColumns.length; idx++) {
            Assertions.assertEquals(indexColumns[idx], index.getColumn(idx).getName());
            Assertions.assertEquals(indexColumns[idx], index.getColumn(idx).getColumn().getName());
        }
    }

    /**
     * Asserts the given foreign key.
     *
     * @param name               The expected name
     * @param targetTableName    The name of the expected target table
     * @param localColumnNames   The names of the expected local columns
     * @param foreignColumnNames The names of the expected foreign columns
     * @param fk                 The foreign key to assert
     */
    protected void assertForeignKey(String name, String targetTableName, String[] localColumnNames, String[] foreignColumnNames, ForeignKey fk) {
        Assertions.assertEquals(name, fk.getName());
        Assertions.assertEquals(targetTableName, fk.getForeignTable().getName());
        Assertions.assertEquals(localColumnNames.length, fk.getReferenceCount());
        for (int idx = 0; idx < localColumnNames.length; idx++) {
            Assertions.assertEquals(localColumnNames[idx], fk.getReference(idx).getLocalColumnName());
            Assertions.assertEquals(foreignColumnNames[idx], fk.getReference(idx).getForeignColumnName());
        }
    }
}
