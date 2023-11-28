package io.devpl.backend.service;

import io.devpl.backend.domain.ParamNode;
import io.devpl.backend.domain.param.GetSqlParam;
import io.devpl.backend.mybatis.ParamMeta;
import io.devpl.backend.mybatis.ParseResult;
import io.devpl.backend.mybatis.tree.TreeNode;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;

public interface MyBatisService {

    List<ParamNode> getMapperStatementParams(String content, boolean inferType);

    ParseResult parseMapperStatement(String mapperStatement, boolean inferType);

    /**
     * 获取可执行的SQL
     * @param mappedStatement MappedStatement
     * @param boundSql        BoundSql
     * @param parameterObject 参数对象
     * @return 可执行的SQL，如果解析出错，返回 '解析失败'
     */
    String getExecutableSql(MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject);

    String getSqlOfMappedStatement(GetSqlParam param);

    MappedStatement parseMappedStatement(String statement);

    List<ParamMeta> getParamMetadata(MappedStatement mappedStatement);

    List<ParamMeta> getParamMetadata(String statement);

    void recursive(TreeNode<ParamMeta> parentNode, List<ParamNode> rows, int parentId);
}
