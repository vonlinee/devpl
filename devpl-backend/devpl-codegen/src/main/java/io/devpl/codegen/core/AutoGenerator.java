package io.devpl.codegen.core;

import io.devpl.codegen.ConstVal;
import io.devpl.codegen.config.*;
import io.devpl.codegen.db.query.DatabaseIntrospector;
import io.devpl.codegen.template.AbstractTemplateEngine;
import io.devpl.codegen.template.velocity.VelocityTemplateEngine;
import io.devpl.codegen.util.FileUtils;
import io.devpl.codegen.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;

/**
 * 生成文件
 */
public class AutoGenerator {

    private static final Logger log = LoggerFactory.getLogger(AutoGenerator.class);
    /**
     * 数据源配置
     */
    private final DataSourceConfig dataSourceConfig;
    /**
     * 配置信息
     */
    protected Context context;
    /**
     * 注入配置
     */
    protected InjectionConfig injectionConfig;
    /**
     * 模板引擎
     */
    protected AbstractTemplateEngine templateEngine;
    /**
     * 数据库表配置
     */
    private StrategyConfig strategyConfig;
    /**
     * 包 相关配置
     */
    private PackageConfig packageConfig;
    /**
     * 模板 相关配置
     */
    private TemplateConfig templateConfig;
    /**
     * 全局 相关配置
     */
    private GlobalConfig globalConfig;
    private ActionCallback callback;

    /**
     * 构造方法
     *
     * @param dataSourceConfig 数据库配置
     * @since 3.5.0
     */
    public AutoGenerator(DataSourceConfig dataSourceConfig) {
        // 这个是必须参数,其他都是可选的,后续去除默认构造更改成final
        this.dataSourceConfig = dataSourceConfig;
    }

    /**
     * 注入配置
     *
     * @param injectionConfig 注入配置
     * @return this
     * @since 3.5.0
     */
    public AutoGenerator injection(InjectionConfig injectionConfig) {
        this.injectionConfig = injectionConfig;
        return this;
    }

    /**
     * 生成策略
     *
     * @param strategyConfig 策略配置
     * @return this
     * @since 3.5.0
     */
    public AutoGenerator strategy(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
        return this;
    }

    /**
     * 指定包配置信息
     *
     * @param packageConfig 包配置
     * @return this
     * @since 3.5.0
     */
    public AutoGenerator packageInfo(PackageConfig packageConfig) {
        this.packageConfig = packageConfig;
        return this;
    }

    /**
     * 指定模板配置
     *
     * @param templateConfig 模板配置
     * @return this
     * @since 3.5.0
     */
    public AutoGenerator template(TemplateConfig templateConfig) {
        this.templateConfig = templateConfig;
        return this;
    }

    /**
     * 指定全局配置
     *
     * @param globalConfig 全局配置
     * @return this
     * @see 3.5.0
     */
    public AutoGenerator global(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }

    /**
     * 设置配置汇总
     *
     * @param context 配置汇总
     * @return this
     * @since 3.5.0
     */
    public AutoGenerator config(Context context) {
        this.context = context;
        return this;
    }

    /**
     * 生成代码
     */
    public void execute() {
        this.execute(null);
    }

    /**
     * 生成代码
     *
     * @param templateEngine 模板引擎
     */
    public void execute(AbstractTemplateEngine templateEngine) {
        log.debug("==========================准备生成文件...==========================");
        // 初始化配置
        if (null == context) {
            initContext();
        }
        // 为了兼容之前逻辑，默认采用 Velocity 引擎
        this.templateEngine = templateEngine == null ? new VelocityTemplateEngine() : templateEngine;
        this.callback = globalConfig.callback;

        // 获取表过滤
        String tableNamePattern = null;
        if (strategyConfig.getLikeTable() != null) {
            tableNamePattern = strategyConfig.getLikeTable().getValue();
        }

        // 获取所有的表信息
        List<TableGeneration> tableInfoList = context.introspectTables(tableNamePattern);
        for (TableGeneration introspectedTable : tableInfoList) {
            generateFiles(introspectedTable);
        }
        open();
    }

    void initContext() {
        if (this.strategyConfig == null) {
            this.strategyConfig = StrategyConfig.builder().build();
        }
        this.globalConfig = Optional.ofNullable(globalConfig).orElse(new GlobalConfig.Builder().build());
        this.templateConfig = Optional.ofNullable(templateConfig).orElse(new TemplateConfig.Builder().build());
        this.packageConfig = Optional.ofNullable(packageConfig).orElse(new PackageConfig.Builder().build());
        this.injectionConfig = Optional.ofNullable(injectionConfig).orElse(new InjectionConfig.Builder().build());
        context = new Context(packageConfig, dataSourceConfig, strategyConfig, templateConfig, globalConfig, injectionConfig);

        context.initialize();

        // 初始化Context组件
        Class<? extends DatabaseIntrospector> databaseQueryClass = dataSourceConfig.getDatabaseQueryClass();
        try {
            Constructor<? extends DatabaseIntrospector> declaredConstructor = databaseQueryClass.getDeclaredConstructor();
            DatabaseIntrospector databaseIntrospector = declaredConstructor.newInstance();
            context.setDatabaseIntrospection(databaseIntrospector);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("创建DatabaseIntrospector实例出现错误:", exception);
        }
    }

    /**
     * 准备生成的文件
     *
     * @param introspectedTable 表信息
     * @return 生成的文件列表
     */
    public List<GeneratedFile> prepareGeneratedFiles(TableGeneration introspectedTable) {
        return Collections.emptyList();
    }

    /**
     * 生成文件
     *
     * @param introspectedTable 表信息
     */
    void generateFiles(TableGeneration introspectedTable) {

        List<GeneratedFile> generatedFiles = prepareGeneratedFiles(introspectedTable);

        log.info("文件个数{}", generatedFiles.size());

        // 数据初始化完毕
        try {
            // 填充模板参数
            log.info("填充模板参数");
            Map<String, Object> templateArgumentsMap = this.prepareTemplateArguments(context, introspectedTable);
            InjectionConfig injectionConfig = context.getInjectionConfig();
            if (injectionConfig != null) {
                // 添加自定义属性
                injectionConfig.beforeOutputFile(introspectedTable, templateArgumentsMap);
                // 输出自定义文件
                outputCustomFile(injectionConfig.getCustomFiles(), introspectedTable, templateArgumentsMap);
            }
            // entity
            outputEntity(introspectedTable, templateArgumentsMap);
            // mapper and xml
            outputMapper(introspectedTable, templateArgumentsMap);
            // service
            outputService(introspectedTable, templateArgumentsMap);
            // controller
            outputController(introspectedTable, templateArgumentsMap);
        } catch (Exception e) {
            throw new RuntimeException("无法创建文件，请检查配置信息！", e);
        }
    }

    /**
     * 输出自定义模板文件
     *
     * @param customFiles 自定义模板文件列表
     * @param tableInfo   表信息
     * @param objectMap   渲染数据
     * @since 3.5.3
     */
    public void outputCustomFile(List<CustomFile> customFiles, TableGeneration tableInfo, Map<String, Object> objectMap) {
        String entityName = tableInfo.getEntityName();
        String parentPath = context.getPathInfo(OutputFile.PARENT);
        customFiles.forEach(file -> {
            String filePath = StringUtils.hasText(file.getFilePath()) ? file.getFilePath() : parentPath;
            if (StringUtils.hasText(file.getPackageName())) {
                filePath = filePath + File.separator + file.getPackageName();
                filePath = filePath.replaceAll("\\.", "\\" + File.separator);
            }
            String fileName = filePath + File.separator + entityName + file.getFileName();
            outputFile(new File(fileName), objectMap, file.getTemplatePath(), file.isFileOverride());
        });
    }

    /**
     * 输出实体文件
     *
     * @param tableInfo 表信息
     * @param objectMap 渲染数据
     */
    public void outputEntity(TableGeneration tableInfo, Map<String, Object> objectMap) {
        String entityName = tableInfo.getEntityName();
        String entityPath = context.getPathInfo(OutputFile.ENTITY);
        if (StringUtils.hasText(entityName) && StringUtils.hasText(entityPath)) {
            getTemplateFilePath(template -> template.getEntityTemplatePath(context.getGlobalConfig()
                .isKotlin())).ifPresent((entity) -> {
                String entityFile = String.format((entityPath + File.separator + "%s" + suffixJavaOrKt()), entityName);
                outputFile(new File(entityFile), objectMap, entity, context.getStrategyConfig().entity()
                    .isFileOverride());
            });
        }
    }

    /**
     * 文件后缀
     */
    protected String suffixJavaOrKt() {
        return context.getGlobalConfig().isKotlin() ? ConstVal.KT_SUFFIX : ConstVal.JAVA_SUFFIX;
    }

    /**
     * 输出Mapper文件(含xml)
     *
     * @param tableInfo 表信息
     * @param objectMap 渲染数据
     * @since 3.5.0
     */
    public void outputMapper(TableGeneration tableInfo, Map<String, Object> objectMap) {
        // MpMapper.java
        String entityName = tableInfo.getEntityName();
        String mapperPath = context.getPathInfo(OutputFile.MAPPER);
        if (StringUtils.hasText(tableInfo.getMapperName()) && StringUtils.hasText(mapperPath)) {
            getTemplateFilePath(TemplateConfig::getMapper).ifPresent(mapper -> {
                String mapperFile = String.format((mapperPath + File.separator + tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
                outputFile(new File(mapperFile), objectMap, mapper, context.getStrategyConfig().mapper()
                    .isFileOverride());
            });
        }
        // MpMapper.xml
        String xmlPath = context.getPathInfo(OutputFile.MAPPER_XML);
        if (StringUtils.hasText(tableInfo.getXmlName()) && StringUtils.hasText(xmlPath)) {
            getTemplateFilePath(TemplateConfig::getXml).ifPresent(xml -> {
                String xmlFile = String.format((xmlPath + File.separator + tableInfo.getXmlName() + ConstVal.XML_SUFFIX), entityName);
                outputFile(new File(xmlFile), objectMap, xml, context.getStrategyConfig().mapper()
                    .isFileOverride());
            });
        }
    }

    /**
     * 输出service文件
     *
     * @param tableInfo 表信息
     * @param objectMap 渲染数据
     * @since 3.5.0
     */
    public void outputService(TableGeneration tableInfo, Map<String, Object> objectMap) {
        // IMpService.java
        String entityName = tableInfo.getEntityName();
        String servicePath = context.getPathInfo(OutputFile.SERVICE);
        if (StringUtils.hasText(tableInfo.getServiceName()) && StringUtils.hasText(servicePath)) {
            getTemplateFilePath(TemplateConfig::getService).ifPresent(service -> {
                String serviceFile = String.format((servicePath + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
                outputFile(new File(serviceFile), objectMap, service, context.getStrategyConfig().service()
                    .isFileOverride());
            });
        }
        // MpServiceImpl.java
        String serviceImplPath = context.getPathInfo(OutputFile.SERVICE_IMPL);
        if (StringUtils.hasText(tableInfo.getServiceImplName()) && StringUtils.hasText(serviceImplPath)) {
            getTemplateFilePath(TemplateConfig::getServiceImpl).ifPresent(serviceImpl -> {
                String implFile = String.format((serviceImplPath + File.separator + tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
                outputFile(new File(implFile), objectMap, serviceImpl, context.getStrategyConfig().service()
                    .isFileOverride());
            });
        }
    }

    /**
     * 获取模板路径
     *
     * @param function function
     * @return 模板路径
     * @since 3.5.0
     */
    protected Optional<String> getTemplateFilePath(Function<TemplateConfig, String> function) {
        String filePath = function.apply(context.getTemplateConfig());
        if (StringUtils.hasText(filePath)) {
            return Optional.of(getPathOfTemplateFile(filePath, templateEngine.getTemplateFileExtension()));
        }
        return Optional.empty();
    }

    public String getPathOfTemplateFile(String filePath, String templateFileExtension) {
        return filePath.endsWith(templateFileExtension) ? filePath : filePath + templateFileExtension;
    }

    /**
     * 输出文件
     *
     * @param file         文件
     * @param objectMap    渲染信息
     * @param templatePath 模板路径
     * @param fileOverride 是否覆盖已有文件
     * @since 3.5.2
     */
    protected void outputFile(File file, Map<String, Object> objectMap, String templatePath, boolean fileOverride) {
        if (isCreate(file, fileOverride)) {
            try {
                // 全局判断【默认】
                boolean exist = file.exists();
                if (!exist) {
                    File parentFile = file.getParentFile();
                    FileUtils.forceMkdir(parentFile);
                }
                if (callback != null) {
                    callback.writeFile(file);
                }
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    templateEngine.merge(objectMap, templatePath, fos);
                    log.debug("模板:" + templatePath + ";  文件:" + file);
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * 输出controller文件
     *
     * @param tableInfo 表信息
     * @param objectMap 渲染数据
     * @since 3.5.0
     */
    public void outputController(TableGeneration tableInfo, Map<String, Object> objectMap) {
        // MpController.java
        String controllerPath = context.getPathInfo(OutputFile.CONTROLLER);
        if (StringUtils.hasText(tableInfo.getControllerName()) && StringUtils.hasText(controllerPath)) {
            getTemplateFilePath(TemplateConfig::getController).ifPresent(controller -> {
                String entityName = tableInfo.getEntityName();
                String controllerFile = String.format((controllerPath + File.separator + tableInfo.getControllerName() + suffixJavaOrKt()), entityName);
                outputFile(new File(controllerFile), objectMap, controller, context.getStrategyConfig()
                    .controller().isFileOverride());
            });
        }
    }

    /**
     * 检查文件是否创建文件
     *
     * @param file         文件
     * @param fileOverride 是否覆盖已有文件
     * @return 是否创建文件
     * @since 3.5.2
     */
    protected boolean isCreate(File file, boolean fileOverride) {
        if (file.exists() && !fileOverride) {
            log.warn("文件[{}]已存在，且未开启文件覆盖配置，需要开启配置可到策略配置中设置！！！", file.getName());
        }
        return !file.exists() || fileOverride;
    }

    /**
     * 渲染对象 MAP 信息
     * TODO 每个模板使用单独的参数集合，全局参数
     *
     * @param context   配置信息
     * @param tableInfo 表信息对象
     * @return ignore
     */
    public Map<String, Object> prepareTemplateArguments(Context context, TableGeneration tableInfo) {
        StrategyConfig strategyConfig = context.getStrategyConfig();
        Map<String, Object> objectMap = new HashMap<>(100);

        // Controller
        Map<String, Object> controllerData = strategyConfig.controller().renderData(tableInfo);
        objectMap.putAll(controllerData);
        // Mapper
        Map<String, Object> mapperData = strategyConfig.mapper().renderData(tableInfo);
        objectMap.putAll(mapperData);
        // Service
        Map<String, Object> serviceData = strategyConfig.service().renderData(tableInfo);
        objectMap.putAll(serviceData);
        // Entity
        Map<String, Object> entityData = strategyConfig.entity().renderData(tableInfo);
        objectMap.putAll(entityData);
        objectMap.put("context", context);
        objectMap.put("package", context.getPackageConfig().getPackageInfo());

        // 全局配置信息
        GlobalConfig globalConfig = context.getGlobalConfig();
        objectMap.put("author", globalConfig.getAuthor());
        objectMap.put("kotlin", globalConfig.isKotlin());
        objectMap.put("swagger", globalConfig.isSwagger());
        objectMap.put("springdoc", globalConfig.isSpringdoc());
        objectMap.put("date", globalConfig.getCommentDate());
        // 启用 schema 处理逻辑
        String schemaName = "";
        if (strategyConfig.isEnableSchema()) {
            // 存在 schemaName 设置拼接 . 组合表名
            schemaName = context.getDataSourceConfig().getSchemaName();
            if (StringUtils.hasText(schemaName)) {
                schemaName += ".";
                tableInfo.setConvert(true);
            }
        }
        objectMap.put("schemaName", schemaName);
        objectMap.put("table", tableInfo);
        objectMap.put("entity", tableInfo.getEntityName());
        return objectMap;
    }

    /**
     * 打开输出目录
     */
    public void open() {
        String outDir = context.getGlobalConfig().getOutputDir();
        if (!StringUtils.hasText(outDir) || !new File(outDir).exists()) {
            System.err.println("未找到输出目录：" + outDir);
        } else if (context.getGlobalConfig().isOpen()) {
            try {
                openDir(outDir);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 打开指定输出文件目录
     *
     * @param outDir 输出文件目录
     * @throws IOException 执行命令出错
     */
    public void openDir(String outDir) throws IOException {
        String osName = System.getProperty("os.name");
        if (osName != null) {
            if (osName.contains("Mac")) {
                Runtime.getRuntime().exec("open " + outDir);
            } else if (osName.contains("Windows")) {
                Runtime.getRuntime().exec(MessageFormat.format("cmd /c start \"\" \"{0}\"", outDir));
            }
        }
    }
}
