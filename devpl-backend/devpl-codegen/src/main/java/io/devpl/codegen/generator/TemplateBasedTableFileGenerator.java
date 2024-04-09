package io.devpl.codegen.generator;

import io.devpl.codegen.ConstVal;
import io.devpl.codegen.generator.config.BuiltinTargetFile;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.generator.config.PackageConfiguration;
import io.devpl.codegen.generator.config.StrategyConfiguration;
import io.devpl.codegen.template.TemplateArgumentsMap;
import io.devpl.codegen.template.TemplateEngine;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Setter
@Getter
class TemplateBasedTableFileGenerator extends AbstractTableFileGenerator {

    private List<TemplateBasedTargetFile> targetFiles;
    private TemplateEngine templateEngine;

    public TemplateBasedTableFileGenerator() {
        super();
        this.targetFiles = new ArrayList<>();
    }

    public <T extends TemplateBasedTargetFile> void addTargetFile(T targetFile) {
        this.targetFiles.add(targetFile);
    }

    public <T extends TemplateBasedTargetFile> void addTargetFiles(Collection<T> targetFiles) {
        this.targetFiles.addAll(targetFiles);
    }

    @Override
    public void initialize(GenerationTarget target) {
        if (target instanceof TableGeneration) {
            this.tableGeneration = (TableGeneration) target;
        }
        for (TemplateBasedTargetFile targetFile : this.targetFiles) {
            targetFile.initialize(target);
        }
    }

    @Override
    public List<GeneratedFile> getGeneratedFiles() {
        List<GeneratedFile> generatedFiles = new ArrayList<>();
        for (TemplateBasedTargetFile targetFile : targetFiles) {
            TemplateGeneratedFile file = new TemplateGeneratedFile();
            file.setTargetFile(targetFile);
            file.setTemplate(targetFile.getTemplate());
            file.setTemplateEngine(this.templateEngine);
            generatedFiles.add(file);
        }

        StrategyConfiguration strategyConfiguration = context.getObject(StrategyConfiguration.class);
        PackageConfiguration packageConfig = context.getObject(PackageConfiguration.class);

        final String parentPackageName = packageConfig.getParent();

        // 全局配置信息
        GlobalConfiguration globalConfiguration = context.getObject(GlobalConfiguration.class);

        final List<GeneratedFile> files = new ArrayList<>();

        Map<String, Object> objectMap = strategyConfiguration.controller().renderData(tableGeneration);
        strategyConfiguration.entity().renderData(tableGeneration);
        // 单表的所有模板参数
        final TemplateArgumentsMap argumentsOfSingleTable = new TemplateArgumentsMap(objectMap);
        // Controller
        TemplateGeneratedFile controllerJavaFile = new TemplateGeneratedFile(BuiltinTargetFile.CONTROLLER);
        controllerJavaFile.setTemplateArguments(argumentsOfSingleTable);
        controllerJavaFile.setFilename(tableGeneration.getControllerName());
        controllerJavaFile.setExtension(ConstVal.JAVA_SUFFIX);
        controllerJavaFile.setTargetPackageName(parentPackageName + "." + packageConfig.getController());
        // Mapper.java
        TemplateGeneratedFile mapperJavaFile = new TemplateGeneratedFile(BuiltinTargetFile.MAPPER);
        mapperJavaFile.setTemplateArguments(argumentsOfSingleTable);
        mapperJavaFile.setFilename(tableGeneration.getMapperName());
        mapperJavaFile.setExtension(ConstVal.JAVA_SUFFIX);
        mapperJavaFile.setTargetPackageName(parentPackageName + "." + packageConfig.getMapper());
        // Mapper.xml
        TemplateGeneratedFile mapperXmlFile = new TemplateGeneratedFile(BuiltinTargetFile.MAPPER_XML);
        mapperXmlFile.setTemplateArguments(argumentsOfSingleTable);
        mapperXmlFile.setFilename(tableGeneration.getMapperName());
        mapperXmlFile.setExtension(ConstVal.XML);
        mapperXmlFile.setTargetPackageName(packageConfig.getXml());
        // Service
        TemplateGeneratedFile serviceJavaFile = new TemplateGeneratedFile(BuiltinTargetFile.SERVICE);
        serviceJavaFile.setFilename(tableGeneration.getServiceName());
        serviceJavaFile.setExtension(ConstVal.JAVA_SUFFIX);
        serviceJavaFile.setTemplateArguments(argumentsOfSingleTable);
        serviceJavaFile.setTargetPackageName(parentPackageName + "." + packageConfig.getService());
        // ServiceImpl
        TemplateGeneratedFile serviceImplJavaFile = new TemplateGeneratedFile(BuiltinTargetFile.SERVICE_IMPL);
        serviceImplJavaFile.setFilename(tableGeneration.getServiceName());
        serviceImplJavaFile.setExtension(ConstVal.JAVA_SUFFIX);
        serviceImplJavaFile.setTemplateArguments(argumentsOfSingleTable);
        serviceImplJavaFile.setTargetPackageName(parentPackageName + "." + packageConfig.getServiceImpl());
        // Entity
        TemplateGeneratedFile entityJavaFile = new TemplateGeneratedFile(BuiltinTargetFile.ENTITY_JAVA);
        entityJavaFile.setFilename(tableGeneration.getEntityName());
        entityJavaFile.setExtension(ConstVal.JAVA_SUFFIX);
        entityJavaFile.setTemplateArguments(argumentsOfSingleTable);
        entityJavaFile.setTargetPackageName(parentPackageName + "." + packageConfig.getEntity());

        Map<String, Object> mapperData = strategyConfiguration.mapper().renderData(tableGeneration);
        objectMap.putAll(mapperData);

        Map<String, Object> serviceData = strategyConfiguration.service().renderData(tableGeneration);
        objectMap.putAll(serviceData);

        Map<String, Object> entityData = strategyConfiguration.entity().renderData(tableGeneration);
        objectMap.putAll(entityData);

        objectMap.put("context", context);
        objectMap.put("package", context.getObject(PackageConfiguration.class).getPackageInfo());

        objectMap.put("author", globalConfiguration.getAuthor());
        objectMap.put("kotlin", globalConfiguration.isKotlin());
        objectMap.put("swagger", globalConfiguration.isSwagger());
        objectMap.put("springdoc", globalConfiguration.isSpringdoc());
        objectMap.put("date", globalConfiguration.getCommentDate());

        objectMap.put("schemaName", tableGeneration.getSchemaName());
        objectMap.put("table", tableGeneration);
        objectMap.put("entity", tableGeneration.getEntityName());

        files.add(controllerJavaFile);
        files.add(serviceJavaFile);
        files.add(serviceImplJavaFile);
        files.add(entityJavaFile);
        files.add(mapperJavaFile);
        files.add(mapperXmlFile);

        TemplateEngine templateEngine = context.getObject(TemplateEngine.class);
        for (GeneratedFile file : files) {
            if (file instanceof TemplateGeneratedFile tgf) {
                tgf.setTemplateEngine(templateEngine);
                tgf.setTargetProject(globalConfiguration.getOutputDir());
            }
        }
        return generatedFiles;
    }
}
