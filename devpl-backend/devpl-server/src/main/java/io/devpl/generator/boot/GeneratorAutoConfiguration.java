package io.devpl.generator.boot;

import com.baomidou.mybatisplus.generator.engine.TemplateEngine;
import com.baomidou.mybatisplus.generator.engine.velocity.VelocityTemplateEngine;
import io.devpl.generator.config.GeneratorProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"io.devpl.generator"})
@EnableConfigurationProperties(GeneratorProperties.class)
public class GeneratorAutoConfiguration {

    @Bean
    public TemplateEngine templateEngine() {
        return new VelocityTemplateEngine();
    }
}
