package io.devpl.backend.service.impl;

import io.devpl.backend.boot.DatabaseBackupHistory;
import io.devpl.backend.dao.DatabaseBackupHistoryMapper;
import io.devpl.backend.service.DatabaseBackupService;
import io.devpl.sdk.io.FileUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class DatabaseBackupServiceImpl implements DatabaseBackupService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseBackupServiceImpl.class);

    @Resource
    DatabaseBackupHistoryMapper databaseBackupHistoryMapper;

    @Override
    public boolean backup(String url, String username, String password, String databaseName, Path path) {
        String baseDir = databaseBackupHistoryMapper.selectMySQLBaseDir();
        path = FileUtils.createFileQuietly(path, true);
        if (path == null) {
            return false;
        }
        FileUtils.clean(path.getParent().toFile());
        String saveLocation = path.toAbsolutePath().toString();
        String command = "%sbin/mysqldump -u%s -p%s %s -r %s".formatted(baseDir, username, password, databaseName, saveLocation);
        logger.info("mysqldump with command: {}", command);

        DatabaseBackupHistory history = new DatabaseBackupHistory();
        history.setSaveLocation(saveLocation);
        history.setBackupTime(LocalDateTime.now());
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            logger.error("数据库备份失败", e);
            history.setSaveLocation("备份失败");
        } finally {
            try {
                databaseBackupHistoryMapper.insert(history);
            } catch (Exception exception) {
                logger.error("保存数据库备份记录失败", exception);
            }
        }
        return true;
    }

    @Override
    public void saveHistory(String saveLocation) {
        DatabaseBackupHistory history = new DatabaseBackupHistory();
        history.setSaveLocation(saveLocation);
        history.setBackupTime(LocalDateTime.now());
        databaseBackupHistoryMapper.insert(history);
    }
}
