package io.devpl.generator.service;

import io.devpl.generator.config.template.GeneratorInfo;

public interface GeneratorConfigService {

    GeneratorInfo getGeneratorInfo();

    String getCodeGenConfigContent();

    boolean saveGeneratorConfig(String content);
}
