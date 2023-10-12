package io.devpl.generator.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * mybatis-plus 配置
 */
@Configuration
public class MyBatisPlusConfiguration {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 防止全表更新与删除
        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * @return LocalDateTimeTypeHandler
     * @see LocalDateTimeTypeHandler
     */
    @Bean
    public TypeHandler<LocalDateTime> localDateTimeTypeHandler() {
        return new BaseTypeHandler<>() {

            @Override
            public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType)
                    throws SQLException {
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
}
