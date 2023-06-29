package io.devpl.generator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 生成器配置属性
 */
@Data
@ConfigurationProperties("codegen")
public class GeneratorProperties {

    /**
     * 模板路径
     */
    private String template = "/template/";
}
