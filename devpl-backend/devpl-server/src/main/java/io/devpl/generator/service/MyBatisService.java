package io.devpl.generator.service;

import io.devpl.generator.domain.ParamNode;
import io.devpl.generator.domain.param.GetSqlParam;
import io.devpl.generator.mybatis.ParseResult;
import io.devpl.generator.mybatis.tree.TreeNode;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;

public interface MyBatisService {

    ParseResult paraseMapperStatement(String mapperStatement);

    /**
     * 获取可执行的SQL
     * @param mappedStatement MappedStatement
     * @param boundSql        BoundSql
     * @param parameterObject 参数对象
     * @return 可执行的SQL，如果解析出错，返回 '解析失败'
     */
    String getExecutableSql(MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject);

    String getPreCompliedSql(GetSqlParam param);

    void recursive(TreeNode<String> parentNode, List<ParamNode> rows, int parentId);
}
