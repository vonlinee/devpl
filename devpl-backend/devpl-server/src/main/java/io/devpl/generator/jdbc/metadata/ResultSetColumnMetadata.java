package io.devpl.generator.jdbc.metadata;

import java.io.Serializable;

/**
 * JDBC查询结果元数据
 *
 * @see java.sql.ResultSet
 * @see java.sql.ResultSetMetaData
 */
public class ResultSetColumnMetadata implements Serializable {

    /**
     * 列名
     *
     * @see java.sql.ResultSetMetaData#getColumnName(int)
     */
    private String columnName;

    /**
     * 列标题，通常是as语句取的别名，如果sql没有as，则结果与columnName相同
     *
     * @see java.sql.ResultSetMetaData#getColumnLabel(int)
     */
    private String columnLabel;

    /**
     * @see java.sql.ResultSetMetaData#getColumnClassName(int)
     */
    private String columnClassName;

    /**
     * @see java.sql.ResultSetMetaData#getColumnType(int)
     */
    private int columnType;

    /**
     * @see java.sql.ResultSetMetaData#getTableName(int)
     */
    private String tableName;

    /**
     * @see java.sql.ResultSetMetaData#getCatalogName(int)
     */
    private String catalogName;

    /**
     * @see java.sql.ResultSetMetaData#getColumnDisplaySize(int)
     */
    private int columnDisplaySize;

    /**
     * @see java.sql.ResultSetMetaData#getColumnTypeName(int)
     */
    private String columnTypeName;

    /**
     * @see java.sql.ResultSetMetaData#getPrecision(int)
     */
    private int precision;

    /**
     * @see java.sql.ResultSetMetaData#getScale(int)
     */
    private int scale;

    /**
     * @see java.sql.ResultSetMetaData#getSchemaName(int)
     */
    private String schemaName;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    public String getColumnClassName() {
        return columnClassName;
    }

    public void setColumnClassName(String columnClassName) {
        this.columnClassName = columnClassName;
    }

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String typeName) {
        this.tableName = typeName;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public int getColumnDisplaySize() {
        return columnDisplaySize;
    }

    public void setColumnDisplaySize(int columnDisplaySize) {
        this.columnDisplaySize = columnDisplaySize;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
}
