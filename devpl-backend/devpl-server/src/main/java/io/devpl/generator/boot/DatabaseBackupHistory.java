package io.devpl.generator.boot;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DatabaseBackupHistory {

    private Long id;

    private String saveLocation;

    private LocalDateTime backupTime;

    private LocalDateTime createTime;
}
