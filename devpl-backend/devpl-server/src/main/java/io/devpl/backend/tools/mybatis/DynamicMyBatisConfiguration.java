package io.devpl.backend.tools.mybatis;

import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.builder.ResultMapResolver;
import org.apache.ibatis.builder.annotation.MethodResolver;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MyBatis Configuration 能运行时修改
 * 部分方法添加synchronized修饰
 *
 * @see com.baomidou.mybatisplus.core.MybatisConfiguration
 */
public class DynamicMyBatisConfiguration extends Configuration {

    /**
     * 后台的MyBatis配置
     */
    Configuration configuration;

    protected final Map<String, MappedStatement> mappedStatements = new ConcurrentHashMap<>();

    public DynamicMyBatisConfiguration(Configuration configuration) {
        super();
        this.configuration = configuration;
    }

    @Override
    public synchronized void addMappedStatement(MappedStatement ms) {
        super.addMappedStatement(ms);
    }

    @Override
    public synchronized void addKeyGenerator(String id, KeyGenerator keyGenerator) {
        super.addKeyGenerator(id, keyGenerator);
    }

    @Override
    public synchronized void addCache(Cache cache) {
        super.addCache(cache);
    }

    @Override
    public synchronized void addLoadedResource(String resource) {
        super.addLoadedResource(resource);
    }

    @Override
    public synchronized void addResultMap(ResultMap rm) {
        super.addResultMap(rm);
    }

    @Override
    public synchronized void addParameterMap(ParameterMap pm) {
        super.addParameterMap(pm);
    }

    @Override
    public synchronized void addMappers(String packageName, Class<?> superType) {
        super.addMappers(packageName, superType);
    }

    @Override
    public synchronized void addCacheRef(String namespace, String referencedNamespace) {
        super.addCacheRef(namespace, referencedNamespace);
    }

    @Override
    public synchronized void addIncompleteStatement(XMLStatementBuilder incompleteStatement) {
        super.addIncompleteStatement(incompleteStatement);
    }

    @Override
    public synchronized void addIncompleteCacheRef(CacheRefResolver incompleteCacheRef) {
        super.addIncompleteCacheRef(incompleteCacheRef);
    }

    @Override
    public synchronized void addIncompleteResultMap(ResultMapResolver resultMapResolver) {
        super.addIncompleteResultMap(resultMapResolver);
    }

    @Override
    public synchronized void addIncompleteMethod(MethodResolver builder) {
        super.addIncompleteMethod(builder);
    }

    @Override
    public synchronized void addInterceptor(Interceptor interceptor) {
        super.addInterceptor(interceptor);
    }

    @Override
    public synchronized void addMappers(String packageName) {
        super.addMappers(packageName);
    }

    @Override
    public synchronized <T> void addMapper(Class<T> type) {
        super.addMapper(type);
    }
}
