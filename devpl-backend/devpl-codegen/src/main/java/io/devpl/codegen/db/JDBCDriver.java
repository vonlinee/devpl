package io.devpl.codegen.db;

import java.util.Map;
import java.util.Properties;

/**
 * 数据库驱动类型
 * 连接URL格式：jdbc:mysql://[host:port],[host:port].../[database][?参数名1][=参数值1][&参数名2][=参数值2]...
 *
 * @see DBTypeEnum
 */
public enum JDBCDriver implements org.apache.ddlutils.platform.JDBCDriver {

    MYSQL5("com.mysql.jdbc.Driver", "mysql", "MySQL 5"),
    MYSQL8("com.mysql.cj.jdbc.Driver", MYSQL5.subProtocol, "MySQL 8"),

    // Oracle Database
    ORACLE("oracle.jdbc.OracleDriver", "oracle:thin", "Oracle 11 thin") {
        @Override
        public String getConnectionUrlPrefix(String hostname, int port, String databaseName) {
            return JDBC_PROTOCOL + ":" + subProtocol + ":@//" + hostname + ":" + port + "/" + databaseName;
        }
    },
    ORACLE_12C(ORACLE.driverClassName, ORACLE.subProtocol, "Oracle 12 thin") {
        @Override
        public String getConnectionUrlPrefix(String hostname, int port, String databaseName) {
            return ORACLE.getConnectionUrlPrefix(hostname, port, databaseName);
        }
    },
    POSTGRE_SQL("org.postgresql.Driver", "postgresql"),
    SQL_SERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver", "sqlserver"),
    SQLITE("org.sqlite.JDBC", "sqlite");

    private static final String JDBC_PROTOCOL = "jdbc";

    /**
     * 驱动类全类名
     */
    protected final String driverClassName;

    /**
     * 子协议
     */
    protected final String subProtocol;

    /**
     * 描述信息
     */
    protected String description;

    JDBCDriver(String driverClassName, String subProtocol) {
        this.driverClassName = driverClassName;
        this.subProtocol = subProtocol;
    }

    JDBCDriver(String driverClassName, String subProtocol, String description) {
        this(driverClassName, subProtocol);
        this.description = description;
    }

    public static JDBCDriver findByDriverClassName(String driverClassName) {
        if (driverClassName == null || driverClassName.isBlank()) {
            return null;
        }
        for (JDBCDriver driver : values()) {
            if (driver.getDriverClassName().equalsIgnoreCase(driverClassName)) {
                return driver;
            }
        }
        return null;
    }

    public static String[] supportedDriverNames() {
        String[] names = new String[values().length];
        JDBCDriver[] drivers = values();
        for (int i = 0; i < drivers.length; i++) {
            names[i] = drivers[i].name();
        }
        return names;
    }

    public static JDBCDriver getByName(String name) {
        return getByName(name, true, null);
    }

    public static JDBCDriver getByName(String name, JDBCDriver defaultValue) {
        return getByName(name, true, defaultValue);
    }

    /**
     * 根据名称搜索
     *
     * @param name          枚举实例名称
     * @param caseSensitive 大小写敏感
     * @param defaultValue  默认值
     * @return JDBCDriver实例
     */
    public static JDBCDriver getByName(String name, boolean caseSensitive, JDBCDriver defaultValue) {
        if (caseSensitive) {
            for (JDBCDriver driver : values()) {
                if (driver.name().equals(name)) {
                    return driver;
                }
            }
        } else {
            for (JDBCDriver driver : values()) {
                if (driver.name().equalsIgnoreCase(name)) {
                    return driver;
                }
            }
        }
        return defaultValue;
    }

    /**
     * 协议名//[host name][/database name][username and password]
     * 获取JDBC URL连接字符串
     *
     * @param hostname     主机IP
     * @param port         端口号
     * @param databaseName 数据库名，如果为空则不进行拼接
     * @return JDBC URL连接字符串
     */
    public String getConnectionUrl(String hostname, String port, String databaseName, Properties props) {
        return appendConnectionUrlParams(getConnectionUrlPrefix(hostname, parsePortNumber(port), databaseName), props);
    }

    @Override
    public String getConnectionUrl(String hostname, int port, String databaseName, Properties props) {
        port = port < 0 ? getDefaultPort() : port;
        return appendConnectionUrlParams(getConnectionUrlPrefix(hostname, port, databaseName), props);
    }

    public int parsePortNumber(String port) {
        int portNum;
        if (port == null || port.isEmpty()) {
            portNum = 3306;
        } else {
            try {
                portNum = Integer.parseInt(port);
            } catch (Exception exception) {
                portNum = 3306;
            }
        }
        return portNum;
    }

    /**
     * 拼接URL的主要部分
     *
     * @param hostname     主机IP
     * @param port         端口号
     * @param databaseName 数据库名
     * @return URL的主要部分
     */
    public String getConnectionUrlPrefix(String hostname, int port, String databaseName) {
        String connectionUrl = JDBC_PROTOCOL + ":" + subProtocol + "://" + hostname + ":" + port;
        if (databaseName != null && !databaseName.isEmpty()) {
            connectionUrl += "/" + databaseName;
        }
        return connectionUrl;
    }

    /**
     * 追加JDBC URL连接参数
     *
     * @param url        JDBC URL
     * @param properties 连接参数配置
     * @return 完整的JDBC URL
     */
    protected String appendConnectionUrlParams(String url, Properties properties) {
        if (properties == null || properties.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url).append("?");
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDriverClassName() {
        return driverClassName;
    }

    @Override
    public String getSubProtocol() {
        return subProtocol;
    }
}
