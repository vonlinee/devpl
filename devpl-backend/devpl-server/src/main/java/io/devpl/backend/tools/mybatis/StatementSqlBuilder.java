package io.devpl.backend.tools.mybatis;

import java.sql.Statement;

/**
 * 从 Statement 类里获取真实执行的sql
 * <a href="https://stackoverflow.com/questions/2382532/how-can-i-get-the-sql-of-a-preparedstatement">...</a>
 *
 * @see java.sql.PreparedStatement
 * @see java.beans.Statement
 */
public interface StatementSqlBuilder {

    String buildSql(Statement statement);
}
