package io.devpl.backend.tools.mybatis;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;

public interface MappedStatementParser {

    void setStatementParamExtractor(MappedStatementParamExtractor extractor);

    /**
     * @return MappedStatement元数据信息
     * @see XMLConfigBuilder#parse() 参考源码是如何进行解析的
     */
    MappedStatementMetadata parseXmlStatement(XmlFragmentParseParam param);
}
