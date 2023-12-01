package io.devpl.backend.service;

public interface GeneratorConfigService {

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
