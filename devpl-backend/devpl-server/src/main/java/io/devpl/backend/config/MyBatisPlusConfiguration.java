package io.devpl.backend.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * mybatis-plus 配置
 */
@Configuration(proxyBeanMethods = false)
public class MyBatisPlusConfiguration {

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
}
