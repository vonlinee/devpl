package io.devpl.backend.utils;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DruidSqlFormatter implements SqlFormatter {

    @Override
    public String getId() {
        return "druid";
    }

    @Override
    public String format(String dialect, String source) {
        try {
            DbType dbType = DbType.valueOf(dialect.toLowerCase());
            return SQLUtils.format(source, dbType);
        } catch (Throwable throwable) {
            log.error("failed to format sql", throwable);
            return source;
        }
    }
}
