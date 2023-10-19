package io.devpl.fxui.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceStatLoggerImpl;
import io.devpl.sdk.util.StringUtils;
import io.devpl.tookit.fxui.model.ConnectionConfig;
import io.devpl.tookit.fxui.model.ProjectConfiguration;
import io.devpl.fxui.utils.json.JSONUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 本应用所有的数据库操作都在此
 */
public class AppConfig {

    static final String URL = "jdbc:mysql://localhost:3306/devpl?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";

    static final String USERNAME = "root";
    static final String PASSWORD = "123456";

    public static final JdbcTemplate template = initJdbcTemplate();

    /**
     * TODO
     * @return JdbcTemplate
     */
    public static JdbcTemplate initJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        DruidDataSource dds = new DruidDataSource();
        dds.setUrl(URL);
        dds.setUsername(USERNAME);
        dds.setPassword(PASSWORD);
        dds.setDriverClassName("com.mysql.jdbc.Driver");

        DruidDataSourceStatLoggerImpl logger = new DruidDataSourceStatLoggerImpl();

        dds.setStatLogger(logger);
        jdbcTemplate.setDataSource(dds);
        return jdbcTemplate;
    }

//    /**
//     * TODO
//     * @return JdbcTemplate
//     */
//    public static JdbcTemplate initJdbcTemplate() {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate();
//        DruidDataSource dds = new DruidDataSource();
//        dds.setUrl("jdbc:sqlite:" + new File(new File("").getAbsolutePath(), "fxui/src/main/resources/db/devpl.db").getAbsolutePath());
//        dds.setDriverClassName("org.sqlite.JDBC");
//        jdbcTemplate.setDataSource(dds);
//        return jdbcTemplate;
//    }

    public static Connection getConnection() throws SQLException {
        return DBUtils.getConnection(URL, USERNAME, PASSWORD);
    }

    public static List<ConnectionConfig> listConnectionInfo() {
        try (Connection conn = getConnection();) {
            String sql = "select * from connection_config";
            ResultSet rs = DBUtils.executeQuery(conn, sql);
            List<ConnectionConfig> results = new ArrayList<>();
            while (rs.next()) {
                ConnectionConfig item = new ConnectionConfig();
                item.setId(rs.getString("id"));
                item.setPort(rs.getString("port"));
                item.setDbName(rs.getString("db_name"));
                item.setConnectionName(rs.getString("name"));
                item.setHost(rs.getString("host"));
                item.setDbType(rs.getString("db_type"));
                item.setUsername(rs.getString("username"));
                item.setPassword(rs.getString("password"));
                item.setEncoding("utf-8");
                results.add(item);
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据ID删除连接信息
     * @param connectionInfos 连接信息列表
     * @return 删除条数
     */
    public static int deleteConnectionById(List<ConnectionConfig> connectionInfos) {
        String sql = "DELETE FROM connection_config WHERE id IN ";
        StringJoiner stringJoiner = new StringJoiner(",", "(", ")");
        for (ConnectionConfig connectionInfo : connectionInfos) {
            stringJoiner.add("'" + connectionInfo.getId() + "'");
        }
        sql += stringJoiner;
        return template.update(sql);
    }

    public static void saveConnectionConfig(ConnectionConfig config) {
        String sql = "INSERT INTO connection_config " + "(id, name, host, port, db_type, db_name, username, password)\n" + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        if (!StringUtils.hasText(config.getId())) {
            config.setId(UUID.randomUUID().toString());
        }
        if (!StringUtils.hasText(config.getConnectionName())) {
            String connectionName = config.getHost() + "_" + config.getPort();
            config.setConnectionName(connectionName);
        }
    }

    public static List<ProjectConfiguration> listProjectConfigurations() {
        List<ProjectConfiguration> results = new ArrayList<>();
        List<Map<String, Object>> list = template.queryForList("select * from generator_config");
        for (Map<String, Object> map : list) {
            ProjectConfiguration item = new ProjectConfiguration();
            Object value = map.get("value");
            if (value != null) {
                results.add(JSONUtils.toObject(value.toString(), ProjectConfiguration.class));
            }
        }
        return results;
    }

    public static int saveProjectConfiguration(ProjectConfiguration projectConfiguration) {
        return template.update("insert into generator_config values(?, ?)", projectConfiguration.getName(), JSONUtils.toJSONString(projectConfiguration));
    }

    /**
     * @param oldName 配置名称
     * @param newName 配置名称
     * @param value   配置值
     */
    public static int updateProjectConfiguration(String oldName, String newName, String value) {
        String sql = "update generator_config set name = ?, value = ? where name = ?";
        return template.update(sql, newName, value, oldName);
    }
}
