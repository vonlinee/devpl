package com.baomidou.mybatisplus.generator.jdbc;

import java.util.Map;
import java.util.Properties;

/**
 * 数据库驱动类型
 * 连接URL格式：jdbc:mysql://[host:port],[host:port].../[database][?参数名1][=参数值1][&参数名2][=参数值2]...
 */
public enum JDBCDriver {

    MYSQL5("com.mysql.jdbc.Driver", "mysql", "MySQL 5"),
    MYSQL8("com.mysql.cj.jdbc.Driver", "mysql", "MySQL 8"),
    ORACLE("oracle.jdbc.OracleDriver", "oracle:thin", "Oracle 11 thin") {
        @Override
        protected String getConnectionUrlPrefix(String hostname, int port, String databaseName) {
            return JDBC_PROTOCOL + ":" + subProtocol + ":@//" + hostname + ":" + port + "/" + databaseName;
        }
    },
    POSTGRE_SQL("org.postgresql.Driver", "postgresql"),
    SQL_SERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver", "sqlserver"),
    SQLITE("org.sqlite.JDBC", "sqlite");

    public static final String JDBC_PROTOCOL = "jdbc";
    // 驱动类全类名
    protected final String driverClassName;
    // 子协议
    protected final String subProtocol;
    // 描述信息
    protected String description;

    /**
     * 协议名//[host name][/database name][username and password]
     * 获取JDBC URL连接字符串
     * @param hostname     主机IP
     * @param port         端口号
     * @param databaseName 数据库名
     * @return JDBC URL连接字符串
     */
    public String getConnectionUrl(String hostname, String port, String databaseName, Properties props) {
        int portNum;
        if (port == null || port.length() == 0) {
            portNum = 3306;
        } else {
            portNum = Integer.parseInt(port);
        }
        return appendConnectionUrlParams(getConnectionUrlPrefix(hostname, portNum, databaseName), props);
    }

    /**
     * 拼接URL的主要部分
     * @param hostname     主机IP
     * @param port         端口号
     * @param databaseName 数据库名
     * @return URL的主要部分
     */
    protected String getConnectionUrlPrefix(String hostname, int port, String databaseName) {
        String connectionUrl = JDBC_PROTOCOL + ":" + subProtocol + "://" + hostname + ":" + port;
        if (databaseName != null && databaseName.length() != 0) {
            connectionUrl += "/" + databaseName;
        }
        return connectionUrl;
    }

    /**
     * 追加JDBC URL连接参数
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

    JDBCDriver(String driverClassName, String subProtocol) {
        this.driverClassName = driverClassName;
        this.subProtocol = subProtocol;
    }

    JDBCDriver(String driverClassName, String subProtocol, String description) {
        this(driverClassName, subProtocol);
        this.description = description;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public static String[] supportedDbNames() {
        String[] names = new String[values().length];
        JDBCDriver[] drivers = values();
        for (int i = 0; i < drivers.length; i++) {
            names[i] = drivers[i].name();
        }
        return names;
    }

    public static JDBCDriver valueOfDriverName(String driverName) {
        try {
            return JDBCDriver.valueOf(driverName.toUpperCase());
        } catch (Exception exception) {
            return null;
        }
    }

    public String getSubProtocol() {
        return subProtocol;
    }
}
