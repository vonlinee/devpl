package io.devpl.generator.config;

import io.devpl.generator.config.query.*;
import io.devpl.generator.entity.GenDataSource;
import io.devpl.generator.utils.DbUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 代码生成器 数据源
 */
@Data
@Slf4j
public class DataSourceInfo {
    /**
     * 数据源ID
     */
    private Long id;
    /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * 数据库URL
     */
    private String connUrl;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    private AbstractQuery dbQuery;

    private Connection connection;

    public DataSourceInfo(GenDataSource entity) {
        this.id = entity.getId();
        this.dbType = DbType.getValue(entity.getDbType());
        this.connUrl = entity.getConnUrl();
        this.username = entity.getUsername();
        this.password = entity.getPassword();

        if (dbType == DbType.MySQL) {
            this.dbQuery = new MySqlQuery();
        } else if (dbType == DbType.Oracle) {
            this.dbQuery = new OracleQuery();
        } else if (dbType == DbType.PostgreSQL) {
            this.dbQuery = new PostgreSqlQuery();
        } else if (dbType == DbType.SQLServer) {
            this.dbQuery = new SQLServerQuery();
        } else if (dbType == DbType.DM) {
            this.dbQuery = new DmQuery();
        } else if (dbType == DbType.Clickhouse) {
            this.dbQuery = new ClickHouseQuery();
        }
    }

    public DataSourceInfo(Connection connection) throws SQLException {
        this.id = 0L;
        this.dbType = DbType.getValue(connection.getMetaData().getDatabaseProductName());
        if (dbType == DbType.MySQL) {
            this.dbQuery = new MySqlQuery();
        } else if (dbType == DbType.Oracle) {
            this.dbQuery = new OracleQuery();
        } else if (dbType == DbType.PostgreSQL) {
            this.dbQuery = new PostgreSqlQuery();
        } else if (dbType == DbType.SQLServer) {
            this.dbQuery = new SQLServerQuery();
        } else if (dbType == DbType.DM) {
            this.dbQuery = new DmQuery();
        } else if (dbType == DbType.Clickhouse) {
            this.dbQuery = new ClickHouseQuery();
        }
        this.connection = connection;
    }

    /**
     * 获取数据库连接
     * @return JDBC Connection 对象实例
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                return DbUtils.getConnection(this);
            }
            return connection;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public void closeConnection() {
        if (connection == null) {
            return;
        }
        try {
            if (!connection.isClosed()) {
                connection.close(); // 关闭连接
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }
}
