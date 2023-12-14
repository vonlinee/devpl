package com.baomidou.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.codegen.ActionCallback;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.Context;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.po.IntrospectedColumn;
import com.baomidou.mybatisplus.generator.config.po.IntrospectedTable;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.velocity.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.jdbc.meta.ColumnMetadata;
import com.baomidou.mybatisplus.generator.query.DatabaseIntrospector;
import com.baomidou.mybatisplus.generator.type.JavaType;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import com.baomidou.mybatisplus.generator.util.FileUtils;
import com.baomidou.mybatisplus.generator.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 生成文件
 */
public class AutoGenerator {

    private static final Logger logger = LoggerFactory.getLogger(AutoGenerator.class);
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
        logger.debug("==========================准备生成文件...==========================");
        // 初始化配置
        if (null == context) {
            this.strategyConfig = Optional.ofNullable(strategyConfig).orElse(new StrategyConfig.Builder().build());
            this.globalConfig = Optional.ofNullable(globalConfig).orElse(new GlobalConfig.Builder().build());
            this.templateConfig = Optional.ofNullable(templateConfig).orElse(new TemplateConfig.Builder().build());
            this.packageConfig = Optional.ofNullable(packageConfig).orElse(new PackageConfig.Builder().build());
            this.injectionConfig = Optional.ofNullable(injectionConfig).orElse(new InjectionConfig.Builder().build());
            context = new Context(packageConfig, dataSourceConfig, strategyConfig, templateConfig, globalConfig, injectionConfig);

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
        if (null == templateEngine) {
            // 为了兼容之前逻辑，采用 Velocity 引擎 【 默认 】
            templateEngine = new VelocityTemplateEngine();
        }
        this.templateEngine = templateEngine;
        this.callback = globalConfig.callback;


        // 获取所有的表信息
        List<IntrospectedTable> tableInfoList = context.introspectTables();
        // 模板引擎初始化执行文件输出
        templateEngine.init(context);

        DateType dateType = globalConfig.getDateType();

        // 初始化字段属性名
        final Entity entity = strategyConfig.entity();

        for (IntrospectedTable introspectedTable : tableInfoList) {

            introspectedTable.initialize();

            for (IntrospectedColumn column : introspectedTable.getColumns()) {
                ColumnMetadata cmd = column.getColumnMetadata();
                // 设置字段的元数据信息

                JavaType columnType;
                if (cmd.getDataType() == null || cmd.getColumnSize() == null || cmd.getDecimalDigits() == null) {
                    columnType = DbColumnType.STRING;
                } else {
                    columnType = TypeRegistry.getColumnType(cmd.getDataType(), cmd.getColumnSize(), dateType, cmd.getDecimalDigits());
                }
                column.setColumnType(columnType);
                // 数据库字段名 -> Java属性字段名
                String propertyName = entity.getNameConvert().propertyNameConvert(column.getName());

                // 字段类型

                // boolean类型字段
                if (entity.isBooleanColumnRemoveIsPrefix() && "boolean".equalsIgnoreCase(column.getPropertyType()) && propertyName.startsWith("is")) {
                    column.setConvert(true);
                    // 前两个字符小写，后面的不变
                    String rawString = propertyName.substring(2);
                    column.setPropertyName(rawString.substring(0, 2).toLowerCase() + rawString.substring(2));
                }
                // 下划线转驼峰策略
                if (NamingStrategy.UNDERLINE_TO_CAMEL.equals(entity.getColumnNamingStrategy())) {
                    column.setConvert(!propertyName.equalsIgnoreCase(NamingStrategy.underlineToCamel(column.getColumnName())));
                }
                // 原样输出策略
                if (NamingStrategy.NO_CHANGE.equals(entity.getColumnNamingStrategy())) {
                    column.setConvert(!propertyName.equalsIgnoreCase(column.getColumnName()));
                }
                if (entity.isTableFieldAnnotationEnable()) {
                    column.setConvert(true);
                }
                column.setPropertyName(propertyName);
            }

            // 数据初始化完毕
            try {
                // 填充模板参数
                Map<String, Object> objectMap = this.getObjectMap(context, introspectedTable);
                InjectionConfig injectionConfig = context.getInjectionConfig();
                if (injectionConfig != null) {
                    // 添加自定义属性
                    injectionConfig.beforeOutputFile(introspectedTable, objectMap);
                    // 输出自定义文件
                    outputCustomFile(injectionConfig.getCustomFiles(), introspectedTable, objectMap);
                }
                // entity
                outputEntity(introspectedTable, objectMap);
                // mapper and xml
                outputMapper(introspectedTable, objectMap);
                // service
                outputService(introspectedTable, objectMap);
                // controller
                outputController(introspectedTable, objectMap);
            } catch (Exception e) {
                throw new RuntimeException("无法创建文件，请检查配置信息！", e);
            }
        }
        open();
    }

    /**
     * 输出自定义模板文件
     *
     * @param customFiles 自定义模板文件列表
     * @param tableInfo   表信息
     * @param objectMap   渲染数据
     * @since 3.5.3
     */
    public void outputCustomFile(List<CustomFile> customFiles, IntrospectedTable tableInfo, Map<String, Object> objectMap) {
        String entityName = tableInfo.getEntityName();
        String parentPath = context.getPathInfo().get(OutputFile.parent);
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
     * @since 3.5.0
     */
    public void outputEntity(IntrospectedTable tableInfo, Map<String, Object> objectMap) {
        String entityName = tableInfo.getEntityName();
        String entityPath = context.getPathInfo().get(OutputFile.entity);
        if (StringUtils.hasText(entityName) && StringUtils.hasText(entityPath)) {
            getTemplateFilePath(template -> template.getEntity(context.getGlobalConfig()
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
    public void outputMapper(IntrospectedTable tableInfo, Map<String, Object> objectMap) {
        // MpMapper.java
        String entityName = tableInfo.getEntityName();
        String mapperPath = context.getPathInfo().get(OutputFile.mapper);
        if (StringUtils.hasText(tableInfo.getMapperName()) && StringUtils.hasText(mapperPath)) {
            getTemplateFilePath(TemplateConfig::getMapper).ifPresent(mapper -> {
                String mapperFile = String.format((mapperPath + File.separator + tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
                outputFile(new File(mapperFile), objectMap, mapper, context.getStrategyConfig().mapper()
                    .isFileOverride());
            });
        }
        // MpMapper.xml
        String xmlPath = context.getPathInfo().get(OutputFile.xml);
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
    public void outputService(IntrospectedTable tableInfo, Map<String, Object> objectMap) {
        // IMpService.java
        String entityName = tableInfo.getEntityName();
        String servicePath = context.getPathInfo().get(OutputFile.service);
        if (StringUtils.hasText(tableInfo.getServiceName()) && StringUtils.hasText(servicePath)) {
            getTemplateFilePath(TemplateConfig::getService).ifPresent(service -> {
                String serviceFile = String.format((servicePath + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
                outputFile(new File(serviceFile), objectMap, service, context.getStrategyConfig().service()
                    .isFileOverride());
            });
        }
        // MpServiceImpl.java
        String serviceImplPath = context.getPathInfo().get(OutputFile.serviceImpl);
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
        TemplateConfig templateConfig = context.getTemplateConfig();
        String filePath = function.apply(templateConfig);
        if (StringUtils.hasText(filePath)) {
            return Optional.of(templateEngine.templateFilePath(filePath));
        }
        return Optional.empty();
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
                    logger.debug("模板:" + templatePath + ";  文件:" + file);
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
    public void outputController(IntrospectedTable tableInfo, Map<String, Object> objectMap) {
        // MpController.java
        String controllerPath = context.getPathInfo().get(OutputFile.controller);
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
            logger.warn("文件[{}]已存在，且未开启文件覆盖配置，需要开启配置可到策略配置中设置！！！", file.getName());
        }
        return !file.exists() || fileOverride;
    }

    /**
     * 渲染对象 MAP 信息
     *
     * @param context   配置信息
     * @param tableInfo 表信息对象
     * @return ignore
     */
    public Map<String, Object> getObjectMap(Context context, IntrospectedTable tableInfo) {
        StrategyConfig strategyConfig = context.getStrategyConfig();
        // Controller
        Map<String, Object> controllerData = strategyConfig.controller().renderData(tableInfo);
        Map<String, Object> objectMap = new HashMap<>(controllerData);
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
                logger.error(e.getMessage(), e);
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
