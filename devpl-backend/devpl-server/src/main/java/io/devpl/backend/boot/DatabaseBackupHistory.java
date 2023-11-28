package io.devpl.backend.boot;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DatabaseBackupHistory {

    private Long id;

    private String saveLocation;

    private LocalDateTime backupTime;

    private LocalDateTime createTime;
}
