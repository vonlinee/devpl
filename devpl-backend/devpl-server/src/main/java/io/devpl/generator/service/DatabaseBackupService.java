package io.devpl.generator.service;

import java.nio.file.Path;

public interface DatabaseBackupService {

    boolean backup(String url, String username, String password, String databaseName, Path path);

    void saveHistory(String saveLocation);
}
