package org.apache.ddlutils.platform;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.util.JdbcUtils;
import org.apache.ddlutils.util.ObjectMap;
import org.apache.ddlutils.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.Collator;
import java.util.*;

/**
 * An utility class to create a Database model from a live database.
 */
public class JdbcModelReader {
    /**
     * The Log to which logging calls will be made.
     */
    private final Logger _log = LoggerFactory.getLogger(getClass());

    /**
     * The descriptors for the relevant columns in the table metadata.
     */
    private final List<MetaDataColumnDescriptor> columnsForTable;
    /**
     * The descriptors for the relevant columns in the table column metadata.
     */
    private final List<MetaDataColumnDescriptor> columnsForColumn;
    /**
     * The descriptors for the relevant columns in the primary key metadata.
     */
    private final List<MetaDataColumnDescriptor> columnsForPK;
    /**
     * The descriptors for the relevant columns in the foreign key metadata.
     */
    private final List<MetaDataColumnDescriptor> columnsForFK;
    /**
     * The descriptors for the relevant columns in the index metadata.
     */
    private final List<MetaDataColumnDescriptor> columnsForIndex;
    /**
     * The platform that this model reader belongs to.
     */
    private final DatabasePlatform platform;
    /**
     * Contains default column sizes (minimum sizes that a JDBC-compliant db must support).
     */
    private final HashMap<Integer, String> defaultSizes = new HashMap<>();
    /**
     * The default database catalog to read.
     */
    private String defaultCatalogPattern = "%";
    /**
     * The default database schema(s) to read. % indicates all schemas
     */
    private String defaultSchemaPattern = "%";
    /**
     * The default pattern for reading all tables.
     */
    private String defaultTablePattern = "%";
    /**
     * The default pattern for reading all columns.
     */
    private String defaultColumnPattern;
    /**
     * The table types to recognize per default.
     */
    private String[] defaultTableTypes = {"TABLE"};
    /**
     * The active connection while reading a database model.
     */
    private Connection connection;

    /**
     * Creates a new model reader instance.
     * @param platform The platform this builder belongs to
     */
    public JdbcModelReader(DatabasePlatform platform) {
        this.platform = platform;

        defaultSizes.put(Types.CHAR, "254");
        defaultSizes.put(Types.VARCHAR, "254");
        defaultSizes.put(Types.LONGVARCHAR, "254");
        defaultSizes.put(Types.BINARY, "254");
        defaultSizes.put(Types.VARBINARY, "254");
        defaultSizes.put(Types.LONGVARBINARY, "254");
        defaultSizes.put(Types.INTEGER, "32");
        defaultSizes.put(Types.BIGINT, "64");
        defaultSizes.put(Types.REAL, "7,0");
        defaultSizes.put(Types.FLOAT, "15,0");
        defaultSizes.put(Types.DOUBLE, "15,0");
        defaultSizes.put(Types.DECIMAL, "15,15");
        defaultSizes.put(Types.NUMERIC, "15,15");

        columnsForTable = initColumnsForTable();
        columnsForColumn = initColumnsForColumn();
        columnsForPK = initColumnsForPK();
        columnsForFK = initColumnsForFK();
        columnsForIndex = initColumnsForIndex();
    }

    /**
     * Returns the platform that this model reader belongs to.
     * @return The platform
     */
    public DatabasePlatform getPlatform() {
        return platform;
    }

    /**
     * Returns the platform specific settings.
     * @return The platform settings
     */
    public PlatformInfo getPlatformInfo() {
        return platform.getPlatformInfo();
    }

    /**
     * Returns descriptors for the columns that shall be read from the result set when
     * reading the metadata for a table. Note that the columns are read in the order
     * defined by this list.<br/>
     * Redefine this method if you want more columns or a different order.
     * @return The descriptors for the result set columns
     */
    protected List<MetaDataColumnDescriptor> initColumnsForTable() {
        List<MetaDataColumnDescriptor> result = new ArrayList<>();

        result.add(new MetaDataColumnDescriptor("TABLE_NAME", Types.VARCHAR));
        result.add(new MetaDataColumnDescriptor("TABLE_TYPE", Types.VARCHAR, "UNKNOWN"));
        result.add(new MetaDataColumnDescriptor("TABLE_CAT", Types.VARCHAR));
        result.add(new MetaDataColumnDescriptor("TABLE_SCHEM", Types.VARCHAR));
        result.add(new MetaDataColumnDescriptor("REMARKS", Types.VARCHAR));

        return result;
    }

    /**
     * Returns descriptors for the columns that shall be read from the result set when
     * reading the metadata for table columns. Note that the columns are read in the order
     * defined by this list.<br/>
     * Redefine this method if you want more columns or a different order.
     * @return The map column name -> descriptor for the result set columns
     */
    protected List<MetaDataColumnDescriptor> initColumnsForColumn() {
        List<MetaDataColumnDescriptor> result = new ArrayList<>();

        // As suggested by Alexandre Borgoltz, we're reading the COLUMN_DEF first because Oracle
        // has problems otherwise (it seemingly requires a LONG column to be the first to be read)
        // See also DDLUTILS-29
        result.add(new MetaDataColumnDescriptor("COLUMN_DEF", Types.VARCHAR));
        // we're also reading the table name so that a model reader impl can filter manually
        result.add(new MetaDataColumnDescriptor("TABLE_NAME", Types.VARCHAR));
        result.add(new MetaDataColumnDescriptor("COLUMN_NAME", Types.VARCHAR));
        result.add(new MetaDataColumnDescriptor("DATA_TYPE", Types.INTEGER, Types.OTHER));
        result.add(new MetaDataColumnDescriptor("NUM_PREC_RADIX", Types.INTEGER, 10));
        result.add(new MetaDataColumnDescriptor("DECIMAL_DIGITS", Types.INTEGER, 0));
        result.add(new MetaDataColumnDescriptor("COLUMN_SIZE", Types.VARCHAR));
        result.add(new MetaDataColumnDescriptor("IS_NULLABLE", Types.VARCHAR, "YES"));
        result.add(new MetaDataColumnDescriptor("REMARKS", Types.VARCHAR));

        return result;
    }

    /**
     * Returns descriptors for the columns that shall be read from the result set when
     * reading the metadata for primary keys. Note that the columns are read in the order
     * defined by this list.<br/>
     * Redefine this method if you want more columns or a different order.
     * @return The map column name -> descriptor for the result set columns
     */
    protected List<MetaDataColumnDescriptor> initColumnsForPK() {
        List<MetaDataColumnDescriptor> result = new ArrayList<>();

        result.add(new MetaDataColumnDescriptor("COLUMN_NAME", Types.VARCHAR));
        // we're also reading the table name so that a model reader impl can filter manually
        result.add(new MetaDataColumnDescriptor("TABLE_NAME", Types.VARCHAR));
        // the name of the primary key is currently only interesting to the pk index name resolution
        result.add(new MetaDataColumnDescriptor("PK_NAME", Types.VARCHAR));

        return result;
    }

    /**
     * Returns descriptors for the columns that shall be read from the result set when
     * reading the metadata for foreign keys originating from a table. Note that the
     * columns are read in the order defined by this list.<br/>
     * Redefine this method if you want more columns or a different order.
     * @return The map column name -> descriptor for the result set columns
     */
    protected List<MetaDataColumnDescriptor> initColumnsForFK() {
        List<MetaDataColumnDescriptor> result = new ArrayList<>();

        result.add(new MetaDataColumnDescriptor("PKTABLE_NAME", Types.VARCHAR));
        // we're also reading the table name so that a model reader impl can filter manually
        result.add(new MetaDataColumnDescriptor("FKTABLE_NAME", Types.VARCHAR));
        result.add(new MetaDataColumnDescriptor("KEY_SEQ", Types.TINYINT, (short) 0));
        result.add(new MetaDataColumnDescriptor("FK_NAME", Types.VARCHAR));
        result.add(new MetaDataColumnDescriptor("UPDATE_RULE", Types.TINYINT));
        result.add(new MetaDataColumnDescriptor("DELETE_RULE", Types.TINYINT));
        result.add(new MetaDataColumnDescriptor("PKCOLUMN_NAME", Types.VARCHAR));
        result.add(new MetaDataColumnDescriptor("FKCOLUMN_NAME", Types.VARCHAR));

        return result;
    }

    /**
     * Returns descriptors for the columns that shall be read from the result set when
     * reading the metadata for indices. Note that the columns are read in the order
     * defined by this list.<br/>
     * Redefine this method if you want more columns or a different order.
     * @return The map column name -> descriptor for the result set columns
     */
    protected List<MetaDataColumnDescriptor> initColumnsForIndex() {
        List<MetaDataColumnDescriptor> result = new ArrayList<>();
        result.add(new MetaDataColumnDescriptor("INDEX_NAME", Types.VARCHAR));
        // we're also reading the table name so that a model reader impl can filter manually
        result.add(new MetaDataColumnDescriptor("TABLE_NAME", Types.VARCHAR));
        result.add(new MetaDataColumnDescriptor("NON_UNIQUE", Types.BIT, Boolean.TRUE));
        result.add(new MetaDataColumnDescriptor("ORDINAL_POSITION", Types.TINYINT, (short) 0));
        result.add(new MetaDataColumnDescriptor("COLUMN_NAME", Types.VARCHAR));
        result.add(new MetaDataColumnDescriptor("TYPE", Types.TINYINT));
        return result;
    }

    /**
     * Returns the catalog(s) in the database to read per default.
     * @return The default catalog(s)
     */
    public String getDefaultCatalogPattern() {
        return defaultCatalogPattern;
    }

    /**
     * Sets the catalog(s) in the database to read per default.
     * @param catalogPattern The catalog(s)
     */
    public void setDefaultCatalogPattern(String catalogPattern) {
        defaultCatalogPattern = catalogPattern;
    }

    /**
     * Returns the schema(s) in the database to read per default.
     * @return The default schema(s)
     */
    public String getDefaultSchemaPattern() {
        return defaultSchemaPattern;
    }

    /**
     * Sets the schema(s) in the database to read per default.
     * @param schemaPattern The schema(s)
     */
    public void setDefaultSchemaPattern(String schemaPattern) {
        defaultSchemaPattern = schemaPattern;
    }

    /**
     * Returns the default pattern to read the relevant tables from the database.
     * @return The table pattern
     */
    public String getDefaultTablePattern() {
        return defaultTablePattern;
    }

    /**
     * Sets the default pattern to read the relevant tables from the database.
     * @param tablePattern The table pattern
     */
    public void setDefaultTablePattern(String tablePattern) {
        defaultTablePattern = tablePattern;
    }

    /**
     * Returns the default pattern to read the relevant columns from the database.
     * @return The column pattern
     */
    public String getDefaultColumnPattern() {
        return defaultColumnPattern;
    }

    /**
     * Sets the default pattern to read the relevant columns from the database.
     * @param columnPattern The column pattern
     */
    public void setDefaultColumnPattern(String columnPattern) {
        defaultColumnPattern = columnPattern;
    }

    /**
     * Returns the table types to recognize per default.
     * @return The default table types
     */
    public String[] getDefaultTableTypes() {
        if (defaultTableTypes == null) {
            return null;
        } else {
            String[] result = new String[defaultTableTypes.length];
            System.arraycopy(defaultTableTypes, 0, result, 0, defaultTableTypes.length);
            return result;
        }
    }

    /**
     * Sets the table types to recognize per default. Typical types are "TABLE", "VIEW",
     * "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
     * @param types The table types
     */
    public void setDefaultTableTypes(String[] types) {
        if (types == null) {
            defaultTableTypes = null;
        } else {
            defaultTableTypes = new String[types.length];
            System.arraycopy(types, 0, types, 0, types.length);
        }
    }

    /**
     * Returns the descriptors for the columns to be read from the table metadata result set.
     * @return The column descriptors
     */
    protected List<MetaDataColumnDescriptor> getColumnsForTable() {
        return columnsForTable;
    }

    /**
     * Returns the descriptors for the columns to be read from the column metadata result set.
     * @return The column descriptors
     */
    protected List<MetaDataColumnDescriptor> getColumnsForColumn() {
        return columnsForColumn;
    }

    /**
     * Returns the descriptors for the columns to be read from the primary key metadata result set.
     * @return The column descriptors
     */
    protected List<MetaDataColumnDescriptor> getColumnsForPK() {
        return columnsForPK;
    }

    /**
     * Returns the descriptors for the columns to be read from the foreign key metadata result set.
     * @return The column descriptors
     */
    protected List<MetaDataColumnDescriptor> getColumnsForFK() {
        return columnsForFK;
    }

    /**
     * Returns the descriptors for the columns to be read from the index metadata result set.
     * @return The column descriptors
     */
    protected List<MetaDataColumnDescriptor> getColumnsForIndex() {
        return columnsForIndex;
    }

    /**
     * Returns the active connection. Note that this is only set during a call to
     * {@link #readTables(String, String, String[])}.
     * @return The connection or <code>null</code> if there is no active connection
     */
    protected Connection getConnection() {
        return connection;
    }

    /**
     * Reads the database model from the given connection.
     * @param connection   The connection
     * @param databaseName The name of the resulting database; <code>null</code> when the default name (the catalog)
     *                     is desired which might be <code>null</code> itself though
     * @return The database model
     */
    public Database getDatabase(Connection connection, String databaseName) throws SQLException {
        return getDatabase(connection, databaseName, null, null, null);
    }

    /**
     * Reads the database model from the given connection.
     * @param connection   The connection
     * @param databaseName The name of the resulting database; <code>null</code> when the default name (the catalog)
     *                     is desired which might be <code>null</code> itself though
     * @param catalog      The catalog to acess in the database; use <code>null</code> for the default value
     * @param schema       The schema to acess in the database; use <code>null</code> for the default value
     * @param tableTypes   The table types to process; use <code>null</code> or an empty list for the default ones
     * @return The database model
     */
    public Database getDatabase(Connection connection, String databaseName, String catalog, String schema, String[] tableTypes) throws SQLException {
        Objects.requireNonNull(connection, "connection cannot be null");
        Database db = new Database();
        if (databaseName == null || databaseName.isEmpty()) {
            try {
                db.setName(connection.getCatalog());
                if (catalog == null) {
                    catalog = db.getName();
                }
            } catch (Exception ex) {
                _log.info("Cannot determine the catalog name from connection.", ex);
            }
        } else {
            db.setName(databaseName);
        }
        try {
            this.connection = connection;
            db.addTables(readTables(catalog, schema, tableTypes));
            // Note that we do this here instead of in readTable since platforms may redefine the
            // readTable method whereas it is highly unlikely that this method gets redefined
            if (getPlatform().isForeignKeysSorted()) {
                sortForeignKeys(db);
            }
        } finally {
            JdbcUtils.closeQuitely(this.connection);
            this.connection = null;
        }
        db.initialize();
        return db;
    }

    /**
     * create DatabaseMetaDataWrapper
     * @param catalog       catalog
     * @param schemaPattern schemaPattern
     * @param tableTypes    tableTypes
     * @return DatabaseMetaDataWrapper
     */
    protected DatabaseMetaDataWrapper createDatabaseMetaDataWrapper(Connection connection, String catalog, String schemaPattern, String[] tableTypes) {
        DatabaseMetaDataWrapper metaData;
        try {
            metaData = new DatabaseMetaDataWrapper(connection.getMetaData());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        metaData.setCatalog(catalog == null ? getDefaultCatalogPattern() : catalog);
        metaData.setSchemaPattern(schemaPattern == null ? getDefaultSchemaPattern() : schemaPattern);
        metaData.setTableTypes((tableTypes == null) || (tableTypes.length == 0) ? getDefaultTableTypes() : tableTypes);
        return metaData;
    }

    /**
     * Reads the tables from the database metadata.
     * @param catalog       The catalog to acess in the database; use <code>null</code> for the default value
     * @param schemaPattern The schema(s) to acess in the database; use <code>null</code> for the default value
     * @param tableTypes    The table types to process; use <code>null</code> or an empty list for the default ones
     * @return The tables
     */
    protected Collection<Table> readTables(String catalog, String schemaPattern, String[] tableTypes) throws SQLException {
        ResultSet tableData = null;
        try {
            DatabaseMetaDataWrapper metaData = createDatabaseMetaDataWrapper(this.connection, catalog, schemaPattern, tableTypes);
            tableData = metaData.getTables(getDefaultTablePattern());

            final List<Table> tables = new ArrayList<>();

            while (tableData.next()) {
                ObjectMap values = readColumns(tableData, getColumnsForTable());
                Table table = readTable(metaData, values);

                if (table != null) {
                    tables.add(table);
                }
            }
            final Collator collator = Collator.getInstance();
            tables.sort((obj1, obj2) -> collator.compare(obj1.getName().toUpperCase(), obj2.getName().toUpperCase()));
            return tables;
        } finally {
            closeResultSet(tableData);
        }
    }

    /**
     * Reads the next table from the metadata.
     * @param metaData The database metadata
     * @param values   The table metadata values as defined by {@link #getColumnsForTable()}
     * @return The table or <code>null</code> if the result set row did not contain a valid table
     */
    protected Table readTable(DatabaseMetaDataWrapper metaData, ObjectMap values) throws SQLException {
        String tableName = (String) values.get("TABLE_NAME");
        Table table = null;

        if ((tableName != null) && (!tableName.isEmpty())) {
            table = new Table();

            table.setName(tableName);
            table.setType((String) values.get("TABLE_TYPE"));
            table.setCatalog((String) values.get("TABLE_CAT"));
            table.setSchema((String) values.get("TABLE_SCHEM"));
            table.setDescription((String) values.get("REMARKS"));

            table.addColumns(readColumns(metaData, tableName));
            table.addForeignKeys(readForeignKeys(metaData, tableName));
            table.addIndices(readIndices(metaData, tableName));

            Collection<String> primaryKeys = readPrimaryKeyNames(metaData, tableName);

            for (String primaryKey : primaryKeys) {
                table.findColumn(primaryKey, true).setPrimaryKey(true);
            }

            if (getPlatformInfo().isSystemIndicesReturned()) {
                removeSystemIndices(metaData, table);
            }
        }
        return table;
    }

    /**
     * Removes system indices (generated by the database for primary and foreign keys)
     * from the table.
     * @param metaData The database metadata
     * @param table    The table
     */
    protected void removeSystemIndices(DatabaseMetaDataWrapper metaData, Table table) throws SQLException {
        removeInternalPrimaryKeyIndex(metaData, table);

        for (int fkIdx = 0; fkIdx < table.getForeignKeyCount(); fkIdx++) {
            removeInternalForeignKeyIndex(metaData, table, table.getForeignKey(fkIdx));
        }
    }

    /**
     * Tries to remove the internal index for the table's primary key.
     * @param metaData The database metadata
     * @param table    The table
     */
    protected void removeInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table) throws SQLException {
        Column[] pks = table.getPrimaryKeyColumns();
        List<String> columnNames = new ArrayList<>();

        for (Column pk : pks) {
            columnNames.add(pk.getName());
        }

        for (int indexIdx = 0; indexIdx < table.getIndexCount(); ) {
            Index index = table.getIndex(indexIdx);

            if (index.isUnique() && matches(index, columnNames) && isInternalPrimaryKeyIndex(metaData, table, index)) {
                table.removeIndex(indexIdx);
            } else {
                indexIdx++;
            }
        }
    }

    /**
     * Tries to remove the internal index for the given foreign key.
     * @param metaData The database metadata
     * @param table    The table where the table is defined
     * @param fk       The foreign key
     */
    protected void removeInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk) throws SQLException {
        List<String> columnNames = new ArrayList<>();
        boolean mustBeUnique = !getPlatformInfo().isSystemForeignKeyIndicesAlwaysNonUnique();

        for (int columnIdx = 0; columnIdx < fk.getReferenceCount(); columnIdx++) {
            String name = fk.getReference(columnIdx).getLocalColumnName();
            Column localColumn = table.findColumn(name, getPlatform().isDelimitedIdentifierModeOn());

            if (mustBeUnique && !localColumn.isPrimaryKey()) {
                mustBeUnique = false;
            }
            columnNames.add(name);
        }

        for (int indexIdx = 0; indexIdx < table.getIndexCount(); ) {
            Index index = table.getIndex(indexIdx);

            if ((!mustBeUnique || index.isUnique()) && matches(index, columnNames) && isInternalForeignKeyIndex(metaData, table, fk, index)) {
                fk.setAutoIndexPresent(true);
                table.removeIndex(indexIdx);
            } else {
                indexIdx++;
            }
        }
    }

    /**
     * Checks whether the given index matches the column list.
     * @param index              The index
     * @param columnsToSearchFor The names of the columns that the index should be for
     * @return <code>true</code> if the index matches the columns
     */
    protected boolean matches(Index index, List<String> columnsToSearchFor) {
        if (index.getColumnCount() != columnsToSearchFor.size()) {
            return false;
        }
        for (int columnIdx = 0; columnIdx < index.getColumnCount(); columnIdx++) {
            if (!columnsToSearchFor.get(columnIdx).equals(index.getColumn(columnIdx).getName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tries to determine whether the index is the internal database-generated index
     * for the given table's primary key.
     * Note that only unique indices with the correct columns are fed to this method.
     * Redefine this method for specific platforms if there are better ways
     * to determine internal indices.
     * @param metaData The database metadata
     * @param table    The table owning the index
     * @param index    The index to check
     * @return <code>true</code> if the index seems to be an internal primary key one
     */
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) throws SQLException {
        return false;
    }

    /**
     * Tries to determine whether the index is the internal database-generated index
     * for the given foreign key.
     * Note that only non-unique indices with the correct columns are fed to this method.
     * Redefine this method for specific platforms if there are better ways
     * to determine internal indices.
     * @param metaData The database metadata
     * @param table    The table owning the index and foreign key
     * @param fk       The foreign key
     * @param index    The index to check
     * @return <code>true</code> if the index seems to be an internal primary key one
     */
    protected boolean isInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk, Index index) throws SQLException {
        return false;
    }

    /**
     * Reads the column definitions for the indicated table.
     * @param metaData  The database metadata
     * @param tableName The name of the table
     * @return The columns
     */
    protected Collection<Column> readColumns(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        ResultSet columnData = null;

        try {
            columnData = metaData.getColumns(metaData.escapeForSearch(tableName), getDefaultColumnPattern());

            List<Column> columns = new ArrayList<>();

            while (columnData.next()) {
                ObjectMap values = readColumns(columnData, getColumnsForColumn());

                columns.add(readColumn(metaData, values));
            }
            return columns;
        } finally {
            closeResultSet(columnData);
        }
    }

    /**
     * Extracts a column definition from the result set.
     * @param metaData The database metadata
     * @param values   The column metadata values as defined by {@link #getColumnsForColumn()}
     * @return The column
     */
    protected Column readColumn(DatabaseMetaDataWrapper metaData, ObjectMap values) throws SQLException {
        Column column = new Column();

        column.setName((String) values.get("COLUMN_NAME"));
        column.setDefaultValue((String) values.get("COLUMN_DEF"));
        column.setJdbcTypeCode((Integer) values.get("DATA_TYPE"));

        Integer precision = (Integer) values.get("NUM_PREC_RADIX");

        if (precision != null) {
            column.setPrecisionRadix(precision);
        }

        String size = (String) values.get("COLUMN_SIZE");

        if (size == null) {
            size = defaultSizes.get(column.getJdbcTypeCode());
        }
        // we're setting the size after the precision and radix in case
        // the database prefers to return them in the size value
        column.setSize(size);

        Integer scale = (Integer) values.get("DECIMAL_DIGITS");

        if (scale != null) {
            // if there is a scale value, set it after the size (which probably did not contain
            // a scale specification)
            column.setScale(scale);
        }
        column.setRequired("NO".equalsIgnoreCase(((String) values.get("IS_NULLABLE")).trim()));

        String description = (String) values.get("REMARKS");

        if (!StringUtils.isEmpty(description)) {
            column.setDescription(description);
        }
        return column;
    }

    /**
     * Retrieves the names of the columns that make up the primary key for a given table.
     * @param metaData  The database metadata
     * @param tableName The name of the table from which to retrieve PK information
     * @return The primary key column names
     */
    protected Collection<String> readPrimaryKeyNames(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        List<String> pks = new ArrayList<>();
        ResultSet pkData = null;
        try {
            pkData = metaData.getPrimaryKeys(metaData.escapeForSearch(tableName));
            while (pkData.next()) {
                ObjectMap values = readColumns(pkData, getColumnsForPK());
                pks.add(readPrimaryKeyName(metaData, values));
            }
        } finally {
            closeResultSet(pkData);
        }
        return pks;
    }

    /**
     * Extracts a primary key name from the result set.
     * @param metaData The database metadata
     * @param values   The primary key metadata values as defined by {@link #getColumnsForPK()}
     * @return The primary key name
     */
    protected String readPrimaryKeyName(DatabaseMetaDataWrapper metaData, ObjectMap values) throws SQLException {
        return values.getString("COLUMN_NAME");
    }

    /**
     * Retrieves the foreign keys of the indicated table.
     * @param metaData  The database metadata
     * @param tableName The name of the table from which to retrieve FK information
     * @return The foreign keys
     */
    protected Collection<ForeignKey> readForeignKeys(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        Map<String, ForeignKey> fks = new LinkedHashMap<>();
        ResultSet fkData = null;
        try {
            fkData = metaData.getForeignKeys(metaData.escapeForSearch(tableName));
            while (fkData.next()) {
                ObjectMap values = readColumns(fkData, getColumnsForFK());
                readForeignKey(metaData, values, fks);
            }
        } finally {
            closeResultSet(fkData);
        }
        return fks.values();
    }

    /**
     * Reads the next foreign key spec from the result set.
     * @param metaData The database metadata
     * @param values   The foreign key metadata as defined by {@link #getColumnsForFK()}
     * @param knownFks The already read foreign keys for the current table
     */
    protected void readForeignKey(DatabaseMetaDataWrapper metaData, Map<String, Object> values, Map<String, ForeignKey> knownFks) throws SQLException {
        String fkName = (String) values.get("FK_NAME");
        ForeignKey fk = knownFks.get(fkName);

        if (fk == null) {
            fk = new ForeignKey(fkName);
            fk.setForeignTableName((String) values.get("PKTABLE_NAME"));

            CascadeActionEnum onUpdateAction = convertAction((Short) values.get("UPDATE_RULE"));
            CascadeActionEnum onDeleteAction = convertAction((Short) values.get("DELETE_RULE"));

            if (onUpdateAction == null) {
                onUpdateAction = getPlatformInfo().getDefaultOnUpdateAction();
            }
            if (onDeleteAction == null) {
                onDeleteAction = getPlatformInfo().getDefaultOnDeleteAction();
            }
            fk.setOnUpdate(onUpdateAction);
            fk.setOnDelete(onDeleteAction);
            knownFks.put(fkName, fk);
        }

        Reference ref = new Reference();

        ref.setForeignColumnName((String) values.get("PKCOLUMN_NAME"));
        ref.setLocalColumnName((String) values.get("FKCOLUMN_NAME"));
        if (values.containsKey("KEY_SEQ")) {
            ref.setSequenceValue(((Short) values.get("KEY_SEQ")).intValue());
        }
        fk.addReference(ref);
    }

    /**
     * Converts the JDBC action value (one of the <code>importKey</code> constants in the
     * {@link DatabaseMetaData} class) to a {@link CascadeActionEnum}.
     * @param jdbcActionValue The jdbc action value
     * @return The enum value
     */
    protected CascadeActionEnum convertAction(Short jdbcActionValue) {
        CascadeActionEnum action = null;
        if (jdbcActionValue != null) {
            switch (jdbcActionValue) {
                case DatabaseMetaData.importedKeyCascade -> action = CascadeActionEnum.CASCADE;
                case DatabaseMetaData.importedKeySetNull -> action = CascadeActionEnum.SET_NULL;
                case DatabaseMetaData.importedKeySetDefault -> action = CascadeActionEnum.SET_DEFAULT;
                case DatabaseMetaData.importedKeyRestrict -> action = CascadeActionEnum.RESTRICT;
            }
        }
        return action;
    }

    /**
     * Determines the indices for the indicated table.
     * @param metaData  The database metadata
     * @param tableName The name of the table
     * @return The list of indices
     */
    protected Collection<Index> readIndices(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        Map<String, Index> indices = new LinkedHashMap<>();
        ResultSet indexData = null;
        try {
            indexData = metaData.getIndices(metaData.escapeForSearch(tableName), false, false);
            while (indexData.next()) {
                ObjectMap values = readColumns(indexData, getColumnsForIndex());
                readIndex(metaData, values, indices);
            }
        } finally {
            closeResultSet(indexData);
        }
        return indices.values();
    }

    /**
     * Reads the next index spec from the result set.
     * @param metaData     The database metadata
     * @param values       The index metadata as defined by {@link #getColumnsForIndex()}
     * @param knownIndices The already read indices for the current table
     */
    protected void readIndex(DatabaseMetaDataWrapper metaData, ObjectMap values, Map<String, Index> knownIndices) throws SQLException {
        Short indexType = (Short) values.get("TYPE");

        // we're ignoring statistic indices
        if ((indexType != null) && (indexType == DatabaseMetaData.tableIndexStatistic)) {
            return;
        }

        String indexName = (String) values.get("INDEX_NAME");

        if (indexName != null) {
            Index index = knownIndices.get(indexName);

            if (index == null) {
                if ((Boolean) values.get("NON_UNIQUE")) {
                    index = new NonUniqueIndex();
                } else {
                    index = new UniqueIndex();
                }

                index.setName(indexName);
                knownIndices.put(indexName, index);
            }

            IndexColumn indexColumn = new IndexColumn();

            indexColumn.setName((String) values.get("COLUMN_NAME"));
            if (values.containsKey("ORDINAL_POSITION")) {
                indexColumn.setOrdinalPosition(((Short) values.get("ORDINAL_POSITION")).intValue());
            }
            index.addColumn(indexColumn);
        }
    }

    /**
     * Reads the indicated columns from the result set.
     * @param resultSet         The result set
     * @param columnDescriptors The dscriptors of the columns to read
     * @return The read values keyed by the column name
     */
    protected ObjectMap readColumns(ResultSet resultSet, List<MetaDataColumnDescriptor> columnDescriptors) throws SQLException {
        ObjectMap values = new ObjectMap();
        for (MetaDataColumnDescriptor descriptor : columnDescriptors) {
            values.put(descriptor.getName(), descriptor.readColumn(resultSet));
        }
        return values;
    }

    /**
     * Helper method that determines the auto increment status for the given columns via the
     * {@link ResultSetMetaData#isAutoIncrement(int)} method.
     * @param table          The table
     * @param columnsToCheck The columns to check (e.g. the primary key columns)
     */
    protected void determineAutoIncrementFromResultSetMetaData(Table table, Column[] columnsToCheck) throws SQLException {
        if ((columnsToCheck == null) || (columnsToCheck.length == 0)) {
            return;
        }

        StringBuilder query = new StringBuilder();

        query.append("SELECT ");
        for (int idx = 0; idx < columnsToCheck.length; idx++) {
            if (idx > 0) {
                query.append(",");
            }
            if (getPlatform().isDelimitedIdentifierModeOn()) {
                query.append(getPlatformInfo().getDelimiterToken());
            }
            query.append(columnsToCheck[idx].getName());
            if (getPlatform().isDelimitedIdentifierModeOn()) {
                query.append(getPlatformInfo().getDelimiterToken());
            }
        }
        query.append(" FROM ");
        if (getPlatform().isDelimitedIdentifierModeOn()) {
            query.append(getPlatformInfo().getDelimiterToken());
        }
        query.append(table.getName());
        if (getPlatform().isDelimitedIdentifierModeOn()) {
            query.append(getPlatformInfo().getDelimiterToken());
        }
        query.append(" WHERE 1 = 0");

        Statement stmt = null;

        try {
            stmt = getConnection().createStatement();

            ResultSet rs = stmt.executeQuery(query.toString());
            ResultSetMetaData rsMetaData = rs.getMetaData();

            for (int idx = 0; idx < columnsToCheck.length; idx++) {
                if (rsMetaData.isAutoIncrement(idx + 1)) {
                    columnsToCheck[idx].setAutoIncrement(true);
                }
            }
        } finally {
            closeStatement(stmt);
        }
    }

    /**
     * Sorts the foreign keys in the tables of the model.
     * @param model The model
     */
    protected void sortForeignKeys(Database model) {
        for (int tableIdx = 0; tableIdx < model.getTableCount(); tableIdx++) {
            model.getTable(tableIdx).sortForeignKeys(getPlatform().isDelimitedIdentifierModeOn());
        }
    }

    /**
     * Replaces a specific character sequence in the given text with the character sequence
     * whose escaped version it is.
     * @param text      The text
     * @param unescaped The unescaped string, e.g. "'"
     * @param escaped   The escaped version, e.g. "''"
     * @return The resulting text
     */
    protected String unescape(String text, String unescaped, String escaped) {
        String result = text;

        // we need special handling if the single quote is escaped via a double single quote
        if (result != null) {
            if (escaped.equals("''")) {
                if ((result.length() > 2) && result.startsWith("'") && result.endsWith("'")) {
                    result = "'" + StringUtils.replace(result.substring(1, result.length() - 1), escaped, unescaped) + "'";
                } else {
                    result = StringUtils.replace(result, escaped, unescaped);
                }
            } else {
                result = StringUtils.replace(result, escaped, unescaped);
            }
        }
        return result;
    }

    /**
     * Tries to find the schema to which the given table belongs.
     * @param connection    The database connection
     * @param schemaPattern The schema pattern to limit the schemas to search in
     * @param table         The table to search for
     * @return The schema name or <code>null</code> if the schema of the table
     * could not be found Will be removed once full schema support is in place
     */
    public String determineSchemaOf(Connection connection, String schemaPattern, Table table) throws SQLException {
        ResultSet tableData = null;
        ResultSet columnData = null;

        try {
            DatabaseMetaDataWrapper metaData = new DatabaseMetaDataWrapper();

            metaData.setMetaData(connection.getMetaData());
            metaData.setCatalog(getDefaultCatalogPattern());
            metaData.setSchemaPattern(schemaPattern == null ? getDefaultSchemaPattern() : schemaPattern);
            metaData.setTableTypes(getDefaultTableTypes());

            String tablePattern = table.getName();

            if (getPlatform().isDelimitedIdentifierModeOn()) {
                tablePattern = tablePattern.toUpperCase();
            }

            tableData = metaData.getTables(metaData.escapeForSearch(tablePattern));

            boolean found = false;
            String schema = null;

            while (!found && tableData.next()) {
                ObjectMap values = readColumns(tableData, getColumnsForTable());
                String tableName = (String) values.get("TABLE_NAME");

                if ((tableName != null) && (!tableName.isEmpty())) {
                    schema = (String) values.get("TABLE_SCHEM");
                    columnData = metaData.getColumns(metaData.escapeForSearch(tableName), getDefaultColumnPattern());
                    found = true;

                    while (found && columnData.next()) {
                        values = readColumns(columnData, getColumnsForColumn());

                        if (table.findColumn((String) values.get("COLUMN_NAME"), getPlatform().isDelimitedIdentifierModeOn()) == null) {
                            found = false;
                        }
                    }
                    columnData.close();
                    columnData = null;
                }
            }
            return found ? schema : null;
        } finally {
            closeResultSet(columnData);
            closeResultSet(tableData);
        }
    }

    /**
     * Closes the given result set. Any exceptions are logged on warn level but otherwise swallowed.
     * @param resultSet The result set
     */
    protected void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                _log.warn("Error while closing result set", ex);
            }
        }
    }

    /**
     * Closes the given statement. Any exceptions are logged on warn level but otherwise swallowed.
     * @param stmt The statement
     */
    protected void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                _log.warn("Error while closing statement", ex);
            }
        }
    }
}