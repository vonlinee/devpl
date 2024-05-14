package io.devpl.backend.utils;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;

public class DruidSqlFormatter implements SqlFormatter {

    @Override
    public String format(String dialect, String source) {
        DbType dbType = DbType.valueOf(dialect);
        return SQLUtils.format(source, dbType);
    }
}
