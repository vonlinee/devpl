package org.apache.ddlutils.model;

import org.apache.ddlutils.DatabaseObjectRelationMappingException;
import org.apache.ddlutils.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Types;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Represents the database model, i.e. the tables in the database. It also
 * contains the corresponding dyna classes for creating dyna beans for the
 * objects stored in the tables.
 */
public class Database extends SchemaObject implements Serializable {
    /**
     * Unique ID for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = -3160443396757573868L;
    /**
     * The tables.
     */
    private final List<Table> _tables = new ArrayList<>();

    /**
     * The method for generating primary keys (currently ignored).
     */
    private String _idMethod;
    /**
     * The version of the model.
     */
    private String _version;
    /**
     * The dyna class cache for this model.
     */
    private transient TableModelCache _tableModelCache = null;

    /**
     * Creates an empty model without a name.
     */
    public Database() {
    }

    /**
     * Creates an empty model with the given name.
     *
     * @param name The name
     */
    public Database(String name) {
        setName(name);
    }

    /**
     * Adds all tables from the other database to this database.
     * Note that the other database is not changed.
     *
     * @param otherDb The other database model
     */
    public void mergeWith(Database otherDb) throws ModelException {
        DefaultModelCopier modelCopier = new DefaultModelCopier();
        for (int tableIdx = 0; tableIdx < otherDb.getTableCount(); tableIdx++) {
            Table table = otherDb.getTable(tableIdx);
            if (findTable(table.getName()) != null) {
                // TODO: It might make more sense to log a warning and overwrite the table (or merge them) ?
                throw new ModelException("Cannot merge the models because table " + table.getName() + " already defined in this model");
            } else {
                addTable(modelCopier.copy(table, true, false, this, true));
            }
        }
        for (int tableIdx = 0; tableIdx < otherDb.getTableCount(); tableIdx++) {
            Table otherTable = otherDb.getTable(tableIdx);
            Table localTable = findTable(otherTable.getName());
            for (int fkIdx = 0; fkIdx < otherTable.getForeignKeyCount(); fkIdx++) {
                ForeignKey fk = otherTable.getForeignKey(fkIdx);
                localTable.addForeignKey(modelCopier.copy(fk, localTable, this, false));
            }
        }
    }

    /**
     * Returns the version of this database model.
     *
     * @return The version
     */
    public String getVersion() {
        return _version;
    }

    /**
     * Sets the version of this database model.
     *
     * @param version The version
     */
    public void setVersion(String version) {
        _version = version;
    }

    /**
     * Returns the method for generating primary key values.
     *
     * @return The method
     */
    public String getIdMethod() {
        return _idMethod;
    }

    /**
     * Sets the method for generating primary key values. Note that this
     * value is ignored by DdlUtils and only for compatibility with Torque.
     *
     * @param idMethod The method
     */
    public void setIdMethod(String idMethod) {
        _idMethod = idMethod;
    }

    /**
     * Returns the number of tables in this model.
     *
     * @return The number of tables
     */
    public int getTableCount() {
        return _tables.size();
    }

    /**
     * Returns the tables in this model.
     *
     * @return The tables
     */
    public Table[] getTables() {
        return _tables.toArray(new Table[0]);
    }

    /**
     * Returns the table at the specified position.
     *
     * @param idx The index of the table
     * @return The table
     */
    public Table getTable(int idx) {
        return _tables.get(idx);
    }

    /**
     * Adds a table.
     *
     * @param table The table to add
     */
    public void addTable(Table table) {
        if (table != null) {
            _tables.add(table);
        }
    }

    /**
     * Adds a table at the specified position.
     *
     * @param idx   The index where to insert the table
     * @param table The table to add
     */
    public void addTable(int idx, Table table) {
        if (table != null) {
            _tables.add(idx, table);
        }
    }

    /**
     * Adds the given tables.
     *
     * @param tables The tables to add
     */
    public void addTables(Collection<Table> tables) {
        _tables.addAll(tables);
    }

    /**
     * Removes the given table. This method does not check whether there are foreign keys to the table.
     *
     * @param table The table to remove
     */
    public void removeTable(Table table) {
        if (table != null) {
            _tables.remove(table);
        }
    }

    /**
     * Removes the indicated table. This method does not check whether there are foreign keys to the table.
     *
     * @param idx The index of the table to remove
     */
    public void removeTable(int idx) {
        _tables.remove(idx);
    }

    /**
     * Removes the given tables. This method does not check whether there are foreign keys to the tables.
     *
     * @param tables The tables to remove
     */
    public void removeTables(Table[] tables) {
        _tables.removeAll(Arrays.asList(tables));
    }

    /**
     * Removes all but the given tables. This method does not check whether there are foreign keys to the
     * removed tables.
     *
     * @param tables The tables to keep
     */
    public void removeAllTablesExcept(Table[] tables) {
        ArrayList<Table> allTables = new ArrayList<>(_tables);
        allTables.removeAll(Arrays.asList(tables));
        _tables.removeAll(allTables);
    }

    // Helper methods

    /**
     * Initializes the model by establishing the relationships between elements in this model encoded
     * e.g. in foreign keys etc. Also checks that the model elements are valid (table and columns have
     * a name, foreign keys reference existing tables etc.)
     */
    public void initialize() throws ModelException {
        // we have to set up
        // * target tables in foreign keys
        // * columns in foreign key references
        // * columns in indices
        // * columns in uniques
        Set<String> namesOfProcessedTables = new HashSet<>();
        Set<String> namesOfProcessedColumns = new HashSet<>();
        Set<String> namesOfProcessedFks = new HashSet<>();
        Set<String> namesOfProcessedIndices = new HashSet<>();
        int tableIdx = 0;

        if (!hasName()) {
            throw new ModelException("The database model has no name");
        }

        for (Iterator<Table> tableIt = _tables.iterator(); tableIt.hasNext(); tableIdx++) {
            Table curTable = tableIt.next();

            if ((curTable.getName() == null) || (curTable.getName().isEmpty())) {
                throw new ModelException("The table nr. " + tableIdx + " has no name");
            }
            if (namesOfProcessedTables.contains(curTable.getName())) {
                throw new ModelException("There are multiple tables with the name " + curTable.getName());
            }
            namesOfProcessedTables.add(curTable.getName());

            namesOfProcessedColumns.clear();
            namesOfProcessedFks.clear();
            namesOfProcessedIndices.clear();

            for (int idx = 0; idx < curTable.getColumnCount(); idx++) {
                Column column = curTable.getColumn(idx);

                if ((column.getName() == null) || (column.getName().isEmpty())) {
                    throw new ModelException("The column nr. " + idx + " in table " + curTable.getName() + " has no name");
                }
                if (namesOfProcessedColumns.contains(column.getName())) {
                    throw new ModelException("There are multiple columns with the name " + column.getName() + " in the table " + curTable.getName());
                }
                namesOfProcessedColumns.add(column.getName());

                if ((column.getType() == null) || (column.getType().isEmpty())) {
                    throw new ModelException("The column nr. " + idx + " in table " + curTable.getName() + " has no type");
                }
                if ((column.getTypeCode() == Types.OTHER) && !"OTHER".equalsIgnoreCase(column.getType())) {
                    throw new ModelException("The column nr. " + idx + " in table " + curTable.getName() + " has an unknown type " + column.getType());
                }
                namesOfProcessedColumns.add(column.getName());
            }

            for (int idx = 0; idx < curTable.getForeignKeyCount(); idx++) {
                ForeignKey fk = curTable.getForeignKey(idx);
                String fkName = (fk.getName() == null ? "" : fk.getName());
                String fkDesc = (fkName.isEmpty() ? "nr. " + idx : fkName);

                if (!fkName.isEmpty()) {
                    if (namesOfProcessedFks.contains(fkName)) {
                        throw new ModelException("There are multiple foreign keys in table " + curTable.getName() + " with the name " + fkName);
                    }
                    namesOfProcessedFks.add(fkName);
                }

                if (fk.getForeignTable() == null) {
                    Table targetTable = findTable(fk.getForeignTableName(), true);

                    if (targetTable == null) {
                        throw new ModelException("The foreign key " + fkDesc + " in table " + curTable.getName() + " references the undefined table " + fk.getForeignTableName());
                    } else {
                        fk.setForeignTable(targetTable);
                    }
                }
                if (fk.getReferenceCount() == 0) {
                    throw new ModelException("The foreign key " + fkDesc + " in table " + curTable.getName() + " does not have any references");
                }
                for (int refIdx = 0; refIdx < fk.getReferenceCount(); refIdx++) {
                    Reference ref = fk.getReference(refIdx);

                    if (ref.getLocalColumn() == null) {
                        Column localColumn = curTable.findColumn(ref.getLocalColumnName(), true);

                        if (localColumn == null) {
                            throw new ModelException("The foreign key " + fkDesc + " in table " + curTable.getName() + " references the undefined local column " + ref.getLocalColumnName());
                        } else {
                            ref.setLocalColumn(localColumn);
                        }
                    }
                    if (ref.getForeignColumn() == null) {
                        Column foreignColumn = fk.getForeignTable().findColumn(ref.getForeignColumnName(), true);

                        if (foreignColumn == null) {
                            throw new ModelException("The foreign key " + fkDesc + " in table " + curTable.getName() + " references the undefined local column " + ref.getForeignColumnName() + " in table " + fk.getForeignTable().getName());
                        } else {
                            ref.setForeignColumn(foreignColumn);
                        }
                    }
                }
            }

            for (int idx = 0; idx < curTable.getIndexCount(); idx++) {
                Index index = curTable.getIndex(idx);
                String indexName = (index.getName() == null ? "" : index.getName());
                String indexDesc = (indexName.isEmpty() ? "nr. " + idx : indexName);

                if (!indexName.isEmpty()) {
                    if (namesOfProcessedIndices.contains(indexName)) {
                        throw new ModelException("There are multiple indices in table " + curTable.getName() + " with the name " + indexName);
                    }
                    namesOfProcessedIndices.add(indexName);
                }
                if (index.getColumnCount() == 0) {
                    throw new ModelException("The index " + indexDesc + " in table " + curTable.getName() + " does not have any columns");
                }

                for (int indexColumnIdx = 0; indexColumnIdx < index.getColumnCount(); indexColumnIdx++) {
                    IndexColumn indexColumn = index.getColumn(indexColumnIdx);
                    Column column = curTable.findColumn(indexColumn.getName(), true);

                    if (column == null) {
                        throw new ModelException("The index " + indexDesc + " in table " + curTable.getName() + " references the undefined column " + indexColumn.getName());
                    } else {
                        indexColumn.setColumn(column);
                    }
                }
            }
        }
    }

    /**
     * Finds the table with the specified name, using case-insensitive matching.
     * Note that this method is not called getTable to avoid introspection
     * problems.
     *
     * @param name The name of the table to find
     * @return The table or <code>null</code> if there is no such table
     */
    public Table findTable(String name) {
        return findTable(name, false);
    }

    /**
     * Finds the table with the specified name, using case-insensitive matching.
     * Note that this method is not called getTable to avoid introspection
     * problems.
     *
     * @param name          The name of the table to find
     * @param caseSensitive Whether case matters for the names
     * @return The table or <code>null</code> if there is no such table
     */
    public Table findTable(String name, boolean caseSensitive) {
        for (Table table : _tables) {
            if (StringUtils.equals(table.getName(), name, caseSensitive)) {
                return table;
            }
        }
        return null;
    }

    /**
     * Returns the indicated tables.
     *
     * @param tableNames    The names of the tables
     * @param caseSensitive Whether the case of the table names matters
     * @return The tables
     */
    public Table[] findTables(String[] tableNames, boolean caseSensitive) {
        List<Table> tables = new ArrayList<>();
        if (tableNames != null) {
            for (String tableName : tableNames) {
                Table table = findTable(tableName, caseSensitive);
                if (table != null) {
                    tables.add(table);
                }
            }
        }
        return tables.toArray(new Table[0]);
    }

    /**
     * Finds the tables whose names match the given regular expression.
     *
     * @param tableNameRegExp The table name regular expression
     * @param caseSensitive   Whether the case of the table names matters; if not, then the regular expression should
     *                        assume that the table names are all-uppercase
     * @return The tables
     * @throws PatternSyntaxException If the regular expression is invalid
     */
    public Table[] findTables(String tableNameRegExp, boolean caseSensitive) throws PatternSyntaxException {
        List<Table> tables = new ArrayList<>();

        if (tableNameRegExp != null) {
            Pattern pattern = Pattern.compile(tableNameRegExp);
            for (Table table : _tables) {
                if (table.hasName()) {
                    String tableName = table.getName();
                    if (!caseSensitive) {
                        tableName = tableName.toUpperCase();
                    }
                    if (pattern.matcher(tableName).matches()) {
                        tables.add(table);
                    }
                }
            }
        }
        return tables.toArray(new Table[0]);
    }

    /**
     * Returns the dyna class cache. If none is available yet, a new one will be created.
     *
     * @return The dyna class cache
     */
    private TableModelCache getClassCache() {
        if (_tableModelCache == null) {
            _tableModelCache = new TableModelCache();
        }
        return _tableModelCache;
    }

    /**
     * Resets the dyna class cache. This should be done for instance when a column
     * has been added or removed to a table.
     */
    public void resetClassCache() {
        _tableModelCache = null;
    }

    /**
     * Returns the {@link TableModel} for the given table name. If it does not
     * exist yet, a new one will be created based on the Table definition.
     *
     * @param tableName The name of the table to create the bean for
     * @return The <code>SqlDynaClass</code> for the indicated table or <code>null</code>
     * if the model contains no such table
     */
    public TableModel getClassForTable(String tableName) {
        Table table = findTable(tableName);
        return table != null ? getClassCache().getDynaClass(table) : null;
    }

    /**
     * Returns the {@link TableModel} for the given dyna bean.
     *
     * @param bean The dyna bean
     * @return The <code>SqlDynaClass</code> for the given bean
     */
    public TableModel getClassForTable(TableRow bean) {
        return getClassCache().getDynaClass(bean);
    }

    /**
     * Creates a new dyna bean for the given table.
     *
     * @param table The table to create the bean for
     * @return The new dyna bean
     */
    public TableRow createObjectForTable(Table table) throws DatabaseObjectRelationMappingException {
        return getClassCache().createObject(table);
    }

    /**
     * Convenience method that combines {@link #createObjectForTable(Table)} and
     * {@link #findTable(String, boolean)}.
     *
     * @param tableName     The name of the table to create the bean for
     * @param caseSensitive Whether case matters for the names
     * @return The new dyna bean
     */
    public TableRow createObjectForTable(String tableName, boolean caseSensitive) throws DatabaseObjectRelationMappingException {
        return getClassCache().createObject(findTable(tableName, caseSensitive));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Database other) {
            // Note that this compares case-sensitive
            if (!Objects.equals(getName(), other.getName())) {
                return false;
            }
            if (getTableCount() != other.getTableCount()) {
                return false;
            }
            for (int i = 0; i < getTableCount(); i++) {
                if (!Objects.equals(getTable(i), other.getTable(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), _tables);
    }

    @Override
    public String toString() {
        return "Database [name=" + getName() + "; " + getTableCount() + " tables]";
    }

    /**
     * Returns a verbose string representation of this database.
     *
     * @return The string representation
     */
    public String toVerboseString() {
        StringBuilder result = new StringBuilder();
        result.append("Database [");
        result.append(getName());
        result.append("] tables:");
        for (int idx = 0; idx < getTableCount(); idx++) {
            result.append(" ");
            result.append(getTable(idx).toVerboseString());
        }
        return result.toString();
    }
}
