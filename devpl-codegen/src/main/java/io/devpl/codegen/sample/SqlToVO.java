package io.devpl.codegen.sample;

import io.devpl.codegen.sql.SqlUtils;

import java.util.Map;
import java.util.Set;

/**
 * Sql 转换 对象
 */
public class SqlToVO {

    public static void main(String[] args) {

        String sql = """
            SELECT esr.id AS session_room_id, er.room_name, course.course_name, es.begin_time, es.end_time
            FROM exam_session_room AS esr LEFT JOIN
            exam_session AS es on esr.session_id = es.id
            LEFT JOIN exam_room AS er on esr.room_id = er.room_id
            LEFT JOIN (SELECT session_id, GROUP_CONCAT(course_name separator '、') AS course_name from exam_session_course AS
            esc
            LEFT JOIN exam_course ec on esc.exam_course_id = ec.id
            group by session_id) course ON esr.session_id = course.session_id
            WHERE esr.id IN (1, 2, 3)
                """;

        Map<String, Set<String>> selectColumns = SqlUtils.getSelectColumns(sql);

        System.out.println(selectColumns);
    }
}
