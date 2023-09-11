package org.apache.ddlutils.model;

/**
 * Helper class that provides cloning of model elements.
 */
public final class CloneHelper {

    private CloneHelper() {
    }

    /**
     * Returns a deep clone of the given model object, including all tables, foreign keys, indexes etc.
     * @param source The source model
     * @return The clone
     */
    public static Database clone(Database source) {
        Database result = new Database();

        result.setName(source.getName());
        result.setIdMethod(source.getIdMethod());
        result.setVersion(source.getVersion());

        for (int tableIdx = 0; tableIdx < source.getTableCount(); tableIdx++) {
            Table sourceTable = source.getTable(tableIdx);
            result.addTable(clone(sourceTable, true, false, result, true));
        }
        for (int tableIdx = 0; tableIdx < source.getTableCount(); tableIdx++) {
            Table sourceTable = source.getTable(tableIdx);
            Table clonedTable = result.getTable(tableIdx);

            for (int fkIdx = 0; fkIdx < sourceTable.getForeignKeyCount(); fkIdx++) {
                ForeignKey sourceFk = sourceTable.getForeignKey(fkIdx);

                clonedTable.addForeignKey(clone(sourceFk, clonedTable, result, true));
            }
        }
        return result;
    }

    /**
     * Returns a clone of the given table.
     * @param source           The source table
     * @param cloneIndexes     Whether to clone indexes; if <code>false</code> then the clone will
     *                         not have any indexes
     * @param cloneForeignKeys Whether to clone foreign kets; if <code>false</code> then the clone
     *                         will not have any foreign keys
     * @param targetModel      The target model, can be <code>null</code> if
     *                         <code>cloneForeignKeys=false</code>
     * @param caseSensitive    Whether comparison is case-sensitive (for cloning foreign keys)
     * @return The clone
     */
    public static Table clone(Table source, boolean cloneIndexes, boolean cloneForeignKeys, Database targetModel, boolean caseSensitive) {
        Table result = new Table();

        result.setCatalog(source.getCatalog());
        result.setSchema(source.getSchema());
        result.setName(source.getName());
        result.setType(source.getType());

        for (int colIdx = 0; colIdx < source.getColumnCount(); colIdx++) {
            result.addColumn(clone(source.getColumn(colIdx), true));
        }
        if (cloneIndexes) {
            for (int indexIdx = 0; indexIdx < source.getIndexCount(); indexIdx++) {
                result.addIndex(clone(source.getIndex(indexIdx), result, true));
            }
        }
        if (cloneForeignKeys) {
            for (int fkIdx = 0; fkIdx < source.getForeignKeyCount(); fkIdx++) {
                result.addForeignKey(clone(source.getForeignKey(fkIdx), result, targetModel, caseSensitive));
            }
        }

        return result;
    }

    /**
     * Returns a clone of the given source column.
     * @param source                The source column
     * @param clonePrimaryKeyStatus Whether to clone the column's primary key status; if <code>false</code>
     *                              then the clone will not be a primary key column
     * @return The clone
     */
    public static Column clone(Column source, boolean clonePrimaryKeyStatus) {
        Column result = new Column();

        result.setName(source.getName());
        result.setJavaName(source.getJavaName());
        result.setPrimaryKey(clonePrimaryKeyStatus && source.isPrimaryKey());
        result.setRequired(source.isRequired());
        result.setAutoIncrement(source.isAutoIncrement());
        result.setJdbcTypeCode(source.getJdbcTypeCode());
        result.setSize(source.getSize());
        result.setDefaultValue(source.getDefaultValue());

        return result;
    }

    /**
     * Returns a clone of the given source index.
     * @param source        The source index
     * @param targetTable   The table whose columns shall be used by the clone
     * @param caseSensitive Whether comparison is case-sensitive (for finding the columns
     *                      in the target table)
     * @return The clone
     */
    public static Index clone(Index source, Table targetTable, boolean caseSensitive) {
        Index result = (source.isUnique() ? new UniqueIndex() : new NonUniqueIndex());

        result.setName(source.getName());
        for (int colIdx = 0; colIdx < source.getColumnCount(); colIdx++) {
            IndexColumn column = source.getColumn(colIdx);

            result.addColumn(clone(column, targetTable, caseSensitive));
        }
        return result;
    }

    /**
     * Returns a clone of the given index column.
     * @param source        The source index column
     * @param targetTable   The table containing the column to be used by the clone
     * @param caseSensitive Whether comparison is case-sensitive (for finding the columns
     *                      in the target table)
     * @return The clone
     */
    public static IndexColumn clone(IndexColumn source, Table targetTable, boolean caseSensitive) {
        IndexColumn result = new IndexColumn();

        result.setColumn(targetTable.findColumn(source.getName(), caseSensitive));
        result.setOrdinalPosition(source.getOrdinalPosition());
        result.setSize(source.getSize());
        return result;
    }

    /**
     * Returns a clone of the given source foreign key.
     * @param source        The source foreign key
     * @param owningTable   The table owning the source foreign key
     * @param targetModel   The target model containing the tables that the clone shall link
     * @param caseSensitive Whether comparison is case-sensitive (for finding the columns
     *                      in the target model)
     * @return The clone
     */
    public static ForeignKey clone(ForeignKey source, Table owningTable, Database targetModel, boolean caseSensitive) {
        ForeignKey result = new ForeignKey();
        Table foreignTable = targetModel.findTable(source.getForeignTableName(), caseSensitive);

        result.setName(source.getName());
        result.setForeignTable(foreignTable);
        result.setAutoIndexPresent(source.isAutoIndexPresent());
        result.setOnDelete(source.getOnDelete());
        result.setOnUpdate(source.getOnUpdate());

        for (int refIdx = 0; refIdx < source.getReferenceCount(); refIdx++) {
            Reference ref = source.getReference(refIdx);

            result.addReference(clone(ref, owningTable, foreignTable, caseSensitive));
        }

        return result;
    }

    /**
     * Returns a clone of the given source reference.
     * @param source        The source reference
     * @param localTable    The table containing the local column to be used by the reference
     * @param foreignTable  The table containing the foreign column to be used by the reference
     * @param caseSensitive Whether comparison is case-sensitive (for finding the columns
     *                      in the tables)
     * @return The clone
     */
    public static Reference clone(Reference source, Table localTable, Table foreignTable, boolean caseSensitive) {
        Reference result = new Reference();

        result.setLocalColumn(localTable.findColumn(source.getLocalColumnName(), caseSensitive));
        result.setForeignColumn(foreignTable.findColumn(source.getForeignColumnName(), caseSensitive));
        return result;
    }
}