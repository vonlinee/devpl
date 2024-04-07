package io.devpl.codegen.generator.config.xml;

import io.devpl.codegen.generator.Context;
import io.devpl.codegen.generator.JavaTypeResolverConfiguration;
import io.devpl.codegen.generator.ObjectFactory;
import io.devpl.codegen.generator.PropertyRegistry;
import io.devpl.codegen.generator.config.*;
import io.devpl.codegen.util.Messages;
import io.devpl.codegen.util.StringUtils;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.IgnoredColumnException;
import org.mybatis.generator.config.IgnoredColumnPattern;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This class parses configuration files into the new Configuration API.
 */
public class GeneratorConfigurationParser {
    private final Properties extraProperties;
    private final Properties configurationProperties;

    public GeneratorConfigurationParser(Properties extraProperties) {
        super();
        if (extraProperties == null) {
            this.extraProperties = new Properties();
        } else {
            this.extraProperties = extraProperties;
        }
        configurationProperties = new Properties();
    }

    public Configuration parseConfiguration(Element rootNode) throws XMLParserException {

        Configuration configuration = new Configuration();

        NodeList nodeList = rootNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("properties".equals(childNode.getNodeName())) {
                parseProperties(childNode);
            } else if ("classPathEntry".equals(childNode.getNodeName())) {
                parseClassPathEntry(configuration, childNode);
            } else if ("context".equals(childNode.getNodeName())) {
                parseContext(configuration, childNode);
            }
        }

        return configuration;
    }

    protected void parseProperties(Node node) throws XMLParserException {
        Properties attributes = parseAttributes(node);
        String resource = attributes.getProperty("resource");
        String url = attributes.getProperty("url");

        if (!StringUtils.hasText(resource) && !StringUtils.hasText(url)) {
            throw new XMLParserException(Messages.getString("RuntimeError.14"));
        }

        if (StringUtils.hasText(resource) && StringUtils.hasText(url)) {
            throw new XMLParserException(Messages.getString("RuntimeError.14"));
        }

        URL resourceUrl;
        try {
            if (StringUtils.hasText(resource)) {
                resourceUrl = ObjectFactory.getResource(resource);
                if (resourceUrl == null) {
                    throw new XMLParserException(Messages.getString("RuntimeError.15", resource));
                }
            } else {
                resourceUrl = new URL(url);
            }
            InputStream inputStream = resourceUrl.openConnection().getInputStream();
            configurationProperties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            if (StringUtils.hasText(resource)) {
                throw new XMLParserException(Messages.getString("RuntimeError.16", resource));
            } else {
                throw new XMLParserException(Messages.getString("RuntimeError.17", url));
            }
        }
    }

    private void parseContext(Configuration configuration, Node node) {
        Properties attributes = parseAttributes(node);
        String defaultModelType = attributes.getProperty("defaultModelType");
        String targetRuntime = attributes.getProperty("targetRuntime");
        String introspectedColumnImpl = attributes.getProperty("introspectedColumnImpl");
        String id = attributes.getProperty("id");

        ModelType mt = defaultModelType == null ? null : ModelType.getModelType(defaultModelType);
        String contextImpl = attributes.getProperty("contextImpl");

        Context context = (Context) ObjectFactory.createInternalObject(contextImpl);
        context.setId(id);
        context.put(PropertyRegistry.MODEL_TYPE, mt);
        if (StringUtils.hasText(introspectedColumnImpl)) {
            context.put(PropertyRegistry.INTROSPECTED_COLUMN_IMPL, introspectedColumnImpl);
        }
        if (StringUtils.hasText(targetRuntime)) {
            context.put(PropertyRegistry.TARGET_RUNTIME, targetRuntime);
        }

        configuration.addContext(context);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            switch (childNode.getNodeName()) {
                case "property" -> parseContextProperty(context, childNode);
                case "plugin" -> parsePlugin(context, childNode);
                case "commentGenerator" -> parseCommentGenerator(context, childNode);
                case "jdbcConnection" -> parseJdbcConnection(context, childNode);
                case "connectionFactory" -> parseConnectionFactory(context, childNode);
                case "javaModelGenerator" -> parseJavaModelGenerator(context, childNode);
                case "javaTypeResolver" -> parseJavaTypeResolver(context, childNode);
                case "sqlMapGenerator" -> parseSqlMapGenerator(context, childNode);
                case "javaClientGenerator" -> parseJavaClientGenerator(context, childNode);
                case "table" -> parseTable(context, childNode);
            }
        }
    }

    protected void parseSqlMapGenerator(Context context, Node node) {

    }

    protected void parseTable(Context context, Node node) {
        TableConfiguration tc = TableConfiguration.none();
        // context.addTableConfiguration(tc);

        Properties attributes = parseAttributes(node);

        String catalog = attributes.getProperty("catalog");
        if (StringUtils.hasText(catalog)) {
            tc.setCatalog(catalog);
        }

        String schema = attributes.getProperty("schema");
        if (StringUtils.hasText(schema)) {
            tc.setSchema(schema);
        }

        String tableName = attributes.getProperty("tableName");
        if (StringUtils.hasText(tableName)) {
            tc.setTableName(tableName);
        }

        String domainObjectName = attributes.getProperty("domainObjectName");
        if (StringUtils.hasText(domainObjectName)) {
            tc.setDomainObjectName(domainObjectName);
        }

        String alias = attributes.getProperty("alias");
        if (StringUtils.hasText(alias)) {
            tc.setAlias(alias);
        }

        String enableInsert = attributes.getProperty("enableInsert");
        if (StringUtils.hasText(enableInsert)) {
            tc.setInsertStatementEnabled(StringUtils.isTrue(enableInsert));
        }

        String enableSelectByPrimaryKey = attributes.getProperty("enableSelectByPrimaryKey");
        if (StringUtils.hasText(enableSelectByPrimaryKey)) {
            tc.setSelectByPrimaryKeyStatementEnabled(StringUtils.isTrue(enableSelectByPrimaryKey));
        }

        String enableSelectByExample = attributes.getProperty("enableSelectByExample");
        if (StringUtils.hasText(enableSelectByExample)) {
            tc.setSelectByExampleStatementEnabled(StringUtils.isTrue(enableSelectByExample));
        }

        String enableUpdateByPrimaryKey = attributes.getProperty("enableUpdateByPrimaryKey");
        if (StringUtils.hasText(enableUpdateByPrimaryKey)) {
            tc.setUpdateByPrimaryKeyStatementEnabled(StringUtils.isTrue(enableUpdateByPrimaryKey));
        }

        String enableDeleteByPrimaryKey = attributes.getProperty("enableDeleteByPrimaryKey");
        if (StringUtils.hasText(enableDeleteByPrimaryKey)) {
            tc.setDeleteByPrimaryKeyStatementEnabled(StringUtils.isTrue(enableDeleteByPrimaryKey));
        }

        String enableDeleteByExample = attributes.getProperty("enableDeleteByExample");
        if (StringUtils.hasText(enableDeleteByExample)) {
            tc.setDeleteByExampleStatementEnabled(StringUtils.isTrue(enableDeleteByExample));
        }

        String enableCountByExample = attributes.getProperty("enableCountByExample");
        if (StringUtils.hasText(enableCountByExample)) {
            tc.setCountByExampleStatementEnabled(StringUtils.isTrue(enableCountByExample));
        }

        String enableUpdateByExample = attributes.getProperty("enableUpdateByExample");
        if (StringUtils.hasText(enableUpdateByExample)) {
            tc.setUpdateByExampleStatementEnabled(StringUtils.isTrue(enableUpdateByExample));
        }

        String selectByPrimaryKeyQueryId = attributes.getProperty("selectByPrimaryKeyQueryId");
        if (StringUtils.hasText(selectByPrimaryKeyQueryId)) {
            tc.setSelectByPrimaryKeyQueryId(selectByPrimaryKeyQueryId);
        }

        String selectByExampleQueryId = attributes.getProperty("selectByExampleQueryId");
        if (StringUtils.hasText(selectByExampleQueryId)) {
            tc.setSelectByExampleQueryId(selectByExampleQueryId);
        }

        String modelType = attributes.getProperty("modelType");
        if (StringUtils.hasText(modelType)) {
            tc.setConfiguredModelType(modelType);
        }

        String escapeWildcards = attributes.getProperty("escapeWildcards");
        if (StringUtils.hasText(escapeWildcards)) {
            tc.setWildcardEscapingEnabled(StringUtils.isTrue(escapeWildcards));
        }

        String delimitIdentifiers = attributes.getProperty("delimitIdentifiers");
        if (StringUtils.hasText(delimitIdentifiers)) {
            tc.setDelimitIdentifiers(StringUtils.isTrue(delimitIdentifiers));
        }

        String delimitAllColumns = attributes.getProperty("delimitAllColumns");
        if (StringUtils.hasText(delimitAllColumns)) {
            tc.setAllColumnDelimitingEnabled(StringUtils.isTrue(delimitAllColumns));
        }

        String mapperName = attributes.getProperty("mapperName");
        if (StringUtils.hasText(mapperName)) {
            tc.setMapperName(mapperName);
        }

        String sqlProviderName = attributes.getProperty("sqlProviderName");
        if (StringUtils.hasText(sqlProviderName)) {
            tc.setSqlProviderName(sqlProviderName);
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) {
                parseProperty(tc, childNode);
            } else if ("columnOverride".equals(childNode.getNodeName())) {
                parseColumnOverride(tc, childNode);
            } else if ("ignoreColumn".equals(childNode.getNodeName())) {
                parseIgnoreColumn(tc, childNode);
            } else if ("ignoreColumnsByRegex".equals(childNode.getNodeName())) {
                parseIgnoreColumnByRegex(tc, childNode);
            } else if ("generatedKey".equals(childNode.getNodeName())) {
                parseGeneratedKey(tc, childNode);
            } else if ("domainObjectRenamingRule".equals(childNode.getNodeName())) {
                parseDomainObjectRenamingRule(tc, childNode);
            } else if ("columnRenamingRule".equals(childNode.getNodeName())) {
                parseColumnRenamingRule(tc, childNode);
            }
        }
    }

    private void parseColumnOverride(TableConfiguration tc, Node node) {
        Properties attributes = parseAttributes(node);
        String column = attributes.getProperty("column");

        ColumnOverride co = new ColumnOverride(column);

        String property = attributes.getProperty("property");
        if (StringUtils.hasText(property)) {
            co.setJavaProperty(property);
        }

        String javaType = attributes.getProperty("javaType");
        if (StringUtils.hasText(javaType)) {
            co.setJavaType(javaType);
        }

        String jdbcType = attributes.getProperty("jdbcType");
        if (StringUtils.hasText(jdbcType)) {
            co.setJdbcType(jdbcType);
        }

        String typeHandler = attributes.getProperty("typeHandler");
        if (StringUtils.hasText(typeHandler)) {
            co.setTypeHandler(typeHandler);
        }

        String delimitedColumnName = attributes.getProperty("delimitedColumnName");
        if (StringUtils.hasText(delimitedColumnName)) {
            co.setColumnNameDelimited(StringUtils.isTrue(delimitedColumnName));
        }

        String isGeneratedAlways = attributes.getProperty("isGeneratedAlways");
        if (StringUtils.hasText(isGeneratedAlways)) {
            co.setGeneratedAlways(Boolean.parseBoolean(isGeneratedAlways));
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) {
                parseProperty(co, childNode);
            }
        }

        tc.addColumnOverride(co);
    }

    private void parseGeneratedKey(TableConfiguration tc, Node node) {
        Properties attributes = parseAttributes(node);

        String column = attributes.getProperty("column");
        boolean identity = StringUtils.isTrue(attributes.getProperty("identity"));
        String sqlStatement = attributes.getProperty("sqlStatement");
        String type = attributes.getProperty("type");

        GeneratedKey gk = new GeneratedKey(column, sqlStatement, identity, type);

        tc.setGeneratedKey(gk);
    }

    private void parseIgnoreColumn(TableConfiguration tc, Node node) {
        Properties attributes = parseAttributes(node);
        String column = attributes.getProperty("column");
        String delimitedColumnName = attributes.getProperty("delimitedColumnName");

        IgnoredColumn ic = new IgnoredColumn(column);

        if (StringUtils.hasText(delimitedColumnName)) {
            ic.setColumnNameDelimited(StringUtils.isTrue(delimitedColumnName));
        }

        tc.addIgnoredColumn(ic);
    }

    private void parseIgnoreColumnByRegex(TableConfiguration tc, Node node) {
        Properties attributes = parseAttributes(node);
        String pattern = attributes.getProperty("pattern");

        IgnoredColumnPattern icPattern = new IgnoredColumnPattern(pattern);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if ("except".equals(childNode.getNodeName())) {
                parseException(icPattern, childNode);
            }
        }

        tc.addIgnoredColumnPattern(icPattern);
    }

    private void parseException(IgnoredColumnPattern icPattern, Node node) {
        Properties attributes = parseAttributes(node);
        String column = attributes.getProperty("column");
        String delimitedColumnName = attributes.getProperty("delimitedColumnName");

        IgnoredColumnException exception = new IgnoredColumnException(column);

        if (StringUtils.hasText(delimitedColumnName)) {
            exception.setColumnNameDelimited(StringUtils.isTrue(delimitedColumnName));
        }

        icPattern.addException(exception);
    }

    private void parseDomainObjectRenamingRule(TableConfiguration tc, Node node) {
        Properties attributes = parseAttributes(node);
        String searchString = attributes.getProperty("searchString");
        String replaceString = attributes.getProperty("replaceString");

    }

    private void parseColumnRenamingRule(TableConfiguration tc, Node node) {
        Properties attributes = parseAttributes(node);
        String searchString = attributes.getProperty("searchString");
        String replaceString = attributes.getProperty("replaceString");

        ColumnRenamingRule crr = new ColumnRenamingRule();

        crr.setSearchString(searchString);

        if (StringUtils.hasText(replaceString)) {
            crr.setReplaceString(replaceString);
        }

        tc.setColumnRenamingRule(crr);
    }

    protected void parseJavaTypeResolver(Context context, Node node) {
        JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();

        context.putObject(javaTypeResolverConfiguration);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type");

        if (StringUtils.hasText(type)) {
            javaTypeResolverConfiguration.setConfigurationType(type);
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) {
                parseProperty(javaTypeResolverConfiguration, childNode);
            }
        }
    }

    private void parsePlugin(Context context, Node node) {
        PluginConfiguration pluginConfiguration = new PluginConfiguration();

        context.addPluginConfiguration(pluginConfiguration);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type");

        pluginConfiguration.setConfigurationType(type);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) {
                parseProperty(pluginConfiguration, childNode);
            }
        }
    }

    protected void parseJavaModelGenerator(Context context, Node node) {

    }

    private void parseJavaClientGenerator(Context context, Node node) {

    }

    protected void parseJdbcConnection(Context context, Node node) {
        JdbcConfiguration jdbcConnectionConfiguration = new JdbcConfiguration();

        context.putObject(jdbcConnectionConfiguration);

        Properties attributes = parseAttributes(node);
        String driverClass = attributes.getProperty("driverClass");
        String connectionURL = attributes.getProperty("connectionURL");

        jdbcConnectionConfiguration.setDriverClassName(driverClass);
        jdbcConnectionConfiguration.setConnectionUrl(connectionURL);

        String userId = attributes.getProperty("userId");
        if (StringUtils.hasText(userId)) {
            jdbcConnectionConfiguration.setUserId(userId);
        }

        String password = attributes.getProperty("password");
        if (StringUtils.hasText(password)) {
            jdbcConnectionConfiguration.setPassword(password);
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) {
                parseProperty(jdbcConnectionConfiguration, childNode);
            }
        }
    }

    protected void parseClassPathEntry(Configuration configuration, Node node) {
        Properties attributes = parseAttributes(node);

        configuration.addClasspathEntry(attributes.getProperty("location"));
    }

    protected void parseContextProperty(Context context, Node node) {
        Properties attributes = parseAttributes(node);
        String name = attributes.getProperty("name");
        String value = attributes.getProperty("value");
        context.put(name, value);
    }

    protected void parseProperty(PropertyHolder propertyHolder, Node node) {
        Properties attributes = parseAttributes(node);
        String name = attributes.getProperty("name");
        String value = attributes.getProperty("value");
        propertyHolder.addProperty(name, value);
    }

    protected Properties parseAttributes(Node node) {
        Properties attributes = new Properties();
        NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node attribute = nnm.item(i);
            String value = parsePropertyTokens(attribute.getNodeValue());
            attributes.put(attribute.getNodeName(), value);
        }

        return attributes;
    }

    String parsePropertyTokens(String s) {
        final String OPEN = "${";
        final String CLOSE = "}";
        int currentIndex = 0;

        List<String> answer = new ArrayList<>();

        int markerStartIndex = s.indexOf(OPEN);
        if (markerStartIndex < 0) {
            // no parameter markers
            answer.add(s);
            currentIndex = s.length();
        }

        while (markerStartIndex > -1) {
            if (markerStartIndex > currentIndex) {
                // add the characters before the next parameter marker
                answer.add(s.substring(currentIndex, markerStartIndex));
                currentIndex = markerStartIndex;
            }

            int markerEndIndex = s.indexOf(CLOSE, currentIndex);
            int nestedStartIndex = s.indexOf(OPEN, markerStartIndex + OPEN.length());
            while (nestedStartIndex > -1 && markerEndIndex > -1 && nestedStartIndex < markerEndIndex) {
                nestedStartIndex = s.indexOf(OPEN, nestedStartIndex + OPEN.length());
                markerEndIndex = s.indexOf(CLOSE, markerEndIndex + CLOSE.length());
            }

            if (markerEndIndex < 0) {
                // no closing delimiter, just move to the end of the string
                answer.add(s.substring(markerStartIndex));
                currentIndex = s.length();
                break;
            }

            // we have a valid property marker...
            String property = s.substring(markerStartIndex + OPEN.length(), markerEndIndex);
            String propertyValue = resolveProperty(parsePropertyTokens(property));
            if (propertyValue == null) {
                // add the property marker back into the stream
                answer.add(s.substring(markerStartIndex, markerEndIndex + 1));
            } else {
                answer.add(propertyValue);
            }

            currentIndex = markerEndIndex + CLOSE.length();
            markerStartIndex = s.indexOf(OPEN, currentIndex);
        }

        if (currentIndex < s.length()) {
            answer.add(s.substring(currentIndex));
        }

        return String.join("", answer);
    }

    protected void parseCommentGenerator(Context context, Node node) {

    }

    protected void parseConnectionFactory(Context context, Node node) {

    }

    /**
     * This method resolve a property from one of the three sources: system properties,
     * properties loaded from the &lt;properties&gt; configuration element, and
     * "extra" properties that may be supplied by the Maven or Ant environments.
     *
     * <p>If there is a name collision, system properties take precedence, followed by
     * configuration properties, followed by extra properties.
     *
     * @param key property key
     * @return the resolved property.  This method will return null if the property is
     * undefined in any of the sources.
     */
    private String resolveProperty(String key) {
        String property = System.getProperty(key);

        if (property == null) {
            property = configurationProperties.getProperty(key);
        }

        if (property == null) {
            property = extraProperties.getProperty(key);
        }

        return property;
    }
}
