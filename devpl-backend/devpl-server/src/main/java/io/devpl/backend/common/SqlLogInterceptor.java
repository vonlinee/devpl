package io.devpl.backend.common;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 自定义mybatis插件，实现输出实际执行sql语句
 */
@Profile("dev")
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
    @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
@Component
public class SqlLogInterceptor implements Interceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlLogInterceptor.class);
    /**
     * 获取配置中需要拦截的表
     */
    private final List<String> tableNames = List.of("pms_category");

    /**
     * 忽略插入sql_log表的语句
     */
    private static final String IGNORE_SQL_PREFIX = "insert into sql_log";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (CollectionUtils.isEmpty(tableNames)) {
            //继续执行
            return invocation.proceed();
        }
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String sql = boundSql.getSql().replaceAll("\\s+", " ").toLowerCase();
        if (sql.toLowerCase().startsWith(IGNORE_SQL_PREFIX)) {
            //忽略自己 记录 sql 语句入库的sql
            return invocation.proceed();
        }
        List<ParameterMapping> parameterMappings = new ArrayList<>(boundSql.getParameterMappings());
        Object parameterObject = boundSql.getParameterObject();
        if (parameterMappings.isEmpty() && parameterObject == null) {
            return invocation.proceed();
        }
        Configuration configuration = mappedStatement.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        try {
            String parameter = "null";
            MetaObject newMetaObject = configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() == ParameterMode.OUT) {
                    continue;
                }
                String propertyName = parameterMapping.getProperty();
                if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    parameter = getParameterValue(parameterObject);
                } else if (newMetaObject.hasGetter(propertyName)) {
                    parameter = getParameterValue(newMetaObject.getValue(propertyName));
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    parameter = getParameterValue(boundSql.getAdditionalParameter(propertyName));
                }
                // fixme 此处不严谨，若sql语句中有❓，则替换错位。?️
                sql = sql.replaceFirst("\\?", parameter);
            }
            // 将拦截到的sql语句插入日志表中

            //sqlLogService.save(SqlLog.builder().executedSql(sql).build());
        } catch (Exception e) {

        }
        return invocation.proceed();
    }

    /**
     * 获取参数
     *
     * @param param Object类型参数
     * @return 转换之后的参数
     */
    private static String getParameterValue(Object param) {
        if (param == null) {
            return "null";
        }
        if (param instanceof Number) {
            return param.toString();
        }
        String value = null;
        if (param instanceof String) {
            value = param.toString();
        } else if (param instanceof Date) {
            //TODO  引入依赖

        } else {
            value = param.toString();
        }
        return StringUtils.quotaMark(value);
    }


    @Override
    public Object plugin(Object o) {
        if (o instanceof StatementHandler) {
            return Plugin.wrap(o, this);
        }
        return o;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

