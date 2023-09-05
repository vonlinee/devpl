package io.devpl.generator.service;

import io.devpl.generator.config.template.GeneratorInfo;

public interface GeneratorConfigService {

    /**
     * 获取生成器配置
     * @param refresh 是否获取最新的配置
     * @return 生成器配置
     */
    GeneratorInfo getGeneratorInfo(boolean refresh);

    /**
     * 将生成器配置转换为JSON文本，配置存放在json配置文件中
     * @return JSON字符串
     */
    String getCodeGenConfigContent();

    /**
     * 保存生成器配置到json配置文件中
     * @param content JSON字符串
     */
    boolean saveGeneratorConfig(String content);
}
