package io.devpl.generator.mybatis;

import org.apache.ibatis.session.Configuration;

/**
 * 模拟MyBatis的配置项
 */
public class MyBaticMockConfiguration extends Configuration {

    Configuration configuration;

    public MyBaticMockConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
