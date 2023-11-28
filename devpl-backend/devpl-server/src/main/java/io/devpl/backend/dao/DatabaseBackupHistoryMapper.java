package io.devpl.backend.dao;

import io.devpl.backend.boot.DatabaseBackupHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DatabaseBackupHistoryMapper {

    @Select("select @@basedir")
    String selectMySQLBaseDir();

    @Insert(value = "INSERT INTO database_backup_history VALUE (NULL, #{param.saveLocation}, #{param.backupTime}, now())")
    int insert(@Param("param") DatabaseBackupHistory history);
}
