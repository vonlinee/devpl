package io.devpl.backend.service;

import io.devpl.backend.domain.ParamNode;
import io.devpl.backend.domain.enums.MapperStatementParamValueType;
import io.devpl.backend.domain.param.GetSqlParam;
import io.devpl.backend.mybatis.ParamMeta;
import io.devpl.backend.mybatis.ParseResult;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;

public interface MyBatisService {

    /**
     * 获取MyBatis参数节点
     *
     * @param content   MyBatis Mapper Statement
     * @param inferType 推断参数的类型
     * @return 参数列表
     */
    List<ParamNode> getMapperStatementParams(String content, boolean inferType);

    /**
     * 解析字符串形式的Mapper Statement
     *
     * @param mapperStatement mapper statement
     * @param inferType       是否开启类型推断
     * @return 解析结果
     */
    ParseResult parseMapperStatement(String mapperStatement, boolean inferType);

    /**
     * 获取可执行的SQL
     *
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

    MapperStatementParamValueType inferType(String paramName);
}
