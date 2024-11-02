package io.devpl.backend.tools.mybatis;

import io.devpl.codegen.type.AbstractTypeInferenceStrategy;
import io.devpl.codegen.type.CompositeTypeInferenceStrategy;
import io.devpl.sdk.util.StringUtils;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.builder.xml.XMLIncludeTransformer;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.Field;
import java.util.*;

public class DynamicMyBatisSupport extends Configuration implements MappedStatementParser, MappedStatementSqlBuilder {

    MappedStatementParamExtractor extractor;

    public DynamicMyBatisSupport() {
        try {
            Field field = Configuration.class.getDeclaredField("mappedStatements");
            field.setAccessible(true);
            field.set(this, new HashMap<>());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("cannot replace mappedStatements from StrictMap to HashMap by reflection", e);
        }
    }

    @Override
    public void setStatementParamExtractor(MappedStatementParamExtractor extractor) {
        this.extractor = extractor;
    }

    public MappedStatementParamExtractor getMappedStatementParamExtractor() {
        return this.extractor == null ? new DefaultMappedStatementParamExtractor() : this.extractor;
    }

    /**
     * mappersElement(root.evalNode("mappers"));
     * TODO 支持引用<sql></sql>标签内容
     *
     * @return MappedStatementMetadata
     * @see XMLMapperBuilder#parse() 参考源码是如何进行解析的
     */
    @Override
    public MappedStatementMetadata parseXmlStatement(XmlFragmentParseParam param) {
        XPathParser xPathParser = new XPathParser(param.getXmlTagContent(), false, null, new IgnoreDTDEntityResolver());
        // 直接取根节点
        XNode node = xPathParser.evalNode("/*");

        MyMapperBuilderAssistant assistant = new MyMapperBuilderAssistant(this, param.getResource());
        assistant.setCurrentNamespace("null");

        String databaseId = param.getDatabaseId();

        // 解析为MappedStatement对象
        MissingCompatiableStatementBuilder statementParser = new MissingCompatiableStatementBuilder(this, node, assistant, databaseId);
        MappedStatement mappedStatement = statementParser.parseMappedStatement();

        if (!param.cacheParseResult()) {
            mappedStatements.remove(mappedStatement.getId());
        }

        // 解析参数
        MappedStatementParamExtractor extractor = getMappedStatementParamExtractor();

        // 开启类型推断
        if (param.isInferType()) {
            extractor.setTypeInferenceStrategy(new DefaultTypeInferenceStrategy());
        }
        extractor.apply(mappedStatement);

        MappedStatementMetadata msm = new MappedStatementMetadata();
        msm.setMappedStatement(mappedStatement);
        msm.setParams(extractor.getParams());
        msm.setNamespace(msm.getNamespace());
        return msm;
    }

    public boolean containsMappedStatement(String id) {
        return mappedStatements.containsKey(id);
    }

    public boolean containsMappedStatement(MappedStatement mappedStatement) {
        return mappedStatements.containsKey(mappedStatement.getId());
    }

    public MappedStatement removeMappedStatement(MappedStatement mappedStatement) {
        return mappedStatements.remove(mappedStatement.getId());
    }

    /**
     * MappedStatement#getBoundSql每次返回的是不同的对象
     *
     * @see MappedStatement#getBoundSql(Object)
     */
    @Override
    public String buildPreparedSql(MappedStatement mappedStatement, Map<String, Object> parameterObject) {
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        return boundSql.getSql();
    }

    /**
     * TODO 使用的其他组件无法生效，例如一些TypeHandler
     *
     * @param mappedStatement MappedStatement
     * @param parameterObject parameterObject, map or pojo
     * @return sql with all param filled
     * @see org.apache.ibatis.logging.jdbc.PreparedStatementLogger  Parameters
     * @see org.apache.ibatis.logging.jdbc.ConnectionLogger         Preparing
     * @see org.apache.ibatis.logging.jdbc.ResultSetLogger          Columns
     * @see org.apache.ibatis.logging.jdbc.BaseJdbcLogger
     * @see org.apache.ibatis.scripting.defaults.DefaultParameterHandler
     * @see com.baomidou.mybatisplus.core.MybatisParameterHandler
     * @see org.apache.ibatis.executor.BaseExecutor#query(MappedStatement, Object, RowBounds, ResultHandler)
     */
    @Override
    public String buildExecutableSql(MappedStatement mappedStatement, Map<String, Object> parameterObject) {
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        Configuration configuration = mappedStatement.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        List<String> paramValues = new ArrayList<>();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            MetaObject metaObject = null;
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        if (metaObject == null) {
                            metaObject = configuration.newMetaObject(parameterObject);
                        }
                        value = metaObject.getValue(propertyName);
                    }
                    TypeHandler<?> typeHandler = parameterMapping.getTypeHandler();
                    JdbcType jdbcType = parameterMapping.getJdbcType();
                    if (value == null && jdbcType == null) {
                        jdbcType = configuration.getJdbcTypeForNull();
                    }

                    if (value == null) {
                        paramValues.add("null");
                    } else {
                        paramValues.add(value + "(" + value.getClass().getSimpleName() + ")");
                    }
                }
            }
        }

        String log = "==>  Preparing: " + boundSql.getSql().replace("\n", " ");
        log += "\n";
        log += "==> Parameters: " + StringUtils.join(paramValues, ",");
        return MyBatisUtils.parseExecutableSql(log);
    }

    // ======================================= Inner Class ===================================

    static class DefaultTypeInferenceStrategy extends CompositeTypeInferenceStrategy<StatementParam> {

        public DefaultTypeInferenceStrategy() {

            // TODO 可以根据数据库字段来解析类型

            addStrategy(new AbstractTypeInferenceStrategy<>() {
                @Override
                public void inferType(Collection<StatementParam> statementParams) {
                    // 类型推断
                    for (StatementParam param : statementParams) {
                        if (param.getDataType() != null || param.getValue() == null) {
                            continue;
                        }
                        Object value = param.getValue();
                        if (value instanceof Number) {
                            param.setDataType(MSParamDataType.NUMERIC);
                        } else if (value instanceof String) {
                            param.setDataType(MSParamDataType.STRING);
                        }
                    }
                }
            });

            addStrategy(new AbstractTypeInferenceStrategy<>() {
                /**
                 * 根据名称推断类型
                 *
                 * @param statementParams 参数列表
                 */
                @Override
                public void inferType(Collection<StatementParam> statementParams) {
                    for (StatementParam statementParam : statementParams) {
                        if (statementParam.getDataType() != null) {
                            continue;
                        }
                        String paramName = statementParam.getName();
                        MSParamDataType type = MSParamDataType.NULL;
                        if (StringUtils.endsWithIgnoreCase(paramName, "name")) {
                            type = MSParamDataType.STRING;
                        } else if (StringUtils.endsWithIgnoreCase(paramName, "num")) {
                            type = MSParamDataType.NUMERIC;
                        }
                        statementParam.setDataType(type);
                    }
                }
            });
        }
    }

    /**
     * 兼容resultType指定的类型缺失的情况
     *
     * @see XMLStatementBuilder
     */
    private static class MissingCompatiableStatementBuilder extends XMLStatementBuilder {

        private final XNode context;
        private final MyMapperBuilderAssistant builderAssistant;
        private final String requiredDatabaseId;
        private MappedStatement parsedStatement;

        public MissingCompatiableStatementBuilder(Configuration configuration, XNode context, MapperBuilderAssistant assistant, String databaseId) {
            // mapper文件路径
            super(configuration, assistant, context, databaseId);
            this.context = context;
            this.builderAssistant = (MyMapperBuilderAssistant) assistant;
            this.requiredDatabaseId = databaseId;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected <T> Class<? extends T> resolveClass(String alias) {
            Class<? extends T> clazz;
            try {
                clazz = super.resolveClass(alias);
            } catch (Exception exception) {
                // 忽略不存在的类型
            }
            clazz = (Class<? extends T>) Integer.class;
            return clazz;
        }

        /**
         * this method is copied from {@code XMLStatementBuilder#parseStatementNode()}
         * <a href="https://mybatis.org/mybatis-3/zh_CN/sqlmap-xml.html">...</a>
         *
         * @see XMLStatementBuilder#parseStatementNode()
         */
        @Override
        public void parseStatementNode() {
            String id = context.getStringAttribute("id");
            String databaseId = context.getStringAttribute("databaseId");

            /**
             * @see XMLStatementBuilder 构造函数  requiredDatabaseId为null
             */
            if (!databaseIdMatchesCurrent(id, databaseId, this.requiredDatabaseId)) {
                return;
            }

            String nodeName = context.getNode().getNodeName();
            SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
            boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
            boolean flushCache = context.getBooleanAttribute("flushCache", !isSelect);
            boolean useCache = context.getBooleanAttribute("useCache", isSelect);
            boolean resultOrdered = context.getBooleanAttribute("resultOrdered", false);

            // Include Fragments before parsing
            XMLIncludeTransformer includeParser = new XMLIncludeTransformer(configuration, builderAssistant);
            includeParser.applyIncludes(context.getNode());


            Class<?> parameterTypeClass = null;
            String parameterType = context.getStringAttribute("parameterType");
            if (parameterType != null) {
                parameterTypeClass = resolveClass(parameterType);
            }

            String lang = context.getStringAttribute("lang");
            /**
             * @see Configuration#getLanguageDriver(Class)
             */
            LanguageDriver langDriver = getLanguageDriver(lang);

            // Parse selectKey after includes and remove them.
            processSelectKeyNodes(id, parameterTypeClass, langDriver);

            // Parse the SQL (pre: <selectKey> and <include> were parsed and removed)
            KeyGenerator keyGenerator;
            String keyStatementId = id + SelectKeyGenerator.SELECT_KEY_SUFFIX;
            keyStatementId = builderAssistant.applyCurrentNamespace(keyStatementId, true);
            if (configuration.hasKeyGenerator(keyStatementId)) {
                keyGenerator = configuration.getKeyGenerator(keyStatementId);
            } else {
                keyGenerator = context.getBooleanAttribute("useGeneratedKeys", configuration.isUseGeneratedKeys() && SqlCommandType.INSERT.equals(sqlCommandType)) ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
            }

            SqlSource sqlSource = langDriver.createSqlSource(configuration, context, parameterTypeClass);
            StatementType mappedStatementType = StatementType.valueOf(context.getStringAttribute("statementType", StatementType.PREPARED.toString()));
            Integer fetchSize = context.getIntAttribute("fetchSize");
            Integer timeout = context.getIntAttribute("timeout");
            String parameterMap = context.getStringAttribute("parameterMap");
            String resultType = context.getStringAttribute("resultType");
            Class<?> resultTypeClass = resolveClass(resultType);
            String resultMap = context.getStringAttribute("resultMap");
            if (resultTypeClass == null && resultMap == null) {
                resultTypeClass = MapperAnnotationBuilder.getMethodReturnType(builderAssistant.getCurrentNamespace(), id);
            }
            String resultSetType = context.getStringAttribute("resultSetType");
            ResultSetType resultSetTypeEnum = resolveResultSetType(resultSetType);
            if (resultSetTypeEnum == null) {
                resultSetTypeEnum = configuration.getDefaultResultSetType();
            }
            String keyProperty = context.getStringAttribute("keyProperty");
            String keyColumn = context.getStringAttribute("keyColumn");
            String resultSets = context.getStringAttribute("resultSets");
            boolean dirtySelect = context.getBooleanAttribute("affectData", Boolean.FALSE);

            parsedStatement = builderAssistant.addMappedStatement(id, sqlSource, mappedStatementType, sqlCommandType, fetchSize, timeout, parameterMap, parameterTypeClass, resultMap, resultTypeClass, resultSetTypeEnum, flushCache, useCache, resultOrdered, keyGenerator, keyProperty, keyColumn, null, langDriver, resultSets, dirtySelect);
        }

        /**
         * 解析MappedStatement标签
         */
        public synchronized MappedStatement parseMappedStatement() {
            this.parseStatementNode();
            MappedStatement ms = parsedStatement;
            this.parsedStatement = null;
            return ms;
        }

        private void processSelectKeyNodes(String id, Class<?> parameterTypeClass, LanguageDriver langDriver) {
            List<XNode> selectKeyNodes = context.evalNodes("selectKey");
            if (configuration.getDatabaseId() != null) {
                parseSelectKeyNodes(id, selectKeyNodes, parameterTypeClass, langDriver, configuration.getDatabaseId());
            }
            parseSelectKeyNodes(id, selectKeyNodes, parameterTypeClass, langDriver, null);
            removeSelectKeyNodes(selectKeyNodes);
        }

        private void parseSelectKeyNodes(String parentId, List<XNode> list, Class<?> parameterTypeClass, LanguageDriver langDriver, String skRequiredDatabaseId) {
            for (XNode nodeToHandle : list) {
                String id = parentId + SelectKeyGenerator.SELECT_KEY_SUFFIX;
                String databaseId = nodeToHandle.getStringAttribute("databaseId");
                if (databaseIdMatchesCurrent(id, databaseId, skRequiredDatabaseId)) {
                    parseSelectKeyNode(id, nodeToHandle, parameterTypeClass, langDriver, databaseId);
                }
            }
        }

        private void parseSelectKeyNode(String id, XNode nodeToHandle, Class<?> parameterTypeClass, LanguageDriver langDriver, String databaseId) {
            String resultType = nodeToHandle.getStringAttribute("resultType");
            Class<?> resultTypeClass = resolveClass(resultType);
            StatementType mappedStatementType = StatementType.valueOf(nodeToHandle.getStringAttribute("statementType", StatementType.PREPARED.toString()));
            String keyProperty = nodeToHandle.getStringAttribute("keyProperty");
            String keyColumn = nodeToHandle.getStringAttribute("keyColumn");
            boolean executeBefore = "BEFORE".equals(nodeToHandle.getStringAttribute("order", "AFTER"));

            // defaults
            boolean useCache = false;
            boolean resultOrdered = false;
            KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
            Integer fetchSize = null;
            Integer timeout = null;
            boolean flushCache = false;
            String parameterMap = null;
            String resultMap = null;
            ResultSetType resultSetTypeEnum = null;

            SqlSource sqlSource = langDriver.createSqlSource(configuration, nodeToHandle, parameterTypeClass);
            SqlCommandType sqlCommandType = SqlCommandType.SELECT;

            /**
             * @see Configuration#addMappedStatement(MappedStatement)
             */
            builderAssistant.addMappedStatement(id, sqlSource, mappedStatementType, sqlCommandType, fetchSize, timeout, parameterMap, parameterTypeClass, resultMap, resultTypeClass, resultSetTypeEnum, flushCache, useCache, resultOrdered, keyGenerator, keyProperty, keyColumn, databaseId, langDriver, null, false);

            id = builderAssistant.applyCurrentNamespace(id, false);

            MappedStatement keyStatement = configuration.getMappedStatement(id, false);
            configuration.addKeyGenerator(id, new SelectKeyGenerator(keyStatement, executeBefore));
        }

        private boolean databaseIdMatchesCurrent(String id, String databaseId, String requiredDatabaseId) {
            if (requiredDatabaseId != null) {
                return requiredDatabaseId.equals(databaseId);
            }
            if (databaseId != null) {
                return false;
            }
            id = builderAssistant.applyCurrentNamespace(id, false);
            if (!this.configuration.hasStatement(id, false)) {
                return true;
            }
            // skip this statement if there is a previous one with a not null databaseId
            MappedStatement previous = this.configuration.getMappedStatement(id, false); // issue #2
            return previous.getDatabaseId() == null;
        }


        private void removeSelectKeyNodes(List<XNode> selectKeyNodes) {
            for (XNode nodeToHandle : selectKeyNodes) {
                nodeToHandle.getParent().getNode().removeChild(nodeToHandle.getNode());
            }
        }

        private LanguageDriver getLanguageDriver(String lang) {
            Class<? extends LanguageDriver> langClass = null;
            if (lang != null) {
                langClass = resolveClass(lang);
            }
            return configuration.getLanguageDriver(langClass);
        }
    }

    /**
     * @see MapperBuilderAssistant
     */
    private static class MyMapperBuilderAssistant extends MapperBuilderAssistant {

        private final String resource;
        private Cache currentCache;
        private boolean unresolvedCacheRef; // issue #676

        public MyMapperBuilderAssistant(Configuration configuration, String resource) {
            super(configuration, resource);
            this.resource = resource;
        }

        @Override
        public MappedStatement addMappedStatement(String id, SqlSource sqlSource, StatementType mappedStatementType, SqlCommandType sqlCommandType, Integer fetchSize, Integer timeout, String parameterMap, Class<?> parameterType, String resultMap, Class<?> resultType, ResultSetType resultSetType, boolean flushCache, boolean useCache, boolean resultOrdered, KeyGenerator keyGenerator, String keyProperty, String keyColumn, String databaseId, LanguageDriver lang, String resultSets, boolean dirtySelect) {

            if (unresolvedCacheRef) {
                throw new IncompleteElementException("Cache-ref not yet resolved");
            }

            id = applyCurrentNamespace(id, false);

            MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlSource, sqlCommandType).resource(resource).fetchSize(fetchSize).timeout(timeout).statementType(mappedStatementType).keyGenerator(keyGenerator).keyProperty(keyProperty).keyColumn(keyColumn).databaseId(databaseId).lang(lang).resultOrdered(resultOrdered).resultSets(resultSets).resultSetType(resultSetType).flushCacheRequired(flushCache).useCache(useCache).cache(currentCache).dirtySelect(dirtySelect);

            List<ResultMap> statementResultMaps = getStatementResultMaps(resultMap, resultType, id);

            statementBuilder.resultMaps(statementResultMaps);

            ParameterMap statementParameterMap = getStatementParameterMap(parameterMap, parameterType, id);
            if (statementParameterMap != null) {
                statementBuilder.parameterMap(statementParameterMap);
            }

            MappedStatement statement = statementBuilder.build();

            if (configuration.getMappedStatement(statement.getId()) == null) {
                configuration.addMappedStatement(statement);
            }
            return statement;
        }

        @Override
        public MappedStatement addMappedStatement(String id, SqlSource sqlSource, StatementType mappedStatementType, SqlCommandType sqlCommandType, Integer fetchSize, Integer timeout, String parameterMap, Class<?> parameterType, String resultMap, Class<?> resultType, ResultSetType resultSetType, boolean flushCache, boolean useCache, boolean resultOrdered, KeyGenerator keyGenerator, String keyProperty, String keyColumn, String databaseId, LanguageDriver lang, String resultSets) {

            if (unresolvedCacheRef) {
                throw new IncompleteElementException("Cache-ref not yet resolved");
            }

            id = applyCurrentNamespace(id, false);

            List<ResultMap> statementResultMaps = getStatementResultMaps(resultMap, resultType, id);

            MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlSource, sqlCommandType).resource(resource).fetchSize(fetchSize).timeout(timeout).statementType(mappedStatementType).keyGenerator(keyGenerator).keyProperty(keyProperty).keyColumn(keyColumn).databaseId(databaseId).lang(lang).resultOrdered(resultOrdered).resultSets(resultSets).resultMaps(statementResultMaps).resultSetType(resultSetType).flushCacheRequired(flushCache).useCache(useCache).cache(currentCache);

            ParameterMap statementParameterMap = getStatementParameterMap(parameterMap, parameterType, id);
            if (statementParameterMap != null) {
                statementBuilder.parameterMap(statementParameterMap);
            }

            MappedStatement statement = statementBuilder.build();
            configuration.addMappedStatement(statement);
            return statement;
        }

        private ParameterMap getStatementParameterMap(String parameterMapName, Class<?> parameterTypeClass, String statementId) {
            parameterMapName = applyCurrentNamespace(parameterMapName, true);
            ParameterMap parameterMap = null;
            if (parameterMapName != null) {
                try {
                    parameterMap = configuration.getParameterMap(parameterMapName);
                } catch (IllegalArgumentException e) {
                    throw new IncompleteElementException("Could not find parameter map " + parameterMapName, e);
                }
            } else if (parameterTypeClass != null) {
                List<ParameterMapping> parameterMappings = new ArrayList<>();
                parameterMap = new ParameterMap.Builder(configuration, statementId + "-Inline", parameterTypeClass, parameterMappings).build();
            }
            return parameterMap;
        }

        private List<ResultMap> getStatementResultMaps(String resultMap, Class<?> resultType, String statementId) {
            resultMap = applyCurrentNamespace(resultMap, true);
            List<ResultMap> resultMaps = new ArrayList<>();
            if (resultMap != null) {
                String[] resultMapNames = resultMap.split(",");
                for (String resultMapName : resultMapNames) {
                    try {
                        resultMaps.add(configuration.getResultMap(resultMapName.trim()));
                    } catch (IllegalArgumentException e) {
                        // throw new IncompleteElementException("Could not find result map '" + resultMapName + "' referenced from '" + statementId + "'", e);
                    }
                }
            } else if (resultType != null) {
                ResultMap inlineResultMap = new ResultMap.Builder(configuration, statementId + "-Inline", resultType, new ArrayList<>(), null).build();
                resultMaps.add(inlineResultMap);
            }
            return resultMaps;
        }
    }

}
