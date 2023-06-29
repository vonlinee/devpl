package io.devpl.generator.service.impl;

import io.devpl.generator.service.MyBatisService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import io.devpl.generator.mybatis.SqlFormat;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Slf4j
@Service
public class MyBatisServiceImpl implements MyBatisService {

    @Resource
    SqlSessionFactory sqlSessionFactory;

    @Resource
    DataSource dataSource;

    @Override
    public String getExecutableSql(MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) {
        Configuration configuration = sqlSessionFactory.getConfiguration();

        ParameterHandler parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);

        try {
            // 获取数据源
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
            parameterHandler.setParameters(preparedStatement);

            String sql = preparedStatement.toString();

            int index = sql.indexOf(":");
            if (index >= 0) {
                sql = sql.substring(index + 1);
            }
            sql = sql.replace("\n", "").replace("\t", "");
            return SqlFormat.mysql(sql);
        } catch (Exception exception) {
            log.error("获取真实sql出错");
        }
        return "解析失败";
    }
}
