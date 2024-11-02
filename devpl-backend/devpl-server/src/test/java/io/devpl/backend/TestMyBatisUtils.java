package io.devpl.backend;

import io.devpl.backend.tools.mybatis.MyBatisUtils;

public class TestMyBatisUtils {

    public void test1() {

        String log = """
            ==>  Preparing: SELECT * from `t_student` where `STU_SEX` = ? AND STU_NO IN ( ? , ? , ? )
            ==> Parameters: 23(Integer), 1(String), 2(String), 3(String)
            <==      Total: 0
            """;

        String executableSql = MyBatisUtils.parseExecutableSql(log);

        System.out.println(executableSql);
    }
}
