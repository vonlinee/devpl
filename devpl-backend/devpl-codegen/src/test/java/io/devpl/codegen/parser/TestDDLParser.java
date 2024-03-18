package io.devpl.codegen.parser;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.fastjson2.JSON;
import io.devpl.codegen.parser.sql.MySQLColumnVisitor;
import org.junit.Test;

public class TestDDLParser {

    @Test
    public void test1() {
        String ddl = """
            CREATE TABLE `student` (
              `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
              `name` varchar(32) NOT NULL DEFAULT '' COMMENT '姓名',
              `cert_no` varchar(32) NOT NULL DEFAULT '' COMMENT '证件号',
              `phone` varchar(11) NOT NULL DEFAULT '' COMMENT '手机号',
              `birth_date` date  default NULL COMMENT '出生日期',
              `grade_num` int(8)  NOT NULL DEFAULT '0' COMMENT '年级号',
              `class_num` int(8)  NOT NULL DEFAULT '0' COMMENT '班级号',
              `state` tinyint(8)  NOT NULL DEFAULT '0' COMMENT '状态',
              `balance` decimal(16,2)  NOT NULL DEFAULT '0.00' COMMENT '余额',
              `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
              PRIMARY KEY (`id`) USING BTREE,
              KEY `k_name`(`name`) USING BTREE comment '姓名搜索',
              KEY `k_birth_date`(`birth_date`) USING BTREE comment '出生日期',
              UNIQUE KEY `uk_cert_no` (`cert_no`) USING BTREE comment '证件号唯一',
              UNIQUE KEY `uk_grade_class` (`grade_num`,`class_num`) USING BTREE comment '年级+班级唯一'
            ) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='学生信息表';
            """;


        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(ddl, DbType.mysql);
        SQLStatement sqlStatement = parser.parseStatement();
        if (!(sqlStatement instanceof MySqlCreateTableStatement)) {
            System.out.println("not create table statement");
            return;
        }
        MySqlCreateTableStatement mySqlCreateTableStatement = (MySqlCreateTableStatement) sqlStatement;
        MySQLColumnVisitor mySQLColumnVisitor = new MySQLColumnVisitor();
        mySqlCreateTableStatement.accept(mySQLColumnVisitor);

        System.out.println("--------------table------------------");
        System.out.println("表名 => " + SQLUtils.normalize(mySqlCreateTableStatement.getTableName()));
        System.out.println("表备注 => " + SQLUtils.normalize(mySqlCreateTableStatement.getComment().toString()));
        for (SQLAssignItem tableOption : mySqlCreateTableStatement.getTableOptions()) {
            System.out.println("表配置信息 => " + tableOption.getTarget().toString() + "=" + tableOption.getValue().toString());
        }

        System.out.println("\n------------------columns----------------");
        mySQLColumnVisitor.getColumns().forEach(c -> {
            System.out.println(c.getName() + ":" + c.getDataType() + " => " + JSON.toJSONString(c.getAttributes()));
        });
        System.out.println("\n------------------index----------------");
        for (MySQLColumnVisitor.Index index : mySQLColumnVisitor.getIndices()) {
            System.out.println(JSON.toJSONString(index));
        }

    }
}
