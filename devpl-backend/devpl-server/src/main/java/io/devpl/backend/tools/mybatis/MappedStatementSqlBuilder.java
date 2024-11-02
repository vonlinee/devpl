package io.devpl.backend.tools.mybatis;

import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;

/**
 * 从 MappedStatement 类里获取预编译的sql和真实执行的sql
 *
 * @see java.sql.PreparedStatement
 * @see java.beans.Statement
 */
public interface MappedStatementSqlBuilder {

    String buildPreparedSql(MappedStatement mappedStatement, Map<String, Object> parameterObject);

    String buildExecutableSql(MappedStatement mappedStatement, Map<String, Object> parameterObject);
}
