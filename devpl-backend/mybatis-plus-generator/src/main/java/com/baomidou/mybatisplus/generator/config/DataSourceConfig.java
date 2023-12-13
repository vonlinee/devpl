package com.baomidou.mybatisplus.generator.config;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.querys.DbQueryDecorator;
import com.baomidou.mybatisplus.generator.jdbc.DBType;
import com.baomidou.mybatisplus.generator.query.AbstractDatabaseIntrospector;
import com.baomidou.mybatisplus.generator.query.DatabaseIntrospector;
import com.baomidou.mybatisplus.generator.query.DefaultDatabaseIntrospector;
import com.baomidou.mybatisplus.generator.util.JdbcUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库配置
 *
 * @author YangHu, hcl, hubin
 * @since 2016/8/30
 */
public class DataSourceConfig {
    protected final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);
    /**
     * 数据库连接属性
     *
     * @since 3.5.3
     */
    private final Map<String, String> connectionProperties = new HashMap<>();
    /**
     * 数据库信息查询
     */
    private DatabaseDialect dbQuery;

    /**
     * schemaName
     */
    private String schemaName;

    /**
     * 类型转换
     */
    private ITypeConvert typeConvert;

    /**
     * 关键字处理器
     *
     * @since 3.3.2
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
     *
     * @since 3.5.0
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
     *
     * @see DefaultDatabaseIntrospector 默认查询方式
     * SQLQuery SQL语句查询方式，配合{@link #typeConvert} 使用
     * @since 3.5.3
     */
    private Class<? extends AbstractDatabaseIntrospector> databaseQueryClass = DefaultDatabaseIntrospector.class;

    private DataSourceConfig() {
    }

    /**
     * 获取数据库查询
     */
    public DatabaseDialect getDbQuery() {
        return dbQuery;
    }

    public ITypeConvert getTypeConvert() {
        return typeConvert;
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
     * @see DbQueryDecorator#getConnection()
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
                        this.processProperties(properties);
                        this.connection = DriverManager.getConnection(url, properties);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    private void processProperties(Properties properties) {
        if (this.databaseQueryClass.getName().equals(DefaultDatabaseIntrospector.class.getName())) {
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
        }
    }

    /**
     * 获取数据库默认schema
     *
     * @return 默认schema
     * @since 3.5.0
     */
    @Nullable
    protected String getDefaultSchema() {
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
    public Class<? extends DatabaseIntrospector> getDatabaseQueryClass() {
        return databaseQueryClass;
    }

    /**
     * 数据库配置构建者
     *
     * @author nieqiurong 2020/10/10.
     * @since 3.5.0
     */
    public static class Builder implements GenericBuilder<DataSourceConfig> {

        private final DataSourceConfig dataSourceConfig;

        private Builder() {
            this.dataSourceConfig = new DataSourceConfig();
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
            if (StringUtils.isBlank(url)) {
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
        public Builder dbQuery(@NotNull DatabaseDialect dbQuery) {
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
         * 设置类型转换器
         *
         * @param typeConvert 类型转换器
         * @return this
         */
        public Builder typeConvert(@NotNull ITypeConvert typeConvert) {
            this.dataSourceConfig.typeConvert = typeConvert;
            return this;
        }

        /**
         * 设置数据库关键字处理器
         *
         * @param keyWordsHandler 关键字处理器
         * @return this
         */
        public Builder keyWordsHandler(@NotNull IKeyWordsHandler keyWordsHandler) {
            this.dataSourceConfig.keyWordsHandler = keyWordsHandler;
            return this;
        }

        /**
         * 指定数据库查询方式
         *
         * @param databaseQueryClass 查询类
         * @return this
         * @since 3.5.3
         */
        public Builder databaseQueryClass(@NotNull Class<? extends AbstractDatabaseIntrospector> databaseQueryClass) {
            this.dataSourceConfig.databaseQueryClass = databaseQueryClass;
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
        @Override
        public DataSourceConfig build() {
            return this.dataSourceConfig;
        }
    }
}
