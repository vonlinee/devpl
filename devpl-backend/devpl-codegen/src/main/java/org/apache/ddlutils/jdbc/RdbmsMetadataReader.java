package org.apache.ddlutils.jdbc;

import org.apache.ddlutils.jdbc.meta.ColumnMetadata;
import org.apache.ddlutils.jdbc.meta.ResultSetColumnMetadata;
import org.apache.ddlutils.jdbc.meta.TableMetadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <a href="https://www.zhihu.com/question/20355738">...</a>
 * 在关系型数据库中，分三级：database.schema.table。即一个数据库下面可以包含多个schema，一个schema下可以包含多个数据库对象，比如表、存储过程、触发器等。
 * 但并非所有数据库都实现了schema这一层，比如mysql直接把schema和database等效了，PostgreSQL、Oracle、SQL server等的schema也含义不太相同。
 * 所以说，关系型数据库中没有catalog的概念。但在一些其它地方（特别是大数据领域的一些组件）有catalog的概念，也是用来做层级划分的，
 * 一般是这样的层级关系：catalog.database.table。
 */
public interface RdbmsMetadataReader {

    /**
     * 获取数据库支持的表类型
     *
     * @return 支持的表类型
     * @throws SQLException 数据库操作异常
     * @see DatabaseMetaData#getTableTypes()
     */
    String[] getTableTypes() throws SQLException;

    /**
     * 获取表的元数据
     *
     * @return ColumnMetadata
     * @throws SQLException 数据库操作异常
     * @see java.sql.DatabaseMetaData#getTables(String, String, String, String[])
     */
    List<TableMetadata> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException;

    /**
     * 获取列的元数据
     *
     * @return ColumnMetadata
     * @throws SQLException 数据库操作异常
     * @see java.sql.DatabaseMetaData#getColumns(String, String, String, String)
     */
    List<ColumnMetadata> getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException;

    /**
     * 获取结果集的元数据
     *
     * @param rs ResultSet
     * @return ResultSetColumnMetadata
     * @throws SQLException 数据库操作异常
     * @see ResultSet#getMetaData()
     */
    List<ResultSetColumnMetadata> getResultSetColumns(ResultSet rs) throws SQLException;
}
