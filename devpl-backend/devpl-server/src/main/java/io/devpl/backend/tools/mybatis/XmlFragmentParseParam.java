package io.devpl.backend.tools.mybatis;

import lombok.Data;

@Data
public class XmlFragmentParseParam {

    /**
     * xml文本
     */
    private String xmlTagContent;

    private String resource;
    private boolean inferType;
    private boolean enableCache;

    private String databaseId;

    public boolean cacheParseResult() {
        return enableCache;
    }
}
