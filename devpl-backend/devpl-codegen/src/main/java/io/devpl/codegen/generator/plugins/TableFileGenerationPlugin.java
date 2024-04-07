package io.devpl.codegen.generator.plugins;

import io.devpl.codegen.ConstVal;
import io.devpl.codegen.db.IdType;
import io.devpl.codegen.generator.*;
import io.devpl.codegen.generator.config.*;
import io.devpl.codegen.strategy.FieldFillStrategy;
import io.devpl.codegen.template.TemplateArgumentsMap;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.model.EntityTemplateArguments;
import io.devpl.codegen.type.JavaType;
import io.devpl.sdk.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 优先级最高
 * 初始化表生成相关的所有数据
 */
public class TableFileGenerationPlugin extends TableGenerationPlugin {

    /**
     * 处理表信息(文件名与导包)
     */
    @Override
    public void initialize(TableGeneration tg) {
        StrategyConfiguration strategyConfiguration = context.getObject(StrategyConfiguration.class);
        EntityTemplateArguments entity = strategyConfiguration.entity();

        NameConverter converter = entity.getNameConvert();
        if (converter == null) {
            converter = new DefaultNameConvert(strategyConfiguration);
        }
        String entityName = converter.entityNameConvert(tg);
        tg.setEntityName(entity.getConverterFileName().apply(entityName));
        tg.setMapperName(strategyConfiguration.mapper().getConverterMapperFileName().apply(entityName));
        tg.setXmlName(strategyConfiguration.mapper().getConverterXmlFileName().apply(entityName));
        tg.setServiceName(strategyConfiguration.service().getConverterServiceFileName().apply(entityName));
        tg.setServiceImplName(strategyConfiguration.service().getConverterServiceImplFileName().apply(entityName));
        tg.setControllerName(strategyConfiguration.controller().getConverterFileName().apply(entityName));

        if (strategyConfiguration.startsWithTablePrefix(tg.getName()) || entity.isTableFieldAnnotationEnable()) {
            tg.setConvert(true);
        } else {
            tg.setConvert(!entityName.equalsIgnoreCase(tg.getName()));
        }

        importPackage(tg);

        // 启用 schema 处理逻辑
        String schemaName = "";
        if (strategyConfiguration.isEnableSchema()) {
            // 存在 schemaName 设置拼接 . 组合表名
            schemaName = context.getObject(JdbcConfiguration.class).getSchemaName();
            if (StringUtils.hasText(schemaName)) {
                schemaName += ".";
                tg.setConvert(true);
            }
        }
        tg.setSchemaName(schemaName);
    }

    /**
     * 导包处理
     */
    public void importPackage(TableGeneration tableGeneration) {
        List<String> importPackages = new ArrayList<>();
        StrategyConfiguration strategyConfiguration = context.getObject(StrategyConfiguration.class);
        EntityTemplateArguments entity = strategyConfiguration.entity();
        String superEntity = entity.getSuperClass();
        if (StringUtils.hasText(superEntity)) {
            // 自定义父类
            importPackages.add(superEntity);
        } else {
            if (entity.isActiveRecord()) {
                // 无父类开启 AR 模式
                importPackages.add("com.baomidou.mybatisplus.extension.activerecord.Model");
            }
        }
        if (entity.isSerialVersionUID() || entity.isActiveRecord()) {
            importPackages.add("java.io.Serializable");
        }
        if (tableGeneration.isConvert()) {
            importPackages.add("com.baomidou.mybatisplus.annotation.TableName");
        }
        IdType idType = entity.getIdType();
        if (null != idType && tableGeneration.hasPrimaryKey()) {
            // 指定需要 IdType 场景
            importPackages.add("com.baomidou.mybatisplus.annotation.IdType");
            importPackages.add("com.baomidou.mybatisplus.annotation.TableId");
        }

        for (ColumnGeneration field : tableGeneration.getColumns()) {
            JavaType columnType = field.getColumnType();
            if (null != columnType && null != columnType.getQualifier()) {
                importPackages.add(columnType.getQualifier());
            }
            if (field.isKeyFlag()) {
                // 主键
                if (field.isConvert() || field.isKeyIdentityFlag()) {
                    importPackages.add("com.baomidou.mybatisplus.annotation.TableId");
                }
                // 自增
                if (field.isKeyIdentityFlag()) {
                    importPackages.add("com.baomidou.mybatisplus.annotation.IdType");
                }
            } else if (field.isConvert()) {
                // 普通字段
                importPackages.add("com.baomidou.mybatisplus.annotation.TableField");
            }
            if (null != getFieldFill(field, entity)) {
                // 填充字段
                importPackages.add("com.baomidou.mybatisplus.annotation.TableField");
                importPackages.add("com.baomidou.mybatisplus.annotation.FieldFill");
            }
            if (field.isVersionField()) {
                importPackages.add("com.baomidou.mybatisplus.annotation.Version");
            }
            if (field.isLogicDeleteField()) {
                importPackages.add("com.baomidou.mybatisplus.annotation.TableLogic");
            }
        }

        tableGeneration.getImportPackages().addAll(importPackages);
    }

    public String getFieldFill(ColumnGeneration column, EntityTemplateArguments entity) {
        if (StringUtils.isBlank(column.getFill())) {
            for (FieldFillStrategy tf : entity.getTableFillList()) {
                // 忽略大写字段问题
                if (tf instanceof ColumnFill && tf.getName().equalsIgnoreCase(column.getName())
                    || tf instanceof PropertyFill
                       && tf.getName().equals(column.getPropertyName())) {
                    column.setFill(tf.getFieldFill().name());
                    break;
                }
            }
        }
        return column.getFill();
    }

    @Override
    public boolean shouldGenerate(GenerationTarget unit) {
        return unit instanceof TableGeneration;
    }

    @Override
    public List<GeneratedFile> generateFiles(GenerationTarget unit) {
        if (!support(unit)) {
            return Collections.emptyList();
        }
        final TableGeneration tg = (TableGeneration) unit;
        StrategyConfiguration strategyConfiguration = context.getObject(StrategyConfiguration.class);
        PackageConfiguration packageConfig = context.getObject(PackageConfiguration.class);

        final String parentPackageName = packageConfig.getParent();

        // 全局配置信息
        GlobalConfiguration globalConfiguration = context.getObject(GlobalConfiguration.class);

        final List<GeneratedFile> files = new ArrayList<>();

        Map<String, Object> objectMap = strategyConfiguration.controller().renderData(tg);
        strategyConfiguration.entity().renderData(tg);
        // 单表的所有模板参数
        final TemplateArgumentsMap argumentsOfSingleTable = new TemplateArgumentsMap(objectMap);
        // Controller
        TemplateGeneratedFile controllerJavaFile = new TemplateGeneratedFile(BuiltinTargetFile.CONTROLLER);
        controllerJavaFile.setTemplateArguments(argumentsOfSingleTable);
        controllerJavaFile.setFilename(tg.getControllerName());
        controllerJavaFile.setExtension(ConstVal.JAVA_SUFFIX);
        controllerJavaFile.setTargetPackageName(parentPackageName + "." + packageConfig.getController());
        // Mapper.java
        TemplateGeneratedFile mapperJavaFile = new TemplateGeneratedFile(BuiltinTargetFile.MAPPER);
        mapperJavaFile.setTemplateArguments(argumentsOfSingleTable);
        mapperJavaFile.setFilename(tg.getMapperName());
        mapperJavaFile.setExtension(ConstVal.JAVA_SUFFIX);
        mapperJavaFile.setTargetPackageName(parentPackageName + "." + packageConfig.getMapper());
        // Mapper.xml
        TemplateGeneratedFile mapperXmlFile = new TemplateGeneratedFile(BuiltinTargetFile.MAPPER_XML);
        mapperXmlFile.setTemplateArguments(argumentsOfSingleTable);
        mapperXmlFile.setFilename(tg.getMapperName());
        mapperXmlFile.setExtension(ConstVal.XML);
        mapperXmlFile.setTargetPackageName(packageConfig.getXml());
        // Service
        TemplateGeneratedFile serviceJavaFile = new TemplateGeneratedFile(BuiltinTargetFile.SERVICE);
        serviceJavaFile.setFilename(tg.getServiceName());
        serviceJavaFile.setExtension(ConstVal.JAVA_SUFFIX);
        serviceJavaFile.setTemplateArguments(argumentsOfSingleTable);
        serviceJavaFile.setTargetPackageName(parentPackageName + "." + packageConfig.getService());
        // ServiceImpl
        TemplateGeneratedFile serviceImplJavaFile = new TemplateGeneratedFile(BuiltinTargetFile.SERVICE_IMPL);
        serviceImplJavaFile.setFilename(tg.getServiceName());
        serviceImplJavaFile.setExtension(ConstVal.JAVA_SUFFIX);
        serviceImplJavaFile.setTemplateArguments(argumentsOfSingleTable);
        serviceImplJavaFile.setTargetPackageName(parentPackageName + "." + packageConfig.getServiceImpl());
        // Entity
        TemplateGeneratedFile entityJavaFile = new TemplateGeneratedFile(BuiltinTargetFile.ENTITY_JAVA);
        entityJavaFile.setFilename(tg.getEntityName());
        entityJavaFile.setExtension(ConstVal.JAVA_SUFFIX);
        entityJavaFile.setTemplateArguments(argumentsOfSingleTable);
        entityJavaFile.setTargetPackageName(parentPackageName + "." + packageConfig.getEntity());

        Map<String, Object> mapperData = strategyConfiguration.mapper().renderData(tg);
        objectMap.putAll(mapperData);

        Map<String, Object> serviceData = strategyConfiguration.service().renderData(tg);
        objectMap.putAll(serviceData);

        Map<String, Object> entityData = strategyConfiguration.entity().renderData(tg);
        objectMap.putAll(entityData);

        objectMap.put("context", context);
        objectMap.put("package", context.getObject(PackageConfiguration.class).getPackageInfo());

        objectMap.put("author", globalConfiguration.getAuthor());
        objectMap.put("kotlin", globalConfiguration.isKotlin());
        objectMap.put("swagger", globalConfiguration.isSwagger());
        objectMap.put("springdoc", globalConfiguration.isSpringdoc());
        objectMap.put("date", globalConfiguration.getCommentDate());

        objectMap.put("schemaName", tg.getSchemaName());
        objectMap.put("table", tg);
        objectMap.put("entity", tg.getEntityName());

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
        return files;
    }
}
