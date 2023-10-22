package io.devpl.generator.boot;

import io.devpl.generator.service.DatabaseBackupService;
import io.devpl.generator.utils.DateTimeUtils;
import io.devpl.generator.utils.DbUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;

/**
 * MySQL数据备份
 */
@Component
public class MySQLDatabaseDumper implements CommandLineRunner {

    static final Logger logger = LoggerFactory.getLogger(MySQLDatabaseDumper.class);

    @Resource
    DatabaseBackupService databaseBackupService;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public void run(String... args) throws Exception {
        String databaseName = DbUtils.getDatabaseNameFromConnectionUrl(url);
        Path path = Path.of(new File("").getAbsolutePath(), "db/backup", databaseName + "-" + DateTimeUtils.nowForFilename() + ".sql");
        boolean result = databaseBackupService.backup(url, username, password, databaseName, path);
        if (result) {
            logger.info("数据库备份成功 {}", path.toAbsolutePath());
        }
    }
}
