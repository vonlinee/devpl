package io.devpl.codegen.db.query;

import io.devpl.codegen.db.DBType;
import io.devpl.codegen.generator.config.JdbcConfiguration;
import io.devpl.codegen.generator.config.LikeTable;
import io.devpl.codegen.generator.config.StrategyConfiguration;
import io.devpl.codegen.jdbc.JdbcUtils;
import io.devpl.sdk.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DbQueryDecorator extends AbstractDbQuery {
    private final AbstractDbQuery dbQuery;
    private final Connection connection;
    private final DBType dbType;
    private final StrategyConfiguration strategyConfiguration;
    private final String schema;
    private final Logger logger;

    public DbQueryDecorator(@NotNull JdbcConfiguration jdbcConfiguration, @NotNull StrategyConfiguration strategyConfiguration) {
        AbstractDbQuery iDbQuery = jdbcConfiguration.getDbQuery();
        if (null == iDbQuery) {
            DBType dbType = JdbcUtils.getDbType(jdbcConfiguration.getConnectionUrl());
            // 默认 MYSQL
            AbstractDbQuery dialect = DbQueryRegistry.getDbQuery(dbType);
            if (dialect == null) {
                dialect = DbQueryRegistry.getDbQuery(DBType.MYSQL);
            }
            iDbQuery = dialect;
        }
        this.dbQuery = iDbQuery;
        this.connection = jdbcConfiguration.getConnection();
        this.dbType = jdbcConfiguration.getDbType();
        this.strategyConfiguration = strategyConfiguration;
        this.schema = jdbcConfiguration.getSchemaName();
        this.logger = LoggerFactory.getLogger(dbQuery.getClass());
    }

    @Override
    public String tablesSql() {
        String tablesSql = dbQuery.tablesSql();
        if (DBType.POSTGRE_SQL == dbType || DBType.KINGBASE_ES == dbType || DBType.DB2 == dbType || DBType.ORACLE == dbType) {
            tablesSql = String.format(tablesSql, this.schema);
        }
        if (strategyConfiguration.isEnableSqlFilter()) {
            StringBuilder sql = new StringBuilder(tablesSql);
            LikeTable table;
            Set<String> tables;
            if ((table = strategyConfiguration.getLikeTable()) != null) {
                sql.append(" AND ").append(dbQuery.tableName()).append(" LIKE '").append(table.getValue()).append("'");
            } else if ((table = strategyConfiguration.getNotLikeTable()) != null) {
                sql.append(" AND ").append(dbQuery.tableName()).append(" NOT LIKE '").append(table.getValue())
                    .append("'");
            }
            if (!(tables = strategyConfiguration.getInclude()).isEmpty()) {
                sql.append(" AND ").append(dbQuery.tableName()).append(" IN (")
                    .append(tables.stream().map(tb -> "'" + tb + "'").collect(Collectors.joining(","))).append(")");
            } else if (!(tables = strategyConfiguration.getExclude()).isEmpty()) {
                sql.append(" AND ").append(dbQuery.tableName()).append(" NOT IN (")
                    .append(tables.stream().map(tb -> "'" + tb + "'").collect(Collectors.joining(","))).append(")");
            }
            return sql.toString();
        }
        return tablesSql;
    }

    @Override
    public String tableFieldsSql() {
        return dbQuery.tableFieldsSql();
    }

    /**
     * 扩展{@link #tableFieldsSql()}方法
     *
     * @param tableName 表名
     * @return 查询表字段语句
     */
    public String tableFieldsSql(String tableName) {
        String tableFieldsSql = this.tableFieldsSql();
        if (DBType.KINGBASE_ES == dbType || DBType.DB2 == dbType) {
            tableFieldsSql = String.format(tableFieldsSql, this.schema, tableName);
        } else if (DBType.ORACLE == dbType) {
            tableFieldsSql = String.format(tableFieldsSql.replace("#schema", this.schema), tableName, tableName.toUpperCase());
        } else if (DBType.DM == dbType) {
            tableName = tableName.toUpperCase();
            tableFieldsSql = String.format(tableFieldsSql, tableName);
        } else if (DBType.POSTGRE_SQL == dbType) {
            tableFieldsSql = String.format(tableFieldsSql, tableName, tableName, tableName);
        } else {
            tableFieldsSql = String.format(tableFieldsSql, tableName);
        }
        return tableFieldsSql;
    }

    @Override
    public String tableName() {
        return dbQuery.tableName();
    }

    @Override
    public String tableComment() {
        return dbQuery.tableComment();
    }

    @Override
    public String fieldName() {
        return dbQuery.fieldName();
    }

    @Override
    public String fieldType() {
        return dbQuery.fieldType();
    }

    @Override
    public String fieldComment() {
        return dbQuery.fieldComment();
    }

    @Override
    public String fieldKey() {
        return dbQuery.fieldKey();
    }

    @Override
    public boolean isKeyIdentity(ResultSet results) {
        try {
            return dbQuery.isKeyIdentity(results);
        } catch (SQLException e) {
            logger.warn("判断主键自增错误:{}", e.getMessage());
            // ignore 这个看到在查H2的时候出了异常，先忽略这个异常了.
        }
        return false;
    }

    @Override
    public String[] fieldCustom() {
        return dbQuery.fieldCustom();
    }

    public Map<String, Object> getCustomFields(ResultSet resultSet) {
        String[] fcs = this.fieldCustom();
        if (null != fcs) {
            Map<String, Object> customMap = new HashMap<>(fcs.length);
            for (String fc : fcs) {
                try {
                    customMap.put(fc, resultSet.getObject(fc));
                } catch (SQLException sqlException) {
                    throw new RuntimeException("获取自定义字段错误:", sqlException);
                }
            }
            return customMap;
        }
        return Collections.emptyMap();
    }

    /**
     * 执行 SQL 查询，回调返回结果
     *
     * @param sql      执行SQL
     * @param consumer 结果处理
     * @throws SQLException SQLException
     */
    public void execute(String sql, Consumer<ResultSetWrapper> consumer) throws SQLException {
        logger.debug("执行SQL:{}", sql);
        int count = 0;
        long start = System.nanoTime();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                consumer.accept(new ResultSetWrapper(resultSet, this, this.dbType));
                count++;
            }
            long end = System.nanoTime();
            logger.debug("返回记录数:{},耗时(ms):{}", count, (end - start) / 1000000);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class ResultSetWrapper {

        private final AbstractDbQuery dbQuery;

        private final ResultSet resultSet;

        private final DBType dbType;

        ResultSetWrapper(ResultSet resultSet, AbstractDbQuery dbQuery, DBType dbType) {
            this.resultSet = resultSet;
            this.dbQuery = dbQuery;
            this.dbType = dbType;
        }

        public ResultSet getResultSet() {
            return resultSet;
        }

        public String getStringResult(String columnLabel) {
            try {
                return resultSet.getString(columnLabel);
            } catch (SQLException sqlException) {
                throw new RuntimeException(String.format("读取[%s]字段出错!", columnLabel), sqlException);
            }
        }

        /**
         * @return 获取字段注释
         */
        public String getFiledComment() {
            return getComment(dbQuery.fieldComment());
        }

        /**
         * 获取格式化注释
         *
         * @param columnLabel 字段列
         * @return 注释
         */
        private String getComment(String columnLabel) {
            return StringUtils.hasText(columnLabel) ? formatComment(getStringResult(columnLabel)) : "";
        }

        /**
         * 获取表注释
         *
         * @return 表注释
         */
        public String getTableComment() {
            return getComment(dbQuery.tableComment());
        }

        /**
         * @param comment 注释
         * @return 格式化内容
         */
        public String formatComment(String comment) {
            return StringUtils.isBlank(comment) ? "" : comment.replaceAll("\r\n", "\t");
        }

        /**
         * @return 是否主键
         */
        public boolean isPrimaryKey() {
            String key = this.getStringResult(dbQuery.fieldKey());
            if (DBType.DB2 == dbType || DBType.SQLITE == dbType || DBType.CLICK_HOUSE == dbType) {
                return StringUtils.hasText(key) && "1".equals(key);
            } else {
                return StringUtils.hasText(key) && "PRI".equalsIgnoreCase(key);
            }
        }
    }
}
