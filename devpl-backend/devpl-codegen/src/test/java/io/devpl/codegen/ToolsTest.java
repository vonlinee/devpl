package io.devpl.codegen;

import com.alibaba.druid.DbType;
import io.devpl.codegen.parser.sql.DruidSqlParser;
import io.devpl.codegen.parser.sql.InsertColumn;
import io.devpl.codegen.parser.sql.InsertSqlParseResult;
import io.devpl.codegen.parser.sql.SqlParser;
import io.devpl.sdk.io.CustomLineIterator;
import io.devpl.sdk.util.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ToolsTest {

    /**
     * 修改INSERT SQL的某些列默认值
     * 比如主键默认设置为NULL
     * create_time设置为 NOW() 函数
     */
    @Test
    public void changeInsertSql() throws IOException {
        File file = new File("C:\\Users\\lenovo\\Desktop\\room_seat.sql");
        CustomLineIterator iterator = new CustomLineIterator(new FileReader(file), ";", true);
        SqlParser sqlParser = DruidSqlParser.createSqlParser(DbType.mysql.name());
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (StringUtils.startsWithIgnoreCase(line, "INSERT")) {
                InsertSqlParseResult result = sqlParser.parseInsertSql(DbType.mysql.name(), line);

                // 由sql解析结果重构sql

                System.out.println(result);
            }
        }
    }
}
