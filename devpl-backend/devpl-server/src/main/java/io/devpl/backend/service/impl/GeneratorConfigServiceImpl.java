package io.devpl.backend.service.impl;

import io.devpl.backend.common.ServerException;
import io.devpl.backend.utils.JSONUtils;
import io.devpl.backend.config.GeneratorProperties;
import io.devpl.backend.config.template.GeneratorInfo;
import io.devpl.backend.entity.TemplateInfo;
import io.devpl.backend.service.GeneratorConfigService;
import io.devpl.backend.utils.PathUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 生成器配置Service
 */
@Slf4j
@Service
public class GeneratorConfigServiceImpl implements GeneratorConfigService {

    @Resource
    private GeneratorProperties properties;

    private String root;

    @PostConstruct
    public void initGeneratorConfig() {
        root = new File("").getAbsolutePath() + File.separator + "devpl-server";
    }

    @Override
    public String getCodeGenConfigContent() {
        InputStream inputStream = loadConfigFile(properties.getTemplate());
        String result;
        try {
            result = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public boolean saveGeneratorConfig(String content) {
        JSONUtils.validateJson(content);
        String path = PathUtils.of(root, "src/main/resources", properties.getTemplate(), "config.json");
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException(file.getPath() + "文件不存在");
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    InputStream loadConfigFile(String template) {
        // 模板路径，如果不是以/结尾，则添加/
        if (!StringUtils.endWith(template, '/')) {
            template = template + "/";
        }
        // 模板配置文件
        InputStream isConfig = this.getClass().getResourceAsStream(template + "config.json");
        if (isConfig == null) {
            throw new ServerException("模板配置文件，config.json不存在");
        }
        return isConfig;
    }

    public GeneratorInfo getGeneratorConfig(String template) {
        // 模板路径，如果不是以/结尾，则添加/
        if (!StringUtils.endWith(template, '/')) {
            template = template + "/";
        }
        // 读取模板配置文件
        String configContent;
        try {
            configContent = StreamUtils.copyToString(loadConfigFile(template), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GeneratorInfo generator = JSONUtils.parseObject(configContent, GeneratorInfo.class);
        if (generator == null) {
            return null;
        }
        try {
            for (TemplateInfo templateInfo : generator.getTemplates()) {
                // 模板文件
                InputStream isTemplate = this.getClass().getResourceAsStream(template + templateInfo.getTemplateName());
                if (isTemplate == null) {
                    throw new ServerException("模板文件 " + templateInfo.getTemplateName() + " 不存在");
                }
                // 读取模板内容
                templateInfo.setContent(StreamUtils.copyToString(isTemplate, StandardCharsets.UTF_8));
            }
            return generator;
        } catch (IOException e) {
            throw new ServerException("读取config.json配置文件失败");
        }
    }
}
