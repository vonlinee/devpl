package io.devpl.backend.config;

import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 生成器配置属性
 */
@Getter
@Setter
@ConfigurationProperties("codegen")
public class GeneratorProperties {

    /**
     * 模板路径
     */
    private String template = "/template/";

    @PostConstruct
    public void init() {
        // 模板路径，如果不是以/结尾，则添加/
        template = StringUtils.withEnd(template, "/");
    }
}
