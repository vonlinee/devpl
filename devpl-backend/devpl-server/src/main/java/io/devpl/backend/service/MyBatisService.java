package io.devpl.backend.service;

import io.devpl.backend.domain.MsParamNode;
import io.devpl.backend.domain.enums.MapperStatementParamValueType;
import io.devpl.backend.domain.param.GetSqlParam;
import io.devpl.backend.mybatis.ParamMeta;
import io.devpl.backend.mybatis.ParseResult;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;

/**
 * MyBatis Service
 */
public interface MyBatisService {

    /**
     * 获取MyBatis参数节点
     *
     * @param content   MyBatis Mapper Statement
     * @param inferType 推断参数的类型
     * @return 参数列表
     */
    List<MsParamNode> getMapperStatementParams(String content, boolean inferType);

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

    /**
     * 获取MappedStatement的sql
     *
     * @param param mapper标签内容
     * @return sql，可能有占位符
     */
    String getSqlOfMappedStatement(GetSqlParam param);

    /**
     * 解析标签
     *
     * @param statement mapper标签
     * @return MappedStatement实例
     */
    MappedStatement parseMappedStatement(String statement);

    /**
     * 解析MappedStatement实例中的参数节点
     *
     * @param mappedStatement MappedStatement实例
     * @return 参数信息
     */
    List<ParamMeta> getParamMetadata(MappedStatement mappedStatement);

    /**
     * 解析参数信息
     *
     * @param statement MyBatis MapperStatement 标签内容
     * @return 参数信息
     */
    List<ParamMeta> getParamMetadata(String statement);

    /**
     * 根据参数名称推断参数类型
     *
     * @param paramName 参数名称
     * @return 参数数据类型
     */
    MapperStatementParamValueType inferType(String paramName);
}
