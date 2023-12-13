package com.baomidou.mybatisplus.generator.config.builder;

import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.IntrospectedTable;
import com.baomidou.mybatisplus.generator.query.DatabaseIntrospector;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置汇总 传递给文件生成工具
 * 大多数类都依赖此类
 * 单个数据库实例对应一个Context
 */
public class Context {

    /**
     * 模板路径配置信息
     */
    private final TemplateConfig templateConfig;

    /**
     * 数据库表信息
     */
    private final List<IntrospectedTable> introspectedTables = new ArrayList<>();

    /**
     * 路径配置信息
     */
    private final Map<OutputFile, String> pathInfo = new HashMap<>();
    /**
     * 包配置信息
     */
    private final PackageConfig packageConfig;
    /**
     * 数据库配置信息
     */
    private final DataSourceConfig dataSourceConfig;
    /**
     * 策略配置信息
     */
    private StrategyConfig strategyConfig;
    /**
     * 全局配置信息
     */
    private GlobalConfig globalConfig;
    /**
     * 注入配置信息
     */
    private InjectionConfig injectionConfig;
    /**
     * 数据查询实例
     *
     * @since 3.5.3
     */
    private DatabaseIntrospector databaseIntrospector;

    /**
     * 在构造器中处理配置
     *
     * @param packageConfig    包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     * @param templateConfig   模板配置
     * @param globalConfig     全局配置
     */
    public Context(PackageConfig packageConfig, DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig, TemplateConfig templateConfig, GlobalConfig globalConfig, InjectionConfig injectionConfig) {
        this.dataSourceConfig = dataSourceConfig;
        this.strategyConfig = strategyConfig;
        this.globalConfig = globalConfig;
        this.templateConfig = templateConfig;
        this.packageConfig = packageConfig;
        this.injectionConfig = injectionConfig;
        this.pathInfo.putAll(initOutputPathInfo(globalConfig.getOutputDir(), globalConfig.isKotlin(), packageConfig, templateConfig));
    }

    /**
     * 初始化路径信息
     *
     * @param outputDir      根路径
     * @param isKotlin       是否使用kotlin
     * @param packageConfig  包配置信息
     * @param templateConfig 模板配置信息
     * @return 路径信息，各类型的文件放在哪个目录
     */
    private Map<OutputFile, String> initOutputPathInfo(String outputDir, boolean isKotlin, PackageConfig packageConfig, TemplateConfig templateConfig) {
        PathInfoHandler pathInfoHandler = new PathInfoHandler(outputDir, packageConfig);
        pathInfoHandler.setDefaultPathInfo(isKotlin, templateConfig);
        return pathInfoHandler.getPathInfo();
    }

    public TemplateConfig getTemplateConfig() {
        return templateConfig;
    }

    public List<IntrospectedTable> introspectTables() {
        if (introspectedTables.isEmpty()) {
            // 获取表过滤
            String tableNamePattern = null;
            if (strategyConfig.getLikeTable() != null) {
                tableNamePattern = strategyConfig.getLikeTable().getValue();
            }
            // 是否跳过视图
            boolean skipView = strategyConfig.isSkipView();
            // 查询的表类型
            String[] tableTypes = skipView ? new String[]{"TABLE"} : new String[]{"TABLE", "VIEW"};
            String schemaPattern = dataSourceConfig.getSchemaName();
            List<IntrospectedTable> tableInfos = this.databaseIntrospector.getTables(schemaPattern, tableNamePattern, tableTypes);
            if (!tableInfos.isEmpty()) {
                this.introspectedTables.addAll(tableInfos);
            }
        }
        return introspectedTables;
    }

    public Map<OutputFile, String> getPathInfo() {
        return pathInfo;
    }

    public StrategyConfig getStrategyConfig() {
        return strategyConfig;
    }

    public Context setStrategyConfig(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
        return this;
    }

    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public Context setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }

    @Nullable
    public InjectionConfig getInjectionConfig() {
        return injectionConfig;
    }

    public Context setInjectionConfig(InjectionConfig injectionConfig) {
        this.injectionConfig = injectionConfig;
        return this;
    }

    public PackageConfig getPackageConfig() {
        return packageConfig;
    }

    public DataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }

    public void setDatabaseIntrospection(DatabaseIntrospector databaseIntrospector) {
        this.databaseIntrospector = databaseIntrospector;
        if (databaseIntrospector != null) {
            databaseIntrospector.setContext(this);
        }
    }
}
