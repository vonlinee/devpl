package io.devpl.backend.tools.mybatis;

import java.sql.Statement;

public class DefaultStatementSqlBuilder implements StatementSqlBuilder {
    @Override
    public String buildSql(Statement statement) {
        if (statement == null) {
            return "statement is null";
        }
        /**
         * 某些驱动版本可以通过 PreparedStatement#toString方法获取sql，比如MySQL8
         */
        return statement.toString();
    }
}
