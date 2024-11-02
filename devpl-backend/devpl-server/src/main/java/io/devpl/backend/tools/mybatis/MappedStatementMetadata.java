package io.devpl.backend.tools.mybatis;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Collection;
import java.util.Set;

/**
 * MyBatis Mapper语句记录表
 */
@Getter
@Setter
public class MappedStatementMetadata {

    /**
     * 项目根路径
     */
    private String projectRoot;

    /**
     * 语句ID
     */
    private String belongFile;

    /**
     * 语句ID
     */
    private String statementId;

    /**
     * 语句类型
     */
    private String statementType;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 结果类型
     */
    private String resultType;

    /**
     * 语句内容
     */
    private String rawContent;

    private MappedStatement mappedStatement;

    private Collection<StatementParam> params;
}
