package io.devpl.codegen.core;

import io.devpl.codegen.config.DataSourceConfig;
import io.devpl.codegen.config.GlobalConfig;
import io.devpl.codegen.config.StrategyConfig;

import java.util.List;

public interface Context {

    /**
     * 初始化上下文
     */
    void initialize();

    /**
     * 收集所有生成的文件
     *
     * @param files 所有生成的文件
     */
    void generateFiles(List<GeneratedFile> files);

    StrategyConfig getStrategyConfig();

    GlobalConfig getGlobalConfig();

    DataSourceConfig getDataSourceConfig();
}
