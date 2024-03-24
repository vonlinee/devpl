package io.devpl.codegen.core;

import io.devpl.codegen.ConstVal;
import io.devpl.codegen.config.*;
import io.devpl.codegen.db.query.DatabaseIntrospector;
import io.devpl.codegen.plugins.TableGenerationPlugin;
import io.devpl.codegen.strategy.ProjectArchetype;
import io.devpl.codegen.strategy.SimpleMavenProjectArchetype;
import io.devpl.codegen.template.velocity.VelocityTemplateEngine;
import io.devpl.codegen.util.ClassUtils;
import io.devpl.sdk.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置汇总 传递给文件生成工具
 * 大多数类都依赖此类
 * 单个数据库实例对应一个Context
 */
@Setter
@Getter
public class ContextImpl implements Context {
    /**
     * 模板路径配置信息
     */
    private final TemplateConfig templateConfig;
    /**
     * 数据库表信息
     */
    private final List<TableGeneration> targetTables = new ArrayList<>();
    /**
     * 路径配置信息
     * 路径信息，各类型的文件放在哪个目录
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
    private DatabaseIntrospector introspector;

    /**
     * 插件
     */
    private PluginManager plugin;

    /**
     * 模板引擎实现类
     */
    private String templateEngineImpl = VelocityTemplateEngine.class.getName();

    /**
     * 用于定位文件位置
     */
    private ProjectArchetype fileLocator = new SimpleMavenProjectArchetype();

    /**
     * 在构造器中处理配置
     *
     * @param packageConfig    包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     * @param templateConfig   模板配置
     * @param globalConfig     全局配置
     */
    public ContextImpl(PackageConfig packageConfig, DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig, TemplateConfig templateConfig, GlobalConfig globalConfig, InjectionConfig injectionConfig) {
        this.dataSourceConfig = dataSourceConfig;
        this.strategyConfig = strategyConfig;
        this.globalConfig = globalConfig;
        this.templateConfig = templateConfig;
        this.packageConfig = packageConfig;
        this.injectionConfig = injectionConfig;
    }

    /**
     * 初始化路径信息
     *
     * @param outputDir      根路径
     * @param isKotlin       是否使用kotlin
     * @param packageConfig  包配置信息
     * @param templateConfig 模板配置信息
     */
    private void initOutputPathInfo(String outputDir, boolean isKotlin, PackageConfig packageConfig, TemplateConfig templateConfig) {
        // 输出文件Map
        Map<OutputFile, String> pathInfo = new HashMap<>();
        putPathInfo(pathInfo, outputDir, templateConfig.getEntityTemplatePath(isKotlin), OutputFile.ENTITY, ConstVal.ENTITY);
        putPathInfo(pathInfo, outputDir, templateConfig.getMapper(), OutputFile.MAPPER, ConstVal.MAPPER);
        putPathInfo(pathInfo, outputDir, templateConfig.getXml(), OutputFile.MAPPER_XML, ConstVal.XML);
        putPathInfo(pathInfo, outputDir, templateConfig.getService(), OutputFile.SERVICE, ConstVal.SERVICE);
        putPathInfo(pathInfo, outputDir, templateConfig.getServiceImpl(), OutputFile.SERVICE_IMPL, ConstVal.SERVICE_IMPL);
        putPathInfo(pathInfo, outputDir, templateConfig.getController(), OutputFile.CONTROLLER, ConstVal.CONTROLLER);
        pathInfo.putIfAbsent(OutputFile.PARENT, joinPath(outputDir, packageConfig.getPackageInfo(ConstVal.PARENT)));
        // 如果配置了包路径，则覆盖自定义路径
        if (!pathInfo.isEmpty()) {
            this.pathInfo.putAll(pathInfo);
        }
    }

    /**
     * @param template   模板路径
     * @param outputFile 输出文件类型
     * @param module     模块名称
     */
    private void putPathInfo(Map<OutputFile, String> pathInfo, String outputDir, String template, OutputFile outputFile, String module) {
        if (StringUtils.hasText(template)) {
            pathInfo.putIfAbsent(outputFile, joinPath(outputDir, packageConfig.getPackageInfo(module)));
        }
    }

    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (!StringUtils.hasText(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }

    /**
     * 初始化插件
     */
    @Override
    public void initialize() {
        this.plugin = new PluginManager();
        addPlugin(new TableGenerationPlugin());

        // 初始化输出路径信息
        initOutputPathInfo(globalConfig.getOutputDir(), globalConfig.isKotlin(), packageConfig, templateConfig);

        // 初始化Context组件
        this.setDatabaseIntrospection(ClassUtils.instantiate(dataSourceConfig.getDatabaseQueryClass()));
    }

    public final void addPlugin(Plugin plugin) {
        // 注入上下文
        plugin.setContext(this);
        // 表生成插件
        this.plugin.addPlugin(plugin);
    }

    /**
     * 获取表信息
     *
     * @return 表信息
     */
    public List<TableGeneration> introspectTables(String tableNamePattern) {
        if (targetTables.isEmpty()) {
            // 是否跳过视图
            boolean skipView = strategyConfig.isSkipView();
            // 查询的表类型
            String[] tableTypes = skipView ? new String[]{"TABLE"} : new String[]{"TABLE", "VIEW"};
            String schemaPattern = dataSourceConfig.getSchemaName();

            List<TableGeneration> tableInfos = this.introspector.getTables(null, schemaPattern, tableNamePattern, tableTypes);
            if (!tableInfos.isEmpty()) {
                this.targetTables.addAll(tableInfos);
            }
        }

        for (TableGeneration tableGeneration : targetTables) {
            plugin.initialize(tableGeneration);
        }

        return targetTables;
    }

    public String getPathInfo(OutputFile outputFile) {
        return pathInfo.get(outputFile);
    }

    public void setDatabaseIntrospection(DatabaseIntrospector databaseIntrospector) {
        this.introspector = databaseIntrospector;
        if (databaseIntrospector != null) {
            databaseIntrospector.setContext(this);
        }
    }

    /**
     * 得到要生成的文件信息
     *
     * @param files 存放结果
     */
    @Override
    public final void generateFiles(List<GeneratedFile> files) {
        for (TableGeneration tableGeneration : targetTables) {
            plugin.generateFiles(tableGeneration, files);
        }
    }

    public boolean useKotlin() {
        return globalConfig.isKotlin();
    }
}