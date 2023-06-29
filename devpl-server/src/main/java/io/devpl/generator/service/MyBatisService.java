package io.devpl.generator.service;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

public interface MyBatisService {

    /**
     * 获取可执行的SQL
     * @param mappedStatement MappedStatement
     * @param boundSql        BoundSql
     * @param parameterObject 参数对象
     * @return 可执行的SQL
     */
    String getExecutableSql(MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject);
}
