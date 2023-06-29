package io.devpl.generator.config;

import io.devpl.generator.config.template.GeneratorConfig;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@ComponentScan(basePackages = {"io.devpl.generator"})
@EnableConfigurationProperties(GeneratorProperties.class)
public class GeneratorAutoConfiguration {

    private final GeneratorProperties properties;

    @Bean
    public GeneratorConfig generatorConfig() {
        return new GeneratorConfig(properties.getTemplate());
    }
}
