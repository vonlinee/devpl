package io.devpl.backend.jdbc;

import io.devpl.sdk.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ddlutils.platform.BuiltinDatabaseType;
import org.apache.ddlutils.platform.BuiltinDriverType;
import org.apache.ddlutils.platform.DriverType;
import org.apache.ddlutils.platform.JdbcDriverManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 每种驱动类名仅支持1个版本
 * 某些驱动会在启动时自行注册，比如mysql
 */
@Slf4j
@Component
public class JdbcDriverManagerImpl implements JdbcDriverManager, InitializingBean {

    private final Map<DriverType, DriverInfo> drivers = new ConcurrentHashMap<>();

    @Value("${devpl.db.driver.location:}")
    private String driverLocation;

    private Properties prepareConnectionProperties(String username, String password, Properties properties) {
        properties = properties == null ? new Properties() : properties;
        // 不填默认使用本机操作系统用户名
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        return properties;
    }

    @Override
    public Connection getConnection(String driverClassName, String jdbcUrl, String username, String password, Properties properties) throws SQLException {
        BuiltinDriverType driveType = BuiltinDriverType.findByDriverClassName(driverClassName);
        Connection connection = null;
        if (driveType != null) {
            DriverInfo driverInfo = drivers.get(driveType);
            if (driverInfo == null) {
                // 驱动未注册
                throw new SQLException("驱动" + driverClassName + "未注册");
            }
            try {
                if (driverInfo.driver != null) {
                    connection = driverInfo.driver.connect(jdbcUrl, prepareConnectionProperties(username, password, properties));
                    if (connection != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("获取连接成功 驱动文件{} 连接{}", driverInfo.filename, connection);
                        }
                    }
                }
            } catch (Throwable e) {
                if (e instanceof SQLException) {
                    throw e;
                }
                throw new SQLException(e.getMessage(), e);
            }
        }
        return connection;
    }

    @Override
    public boolean isRegistered(String driverClassName) {
        BuiltinDriverType driver = BuiltinDriverType.findByDriverClassName(driverClassName);
        if (driver == null) {
            return false;
        }
        return drivers.containsKey(driver);
    }

    @Override
    public void deregister(String driverClassName) {

    }

    @Override
    public void afterPropertiesSet() {
        String absolutePath = new File("").getAbsolutePath();
        File driverLocationDir;
        if (!StringUtils.hasText(driverLocation)) {
            driverLocationDir = new File(absolutePath, "db/drivers");
        } else {
            driverLocationDir = new File(driverLocation);
        }
        if (!driverLocationDir.isDirectory()) {
            log.error("驱动配置错误");
        }
        if (!driverLocationDir.exists()) {
            log.error("驱动配置错误, 目录不存在");
        } else {
            registerDrivers(driverLocationDir);
        }
    }

    /**
     * 启动时注册已经上传的驱动
     *
     * @param rootDir 驱动上传根目录
     */
    private void registerDrivers(File rootDir) {
        // 数据库类型 - 版本 - 驱动jar包
        // 例如 mysql - 8.0.18 - mysql-connector-java-8.0.18.jar
        File[] files = rootDir.listFiles(file -> file.isDirectory() && BuiltinDatabaseType.getValue(file.getName(), null) != null);
        if (files == null) {
            return;
        }
        for (File file : files) {
            // 数据库类型目录
            String dbTypeName = file.getName();
            File[] versionDirs = file.listFiles(File::isDirectory);
            if (versionDirs == null) {
                continue;
            }
            for (File versionDir : versionDirs) {
                // 版本
                final String version = versionDir.getName();
                // 同一个版本应只有一个驱动文件
                File[] driverFiles = versionDir.listFiles(File::isFile);
                if (driverFiles == null || driverFiles.length == 0) {
                    continue;
                }
                final File driverJarFile = driverFiles[0];

                // 判断要加载的驱动全限定类名
                DriverType driverType = getBestMatchedDriverType(dbTypeName, version, driverJarFile);
                if (driverType == null) {
                    continue;
                }
                String driverClassName = driverType.getDriverClassName();

                // 驱动jar包
                Driver driver = null;
                try (JdbcDriverClassLoader loader = new JdbcDriverClassLoader(driverJarFile.toURI().toURL())) {
                    driver = loader.loadDriver(driverClassName);
                } catch (IOException e) {
                    log.error("加载驱动失败", e);
                }
                if (driver == null) {
                    continue;
                }
                try {
                    DriverManager.registerDriver(driver);
                    log.info("注册驱动类{}成功", driver);
                } catch (SQLException e) {
                    log.error("注册驱动类{}失败", driver);
                }
                drivers.put(driverType, new DriverInfo(driver, version, driverJarFile.getAbsolutePath(), driverType));
            }
        }
    }

    /**
     * 获取最匹配的驱动类的全限定类名
     *
     * @param dbTypeName    数据库类型名称
     * @param version       版本号
     * @param driverJarFile 驱动jar文件
     * @return 驱动类的全限定类名
     */
    private DriverType getBestMatchedDriverType(String dbTypeName, String version, File driverJarFile) {
        BuiltinDatabaseType[] dbTypes = BuiltinDatabaseType.values();
        for (BuiltinDatabaseType dbType : dbTypes) {
            if (dbType.name().equalsIgnoreCase(dbTypeName)) {
                if (dbType == BuiltinDatabaseType.MYSQL && version.startsWith("8")) {
                    return dbType.getSupportedDriverType(1);
                }
                return dbType.getSupportedDriverType(0);
            }
        }
        return null;
    }
}
