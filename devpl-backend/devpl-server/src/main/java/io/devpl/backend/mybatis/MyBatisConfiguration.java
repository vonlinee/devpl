package io.devpl.backend.mybatis;

import org.apache.ibatis.session.Configuration;

/**
 * 模拟MyBatis的配置项
 */
public class MyBatisConfiguration extends Configuration {

    Configuration configuration;

    public MyBatisConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
