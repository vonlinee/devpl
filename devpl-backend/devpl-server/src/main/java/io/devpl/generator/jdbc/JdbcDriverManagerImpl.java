package io.devpl.generator.jdbc;

import com.baomidou.mybatisplus.generator.jdbc.DBType;
import com.baomidou.mybatisplus.generator.jdbc.JDBCDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
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

    private final Map<JDBCDriver, DriverInfo> drivers = new ConcurrentHashMap<>();

    @Value("${devpl.driver.location}")
    private String driverLocation;

    @Override
    public Connection getConnection(String driverClassName, String url, String username, String password, Properties properties) {
        JDBCDriver driveType = JDBCDriver.findByDriverClassName(driverClassName);
        if (driveType != null) {
            DriverInfo driverInfo = drivers.get(driveType);
            if (driverInfo == null) {
                // 驱动未注册
                throw new CannotGetJdbcConnectionException("驱动未注册");
            }
            Properties props = properties == null ? new Properties() : properties;
            // 不填默认使用本机操作系统用户名
            props.setProperty("user", username);
            props.setProperty("password", password);
            try {
                if (driverInfo.driver != null) {
                    Connection connection = driverInfo.driver.connect(url, props);
                    if (connection != null) {
                        log.info("获取连接成功 驱动文件{} 连接{}", driverInfo.filename, connection);
                    }
                    return connection;
                }
            } catch (Throwable e) {
                if (e instanceof SQLException sqlException) {
                    throw new CannotGetJdbcConnectionException("获取连接失败", sqlException);
                }
                log.error("获取连接失败 {}", driverInfo.filename, e);
            }
        }
        return null;
    }

    @Override
    public boolean isRegisted(String driverClassName) {
        JDBCDriver driver = JDBCDriver.findByDriverClassName(driverClassName);
        if (driver == null) {
            return false;
        }
        return drivers.containsKey(driver);
    }

    @Override
    public void deregister(String driverClassName) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        File driverLocationDir = new File(driverLocation);
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
        File[] files = rootDir.listFiles(file -> file.isDirectory() && DBType.getValue(file.getName(), null) != null);
        if (files == null || files.length == 0) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            // 数据库类型目录
            String dbTypeName = files[i].getName();
            File[] versionDirs = files[i].listFiles(File::isDirectory);
            if (versionDirs == null || versionDirs.length == 0) {
                continue;
            }
            for (int v = 0; v < versionDirs.length; v++) {
                // 版本
                final String version = versionDirs[v].getName();
                // 同一个版本应只有一个驱动文件
                File[] driverFiles = versionDirs[v].listFiles(File::isFile);
                if (driverFiles == null || driverFiles.length == 0) {
                    continue;
                }
                final File driverJarFile = driverFiles[0];

                // 判断要加载的驱动全限定类名
                JDBCDriver driverType = getBestMatchedDriverType(dbTypeName, version, driverJarFile);
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
    private JDBCDriver getBestMatchedDriverType(String dbTypeName, String version, File driverJarFile) {
        DBType[] dbTypes = DBType.values();
        for (DBType dbType : dbTypes) {
            if (dbType.name().equalsIgnoreCase(dbTypeName)) {
                if (dbType == DBType.MYSQL && version.startsWith("8")) {
                    return dbType.getDriver(1);
                }
                return dbType.getDriver();
            }
        }
        return null;
    }
}
