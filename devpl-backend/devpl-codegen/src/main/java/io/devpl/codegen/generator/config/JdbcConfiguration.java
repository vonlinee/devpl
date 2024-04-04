package io.devpl.codegen.generator.config;

import io.devpl.codegen.db.DBType;
import io.devpl.codegen.db.IKeyWordsHandler;
import io.devpl.codegen.db.query.AbstractDbQuery;
import io.devpl.codegen.jdbc.JdbcDatabaseMetadataReader;
import io.devpl.codegen.jdbc.JdbcUtils;
import io.devpl.codegen.jdbc.meta.DatabaseMetadataReader;
import io.devpl.codegen.util.InternalUtils;
import io.devpl.sdk.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库配置
 */
public class JdbcConfiguration extends ConfigurationHolder {

    /**
     * 数据库连接属性
     */
    private final Map<String, String> connectionProperties = new HashMap<>();
    /**
     * 数据库信息查询
     */
    private AbstractDbQuery dbQuery;
    /**
     * schemaName
     */
    private String schemaName;
    /**
     * 类型转换
     */
    private TypeConverter typeConvert;
    /**
     * 关键字处理器
     */
    private IKeyWordsHandler keyWordsHandler;
    /**
     * 驱动连接的URL
     */
    private String url;

    /**
     * 数据库连接用户名
     */
    private String username;

    /**
     * 数据库连接密码
     */
    private String password;

    /**
     * 数据源实例
     */
    private DataSource dataSource;

    /**
     * 数据库连接
     *
     * @since 3.5.0
     */
    private Connection connection;
    /**
     * 查询方式
     */
    private Class<? extends DatabaseMetadataReader> databaseQueryClass = JdbcDatabaseMetadataReader.class;

    private JdbcConfiguration() {
    }

    /**
     * 获取数据库查询
     */
    public AbstractDbQuery getDbQuery() {
        return dbQuery;
    }

    /**
     * 判断数据库类型
     *
     * @return 类型枚举值
     */
    @NotNull
    public DBType getDbType() {
        return JdbcUtils.getDbType(this.url);
    }

    /**
     * 创建数据库连接对象 这方法建议只调用一次，毕竟只是代码生成，用一个连接就行。
     *
     * @return Connection
     */
    @NotNull
    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            } else {
                synchronized (this) {
                    if (dataSource != null) {
                        connection = dataSource.getConnection();
                    } else {
                        Properties properties = new Properties();
                        connectionProperties.forEach(properties::setProperty);
                        properties.put("user", username);
                        properties.put("password", password);
                        // 使用元数据查询方式时，有些数据库需要增加属性才能读取注释
                        switch (this.getDbType()) {
                            case MYSQL -> {
                                properties.put("remarks", "true");
                                properties.put("useInformationSchema", "true");
                            }
                            case ORACLE -> {
                                properties.put("remarks", "true");
                                properties.put("remarksReporting", "true");
                            }
                        }
                        this.connection = DriverManager.getConnection(url, properties);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    /**
     * 获取数据库默认schema
     *
     * @return 默认schema
     */
    @Nullable
    protected String getDefaultSchemaName() {
        DBType dbType = getDbType();
        String schema = null;
        if (DBType.POSTGRE_SQL == dbType) {
            // pg 默认 schema=public
            schema = "public";
        } else if (DBType.KINGBASE_ES == dbType) {
            // king base 默认 schema=PUBLIC
            schema = "PUBLIC";
        } else if (DBType.DB2 == dbType) {
            // db2 默认 schema=current schema
            schema = "current schema";
        } else if (DBType.ORACLE == dbType) {
            // oracle 默认 schema=username
            schema = this.username.toUpperCase();
        }
        return schema;
    }

    @Nullable
    public String getSchemaName() {
        return schemaName;
    }

    @Nullable
    public IKeyWordsHandler getKeyWordsHandler() {
        return keyWordsHandler;
    }

    @NotNull
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    @NotNull
    public Class<? extends DatabaseMetadataReader> getDatabaseQueryClass() {
        return databaseQueryClass;
    }

    public static Builder builder(Properties properties) {
        return new Builder(properties);
    }

    /**
     * 数据库配置构建者
     *
     * @author nieqiurong 2020/10/10.
     * @since 3.5.0
     */
    public static class Builder {

        private final JdbcConfiguration dataSourceConfig;

        private Builder() {
            this.dataSourceConfig = new JdbcConfiguration();
        }

        private Builder(Properties properties) {
            this.dataSourceConfig = new JdbcConfiguration();
            initialize(properties);
        }

        private void initialize(Properties properties) {
            String url = (String) properties.get("url");
            String username = (String) properties.get("username");
            String password = (String) properties.get("password");
            this.dataSourceConfig.url = InternalUtils.requireNonEmpty(url, "url is empty");
            this.dataSourceConfig.username = InternalUtils.requireNonEmpty(username, "username is empty");
            this.dataSourceConfig.password = InternalUtils.requireNonEmpty(password, "password is empty");
        }

        /**
         * 构造初始化方法
         *
         * @param url      数据库连接地址
         * @param username 数据库账号
         * @param password 数据库密码
         */
        public Builder(@NotNull String url, String username, String password) {
            this();
            if (!StringUtils.hasText(url)) {
                throw new RuntimeException("无法创建文件，请正确输入 url 配置信息！");
            }
            this.dataSourceConfig.url = url;
            this.dataSourceConfig.username = username;
            this.dataSourceConfig.password = password;
        }

        /**
         * 构造初始化方法
         *
         * @param dataSource 外部数据源实例
         */
        public Builder(@NotNull DataSource dataSource) {
            this();
            this.dataSourceConfig.dataSource = dataSource;
            try {
                Connection conn = dataSource.getConnection();
                this.dataSourceConfig.url = conn.getMetaData().getURL();
                try {
                    this.dataSourceConfig.schemaName = conn.getSchema();
                } catch (Throwable exception) {
                    // ignore  如果使用低版本的驱动，这里由于是1.7新增的方法，所以会报错，如果驱动太低，需要自行指定了。
                }
                this.dataSourceConfig.connection = conn;
                this.dataSourceConfig.username = conn.getMetaData().getUserName();
            } catch (SQLException ex) {
                throw new RuntimeException("构建数据库配置对象失败!", ex);
            }
        }

        /**
         * 设置数据库查询实现
         *
         * @param dbQuery 数据库查询实现
         * @return this
         */
        public Builder dbQuery(@NotNull AbstractDbQuery dbQuery) {
            this.dataSourceConfig.dbQuery = dbQuery;
            return this;
        }

        /**
         * 设置数据库schema
         *
         * @param schemaName 数据库schema
         * @return this
         */
        public Builder schema(@NotNull String schemaName) {
            this.dataSourceConfig.schemaName = schemaName;
            return this;
        }

        /**
         * 增加数据库连接属性
         *
         * @param key   属性名
         * @param value 属性值
         * @return this
         * @since 3.5.3
         */
        public Builder addConnectionProperty(@NotNull String key, @NotNull String value) {
            this.dataSourceConfig.connectionProperties.put(key, value);
            return this;
        }

        /**
         * 构建数据库配置
         *
         * @return 数据库配置
         */
        public JdbcConfiguration build() {
            return this.dataSourceConfig;
        }
    }
}
