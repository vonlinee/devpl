package io.devpl.fxui.model;

import io.devpl.codegen.db.JDBCDriver;
import io.devpl.fxui.utils.DBUtils;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

/**
 * 数据库连接配置
 * 注意：连接配置不一定需要数据库，因此此处不存储数据库名称信息
 */
@Getter
@Setter
public class ConnectionConfig {

    private String id;
    /**
     * 连接名称: 唯一名称，可作为Map Key
     */
    private String connectionName;
    private String dbType;
    private JDBCDriver driverInfo;
    private String host;
    private String port;
    /**
     * 数据库名称
     */
    private String schema;
    private String dbName;
    private String username;
    private String password;
    private String encoding;

    private Properties properties;

    public String getConnectionUrl() {
        JDBCDriver driver = JDBCDriver.findByDriverClassName(dbType);
        String databaseName = schema;
        if (databaseName == null) {
            databaseName = "";
        }
        if (driver == null) {
            return null;
        }
        return driver.getConnectionUrl(host, port, databaseName, null);
    }

    public String getConnectionUrl(String databaseName) {
        JDBCDriver driver = JDBCDriver.findByDriverClassName(dbType);
        assert driver != null;
        return driver.getConnectionUrl(host, port, databaseName, properties);
    }

    public String getConnectionUrl(String databaseName, Properties properties) {
        JDBCDriver driver = JDBCDriver.findByDriverClassName(dbType);
        assert driver != null;
        return driver.getConnectionUrl(host, port, databaseName, properties);
    }

    public Connection getConnection(String databaseName, Properties properties) throws SQLException {
        String connectionUrl = getConnectionUrl(databaseName, properties);
        if (properties == null) {
            properties = new Properties();
            properties.put("user", username);
            properties.put("password", password);
            properties.put("serverTimezone", "UTC");
            properties.put("useUnicode", "true");
            properties.put("useSSL", "false");
            properties.put("characterEncoding", encoding);
        }
        return DBUtils.getConnection(connectionUrl, properties);
    }

    public Connection getConnection(String databaseName) throws SQLException {
        String connectionUrl = getConnectionUrl(databaseName);
        Properties properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);
        properties.put("serverTimezone", "UTC");
        properties.put("useUnicode", "true");
        properties.put("useSSL", "false");
        properties.put("characterEncoding", encoding);
        return DBUtils.getConnection(connectionUrl, properties);
    }

    /**
     * 获取数据库连接
     *
     * @return 数据库连接实例
     * @throws SQLException 获取连接失败
     */
    public Connection getConnection() throws SQLException {
        String connectionUrl = getConnectionUrl();
        Properties properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);
        properties.put("serverTimezone", "UTC");
        properties.put("useUnicode", "true");
        properties.put("useSSL", "false");
        properties.put("characterEncoding", encoding);
        return DBUtils.getConnection(connectionUrl, properties);
    }

    /**
     * 获取连接名称
     *
     * @return 连接名称，如果原本为空，则填充默认值：host_port，不取数据库名
     */
    public String getConnectionName() {
        if (this.connectionName == null || connectionName.isEmpty()) {
            this.connectionName = host + "_" + port;
        }
        return this.connectionName;
    }

    private String uniqueKey;

    public String getUniqueKey() {
        if (uniqueKey == null) {
            uniqueKey = connectionName;
        }
        return uniqueKey;
    }

    public JDBCDriver getDriver() {
        if (this.driverInfo == null) {
            this.driverInfo = JDBCDriver.findByDriverClassName(dbType);
        }
        return driverInfo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConnectionConfig) {
            return Objects.equals(this.getConnectionName(), ((ConnectionConfig) obj).getConnectionName());
        }
        return false;
    }
}
