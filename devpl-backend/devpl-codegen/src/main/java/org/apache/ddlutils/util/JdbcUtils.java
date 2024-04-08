package org.apache.ddlutils.util;

import io.devpl.codegen.jdbc.meta.ResultSetColumnMetadata;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtils {

    /**
     * Determines whether a value for the specified column is present in the given result set.
     *
     * @param resultSet  The result set 未关闭的ResultSet
     * @param columnName 不为空
     * @return <code>true</code> if the column is present in the result set
     */
    public static boolean isColumnInResultSet(ResultSet resultSet, String columnName) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int idx = 1; idx <= metaData.getColumnCount(); idx++) {
            if (columnName.equals(metaData.getColumnName(idx).toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 关闭连接
     *
     * @param connection 数据库连接
     */
    public static void closeQuietly(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void closeQuietly(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void closeQuietly(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void closeQuietly(Statement stmt, Connection connection) {
        closeQuietly(stmt);
        closeQuietly(connection);
    }

    public static Connection getConnection(DataSource dataSource, String username, String password) throws SQLException {
        Connection connection;
        if (username != null) {
            connection = dataSource.getConnection(username, password);
        } else {
            connection = dataSource.getConnection();
        }
        return connection;
    }

    /**
     * get metadata of result set
     *
     * @param resultSet ResultSet
     * @return list of ResultSetColumnMetadata
     * @throws SQLException errors when get the metadata of ResultSet
     */
    public static List<ResultSetColumnMetadata> getColumnMetadata(ResultSet resultSet) throws SQLException {
        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int colCount = metaData.getColumnCount();
        List<ResultSetColumnMetadata> list = new ArrayList<>(colCount);
        for (int i = 1; i < colCount + 1; i++) {
            ResultSetColumnMetadata rsc = new ResultSetColumnMetadata();
            rsc.setColumnName(metaData.getColumnName(i));
            rsc.setColumnLabel(metaData.getColumnLabel(i));
            rsc.setColumnClassName(metaData.getColumnClassName(i));
            rsc.setColumnType(metaData.getColumnType(i));
            rsc.setTableName(metaData.getTableName(i));
            rsc.setCatalogName(metaData.getCatalogName(i));
            rsc.setColumnDisplaySize(metaData.getColumnDisplaySize(i));
            rsc.setColumnTypeName(metaData.getColumnTypeName(i));
            rsc.setPrecision(metaData.getPrecision(i));
            rsc.setScale(metaData.getScale(i));
            rsc.setSchemaName(metaData.getSchemaName(i));
            list.add(rsc);
        }
        return list;
    }
}
