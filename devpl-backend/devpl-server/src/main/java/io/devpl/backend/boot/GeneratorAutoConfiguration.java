package io.devpl.backend.boot;

import io.devpl.backend.config.GeneratorProperties;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.velocity.VelocityTemplateEngine;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"io.devpl.backend"})
@EnableConfigurationProperties(GeneratorProperties.class)
public class GeneratorAutoConfiguration {

    /**
     * 模板引擎配置
     *
     * @return 模板引擎实现，默认使用Velocity
     */
    @Bean
    public TemplateEngine templateEngine() {
        return new VelocityTemplateEngine();
    }
}
