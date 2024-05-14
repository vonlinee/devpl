package org.apache.ddlutils.platform;

import org.apache.ddlutils.jdbc.meta.DatabaseMetadataReader;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Wrapper class for database metadata that stores additional info.
 */
public class DatabaseMetaDataWrapper {

    /**
     * Matches the characters not allowed in search strings.
     */
    private final Pattern searchStringPattern = Pattern.compile("[_%]");
    DatabaseMetadataReader reader;
    /**
     * The database metadata.
     */
    private DatabaseMetaData _metaData;
    /**
     * The catalog to access in the database.
     */
    private String _catalog;
    /**
     * The schema(s) to access in the database.
     */
    private String _schemaPattern;
    /**
     * The table types to process.
     */
    private String[] _tableTypes;
    public DatabaseMetaDataWrapper(DatabaseMetadataReader reader) {
        this.reader = reader;
    }
    public DatabaseMetaDataWrapper() {
    }

    /**
     * Returns the database metadata.
     *
     * @return The meta data
     */
    public DatabaseMetaData getMetaData() {
        return _metaData;
    }

    /**
     * Sets the database metadata.
     *
     * @param metaData The meta data
     */
    public void setMetaData(DatabaseMetaData metaData) {
        _metaData = metaData;
    }

    /**
     * Returns the catalog in the database to read.
     *
     * @return The catalog
     */
    public String getCatalog() {
        return _catalog;
    }

    /**
     * Sets the catalog in the database to read.
     *
     * @param catalog The catalog
     */
    public void setCatalog(String catalog) {
        _catalog = catalog;
    }

    /**
     * Returns the schema in the database to read.
     *
     * @return The schema
     */
    public String getSchemaPattern() {
        return _schemaPattern;
    }

    /**
     * Sets the schema in the database to read.
     *
     * @param schema The schema
     */
    public void setSchemaPattern(String schema) {
        _schemaPattern = schema;
    }

    /**
     * Returns the table types to recognize.
     *
     * @return The table types
     */
    public List<String> getTableTypes() throws SQLException {
        return reader.getTableTypes();
    }

    /**
     * Sets the table types to recognize.
     *
     * @param types The table types
     */
    public void setTableTypes(String[] types) {
        if (types == null) {
            _tableTypes = null;
        } else {
            _tableTypes = new String[types.length];
            System.arraycopy(types, 0, _tableTypes, 0, types.length);
        }
    }

    /**
     * Escape a string literal so that it can be used as a search pattern.
     *
     * @param literalString The string to escape.
     * @return A string that can be properly used as a search string.
     * @throws SQLException If an error occurred retrieving the metadata
     */
    public String escapeForSearch(String literalString) throws SQLException {
        String escape = getMetaData().getSearchStringEscape();
        if (Objects.equals(escape, "")) {
            // No escape string, so nothing to do...
            return literalString;
        } else {
            // with Java 5, we would just use Matcher.quoteReplacement
            StringBuilder quotedEscape = new StringBuilder();
            for (int idx = 0; idx < escape.length(); idx++) {
                char c = escape.charAt(idx);
                switch (c) {
                    case '\\' -> quotedEscape.append("\\\\");
                    case '$' -> quotedEscape.append("\\$");
                    default -> quotedEscape.append(c);
                }
            }
            quotedEscape.append("$0");
            return searchStringPattern.matcher(literalString).replaceAll(quotedEscape.toString());
        }
    }

    /**
     * Convenience method to return the table metadata using the configured catalog,
     * schema pattern and table types.
     *
     * @param tableNamePattern The pattern identifying for which tables to return info
     * @return The table meta data
     * @throws SQLException If an error occurred retrieving the metadata
     * @see DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     */
    public ResultSet getTables(String tableNamePattern) throws SQLException {
        return _metaData.getTables(getCatalog(), getSchemaPattern(), tableNamePattern, getTableTypes().toArray(String[]::new));
    }

    /**
     * Convenience method to return the column metadata using the configured catalog and
     * schema pattern.
     *
     * @param tableNamePattern  The pattern identifying for which tables to return info
     * @param columnNamePattern The pattern identifying for which columns to return info
     * @return The column meta data
     * @throws SQLException If an error occurred retrieving the metadata
     * @see DatabaseMetaData#getColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public ResultSet getColumns(String tableNamePattern, String columnNamePattern) throws SQLException {
        return _metaData.getColumns(getCatalog(), getSchemaPattern(), tableNamePattern, columnNamePattern);
    }

    /**
     * Convenience method to return the primary key metadata using the configured catalog and
     * schema pattern.
     *
     * @param tableNamePattern The pattern identifying for which tables to return info
     * @return The primary key metadata
     * @throws SQLException If an error occurred retrieving the metadata
     * @see DatabaseMetaData#getPrimaryKeys(java.lang.String, java.lang.String, java.lang.String)
     */
    public ResultSet getPrimaryKeys(String tableNamePattern) throws SQLException {
        return getMetaData().getPrimaryKeys(getCatalog(), getSchemaPattern(), tableNamePattern);
    }

    /**
     * Convenience method to return the foreign key metadata using the configured catalog and
     * schema pattern.
     *
     * @param tableNamePattern The pattern identifying for which tables to return info
     * @return The foreign key metadata
     * @throws SQLException If an error occurred retrieving the metadata
     * @see DatabaseMetaData#getImportedKeys(java.lang.String, java.lang.String, java.lang.String)
     */
    public ResultSet getForeignKeys(String tableNamePattern) throws SQLException {
        return getMetaData().getImportedKeys(getCatalog(), getSchemaPattern(), tableNamePattern);
    }

    /**
     * Convenience method to return the index metadata using the configured catalog and
     * schema pattern.
     *
     * @param tableNamePattern The pattern identifying for which tables to return info
     * @param unique           Whether to return only indices for unique values
     * @param approximate      Whether the result is allowed to reflect approximate or out of data values
     * @return The index meta data
     * @throws SQLException If an error occurred retrieving the metadata
     * @see DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)
     */
    public ResultSet getIndices(String tableNamePattern, boolean unique, boolean approximate) throws SQLException {
        return getMetaData().getIndexInfo(getCatalog(), getSchemaPattern(), tableNamePattern, unique, approximate);
    }
}
