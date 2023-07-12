package io.devpl.generator.mybatis;

import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyXmlStatemtnBuilder extends XMLStatementBuilder {

    private final Logger logger = LoggerFactory.getLogger(MyXmlStatemtnBuilder.class);

    // 覆盖父类的final属性
    private XNode context;

    public MyXmlStatemtnBuilder(Configuration configuration, XNode context) {
        // mapper文件路径
        this(configuration, new MyMapperBuilderAssistant(configuration, null), context, null);
    }

    MyXmlStatemtnBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context, String databaseId) {
        super(configuration, builderAssistant, context, databaseId);
    }

    @Override
    protected <T> Class<? extends T> resolveClass(String alias) {
        Class<? extends T> clazz;
        try {
            clazz = super.resolveClass(alias);
        } catch (Exception exception) {
            // 手动忽略不存在的类型
            // logger.error("cannot resolve calss", exception);
        }
        clazz = (Class<? extends T>) Integer.class;
        return clazz;
    }

    static class MyMapperBuilderAssistant extends MapperBuilderAssistant {

        private String currentNamespace;
        private String resource;
        private Cache currentCache;
        private boolean unresolvedCacheRef; // issue #676

        public MyMapperBuilderAssistant(Configuration configuration, String resource) {
            super(configuration, resource);
        }

        @Override
        public MappedStatement addMappedStatement(String id, SqlSource sqlSource, StatementType statementType, SqlCommandType sqlCommandType, Integer fetchSize, Integer timeout, String parameterMap, Class<?> parameterType, String resultMap, Class<?> resultType, ResultSetType resultSetType, boolean flushCache, boolean useCache, boolean resultOrdered, KeyGenerator keyGenerator, String keyProperty, String keyColumn, String databaseId, LanguageDriver lang, String resultSets) {
            this.currentNamespace = (String) ReflectionUtils.getValue(this, "currentNamespace");
            this.resource = (String) ReflectionUtils.getValue(this, "resource");
            this.currentCache = (Cache) ReflectionUtils.getValue(this, "currentCache");
            Boolean unresolvedCacheRefValue = (Boolean) ReflectionUtils.getValue(this, "unresolvedCacheRef", false);
            if (unresolvedCacheRefValue != null) {
                this.unresolvedCacheRef = unresolvedCacheRefValue;
            }

            if (unresolvedCacheRef) {
                throw new IncompleteElementException("Cache-ref not yet resolved");
            }

            id = applyCurrentNamespace(id, false);
            boolean isSelect = sqlCommandType == SqlCommandType.SELECT;

            List<ResultMap> resultMaps;
            try {
                resultMaps = getStatementResultMaps(resultMap, resultType, id);
            } catch (Exception exception) {
                resultMaps = Collections.emptyList();
            }

            MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlSource, sqlCommandType)
                .resource(resource)
                .fetchSize(fetchSize)
                .timeout(timeout)
                .statementType(statementType)
                .keyGenerator(keyGenerator)
                .keyProperty(keyProperty)
                .keyColumn(keyColumn)
                .databaseId(databaseId)
                .lang(lang)
                .resultOrdered(resultOrdered)
                .resultSets(resultSets)
                .resultMaps(resultMaps)
                .resultSetType(resultSetType)
                .flushCacheRequired(valueOrDefault(flushCache, !isSelect))
                .useCache(valueOrDefault(useCache, isSelect))
                .cache(currentCache);

            ParameterMap statementParameterMap = getStatementParameterMap(parameterMap, parameterType, id);
            if (statementParameterMap != null) {
                statementBuilder.parameterMap(statementParameterMap);
            }

            MappedStatement statement = statementBuilder.build();
            configuration.addMappedStatement(statement);
            return statement;
        }

        private <T> T valueOrDefault(T value, T defaultValue) {
            return value == null ? defaultValue : value;
        }

        private ParameterMap getStatementParameterMap(
            String parameterMapName,
            Class<?> parameterTypeClass,
            String statementId) {
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
                parameterMap = new ParameterMap.Builder(
                    configuration,
                    statementId + "-Inline",
                    parameterTypeClass,
                    parameterMappings).build();
            }
            return parameterMap;
        }

        private List<ResultMap> getStatementResultMaps(
            String resultMap,
            Class<?> resultType,
            String statementId) {
            resultMap = applyCurrentNamespace(resultMap, true);

            List<ResultMap> resultMaps = new ArrayList<>();
            if (resultMap != null) {
                String[] resultMapNames = resultMap.split(",");
                for (String resultMapName : resultMapNames) {
                    try {
                        resultMaps.add(configuration.getResultMap(resultMapName.trim()));
                    } catch (IllegalArgumentException e) {
                        throw new IncompleteElementException("Could not find result map '" + resultMapName + "' referenced from '" + statementId + "'", e);
                    }
                }
            } else if (resultType != null) {
                ResultMap inlineResultMap = new ResultMap.Builder(
                    configuration,
                    statementId + "-Inline",
                    resultType,
                    new ArrayList<>(),
                    null).build();
                resultMaps.add(inlineResultMap);
            }
            return resultMaps;
        }
    }
}
