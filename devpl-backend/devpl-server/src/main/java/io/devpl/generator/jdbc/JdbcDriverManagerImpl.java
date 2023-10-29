package io.devpl.generator.jdbc;

import com.baomidou.mybatisplus.generator.jdbc.JDBCDriver;
import io.devpl.generator.config.DbType;
import io.devpl.sdk.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 每种驱动类名仅支持1个版本
 * 某些驱动会在启动时自行注册，比如mysql
 */
@Component
public class JdbcDriverManagerImpl implements JdbcDriverManager, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(JdbcDriverManager.class);

    Map<String, JdbcDriverClassLoader> driverClassLoaderMap = new ConcurrentHashMap<>();
    Map<JDBCDriver, Class<?>> drivers = new HashMap<>();

    @Value("${devpl.driver.location}")
    private String driverLocation;

    @Override
    public boolean isRegisted(String driverClassName) {
        DriverManager.drivers().forEach(driver -> {
            System.out.println(driver.getClass());
        });
        return false;
    }

    @Override
    public void register(String driverClassName) {

    }

    @Override
    public void deregister(String driverClassName) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Path rootDir = Path.of(driverLocation);
        registerDrivers(rootDir);
    }

    /**
     * 启动时注册已经上传的驱动
     *
     * @param rootDir 驱动上传根目录
     */
    private void registerDrivers(Path rootDir) {
        // 数据库类型 - 版本 - 驱动jar包
        // 例如 mysql - 8.0.18 - mysql-connector-java-8.0.18.jar
        try (Stream<Path> dbTypeDirStream = Files.list(rootDir)) {
            dbTypeDirStream.filter(Files::isDirectory).forEach(dbTypeDir -> {
                logger.info("扫描{}", dbTypeDir.toAbsolutePath());
                // 数据库类型 小写
                DbType dbType = DbType.getValue(dbTypeDir.toFile().getName().toLowerCase(), null);
                if (dbType != null) {
                    try (Stream<Path> versionDirStream = Files.list(dbTypeDir)) {
                        versionDirStream.filter(Files::isDirectory).forEach(versionDir -> {
                            // 版本
                            try (Stream<Path> jarFileStream = Files.list(versionDir)) {
                                jarFileStream.filter(file -> Files.isRegularFile(file) && file.toString().endsWith(".jar")).forEach(driverJarFile -> {
                                    // 驱动jar包
                                    Driver driver;
                                    try (JdbcDriverClassLoader loader = new JdbcDriverClassLoader(FileUtils.toURL(driverJarFile))) {
                                        logger.info("扫描到驱动文件{}，尝试加载驱动类{}", driverJarFile.toAbsolutePath(), dbType.getDriverClassName());
                                        driver = loader.loadDriver(dbType.getDriverClassName());
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    if (driver != null) {
                                        try {
                                            DriverManager.registerDriver(driver);
                                        } catch (SQLException e) {
                                            logger.info("注册驱动类{}失败, 数据库类型{}", driver, dbType);
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                            } catch (IOException exception) {

                            }
                        });
                    } catch (IOException exception) {

                    }
                }
            });
        } catch (IOException exception) {

        }

        System.out.println(isRegisted(""));
    }
}
