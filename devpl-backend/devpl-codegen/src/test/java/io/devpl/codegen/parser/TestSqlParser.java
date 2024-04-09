package io.devpl.codegen.parser;

import com.alibaba.druid.DbType;
import io.devpl.codegen.parser.sql.*;
import org.junit.Test;

public class TestSqlParser {

    @Test
    public void test1() {

    }

    @Test
    public void testParseSelectSql() {
        String sql = """
            SELECT seat.id,
                   seat.`row`,
                   seat.col,
                   seat.seat_name,
                   seat.is_disabled,
                   arrstu.student_id,
                   arrstu.user_id,
                   arrstu.user_name,
                   arrstu.class_id,
                   arrstu.class_name
            FROM room_seat seat
                     left join (select arr.room_id,
                                       arr.seat_id,
                                       arr.exam_student_id as student_id,
                                       stu.student_id      as user_id,
                                       stu.student_name    as user_name,
                                       stu.class_id,
                                       stu.class_name,
                                       stu.delay_confirmed
                                from student_arrangement arr
                                         LEFT JOIN exam_student stu ON arr.exam_student_id = stu.id
                                where arr.exam_id = #{examId}
                                  and arr.session_id = #{sessionId}
                                  and arr.room_id = #{roomId}) as arrstu on seat.id = arrstu.seat_id
            where seat.room_id = #{roomId}
            and seat.is_deleted = 0
                """;

        SqlParser parser = DruidSqlParser.createSqlParser(DbType.mysql.name());

        SelectSqlParseResult result = parser.parseSelectSql(null, sql);

        System.out.println(result);
    }

    @Test
    public void testParseInsertSql() {
        String sql = """
            INSERT INTO `testdb`.`school_resources_count`
            (`id`, `school_id`, `type`, `type_name`, `count`, `create_time`, `update_time`)
            VALUES (1, 'S-305003', 5, '教学资源', 1, '2024-04-07 16:29:42', '2024-04-07 16:29:42');
            """;

        DruidSqlParser parser = new DruidMySqlParser();

        InsertSqlParseResult result = parser.parseInsertSql(DbType.mysql.name(), sql);

        System.out.println(result);
    }
}
