package io.devpl.backend.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import io.devpl.backend.common.JsonTypeHandler;
import io.devpl.backend.common.ValueEnum;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * mybatis-plus 配置
 *
 * @see com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration
 */
@Configuration(proxyBeanMethods = false)
public class MyBatisPlusConfiguration implements InitializingBean {

    /**
     * 自定义MyBatis配置项
     * 会注入一个多余的ConfigurationCustomizer对象到Spring容器
     *
     * @return 自定义MyBatis配置项回调
     */
    @Bean
    public ConfigurationCustomizer customConfiguration(DataSource dataSource) {
        return configuration -> {
            TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
            registry.register(Map.class, JdbcType.VARCHAR, new JsonTypeHandler());
        };
    }

    /**
     * 拦截器配置
     *
     * @return 拦截器
     */
    @Bean
    @ConditionalOnMissingBean(value = {MybatisPlusInterceptor.class})
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 防止全表更新与删除
        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    @Bean
    public IdentifierGenerator idGenerator() {
        return new ULIDGenerator();
    }

    /**
     * LocalDateTime类型处理器
     *
     * @return LocalDateTimeTypeHandler
     * @see LocalDateTimeTypeHandler
     */
    @Bean
    public TypeHandler<LocalDateTime> localDateTimeTypeHandler() {
        return new BaseTypeHandler<>() {

            @Override
            public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
                ps.setString(i, parameter.toString());
            }

            @Override
            public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
                return rs.getObject(columnName, LocalDateTime.class);
            }

            @Override
            public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
                return rs.getObject(columnIndex, LocalDateTime.class);
            }

            @Override
            public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
                return cs.getObject(columnIndex, LocalDateTime.class);
            }
        };
    }

    /**
     * 通用字段填充逻辑
     *
     * @return MetaObjectHandler
     */
    @Bean
    public MetaObjectHandler commonFieldFillHandler() {
        return new MetaObjectHandler() {
            /**
             * 新增时填充
             * @param metaObject 元对象
             */
            @Override
            public void insertFill(MetaObject metaObject) {
                LocalDateTime nowTime = LocalDateTime.now();
                if (metaObject.hasGetter("updateTime")) {
                    setFieldValByName("updateTime", nowTime, metaObject);
                }
                if (metaObject.hasGetter("createTime")) {
                    setFieldValByName("createTime", nowTime, metaObject);
                }
                if (metaObject.hasSetter("is_deleted")) {
                    setFieldValByName("is_deleted", false, metaObject);
                }
            }

            /**
             * 更新时填充
             * @param metaObject 元对象
             */
            @Override
            public void updateFill(MetaObject metaObject) {
                if (metaObject.hasGetter("updateTime")) {
                    setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
                }
            }
        };
    }

    /**
     * 使用ULID作为主键
     * <a href="https://github.com/ulid/spec">ULID Specification</a>
     * <a href="https://github.com/f4b6a3/ulid-creator">...</a>
     *
     * @see com.baomidou.mybatisplus.annotation.IdType
     */
    static class ULIDGenerator implements IdentifierGenerator {

        @Override
        public boolean assignId(Object idValue) {
            return true;
        }

        @Override
        public Number nextId(Object entity) {
            return null;
        }

        /**
         * 生成ulid
         *
         * @param entity 实体
         * @return uuid
         */
        @Override
        public String nextUUID(Object entity) {
            Ulid ulid = UlidCreator.getMonotonicUlid();
            return ulid.toString();
        }
    }

    /**
     * Boolean类型字段处理
     *
     * @see org.apache.ibatis.type.BooleanTypeHandler
     */
    static class BooleanTypeHandlerWrapper extends BooleanTypeHandler {

        TypeHandler<Boolean> handler;

        public BooleanTypeHandlerWrapper(TypeHandler<Boolean> handler) {
            this.handler = Objects.requireNonNull(handler, "handler cannot be null");
        }

        @Override
        public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
            Boolean val = super.getNullableResult(rs, columnName);
            return val != null && val;
        }

        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
            super.setNonNullParameter(ps, i, parameter, jdbcType);
        }
    }

    /**
     * 统一数据库枚举类型字段，一般是tinyint
     *
     * @see ValueEnum
     */
    static class ValueEnumTypeHandler extends BaseTypeHandler<ValueEnum<?, ?>> {

        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, ValueEnum<?, ?> parameter, JdbcType jdbcType) throws SQLException {

        }

        @Override
        public ValueEnum<?, ?> getNullableResult(ResultSet rs, String columnName) throws SQLException {
            return null;
        }

        @Override
        public ValueEnum<?, ?> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            return null;
        }

        @Override
        public ValueEnum<?, ?> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            return null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
