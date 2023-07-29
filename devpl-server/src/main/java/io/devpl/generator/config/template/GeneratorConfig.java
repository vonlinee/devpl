package io.devpl.generator.config.template;

import io.devpl.generator.common.exception.ServerException;
import io.devpl.generator.common.utils.JSONUtils;
import io.devpl.generator.entity.TemplateInfo;
import io.devpl.sdk.io.IOUtils;
import io.devpl.sdk.util.StringUtils;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 代码生成配置内容
 */
public class GeneratorConfig {
    private String template;

    public GeneratorConfig(String template) {
        this.template = template;
    }
}
