package io.devpl.codegen.plugins;

import io.devpl.codegen.ConstVal;
import io.devpl.codegen.config.*;
import io.devpl.codegen.template.model.EntityTemplateArugments;
import io.devpl.codegen.core.*;
import io.devpl.codegen.db.DbColumnType;
import io.devpl.codegen.db.IKeyWordsHandler;
import io.devpl.codegen.db.IdType;
import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import io.devpl.codegen.strategy.FieldFillStrategy;
import io.devpl.codegen.type.JavaType;
import io.devpl.codegen.type.TypeRegistry;
import io.devpl.codegen.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.function.Function;

/**
 * 优先级最高
 * 初始化表生成相关的所有数据
 */
public class TableGenerationPlugin extends PluginAdapter {

    /**
     * 处理表信息(文件名与导包)
     */
    @Override
    public void initialize(GenerationUnit unit) {
        super.initialize(unit);

        if (unit instanceof TableGeneration tg) {
            StrategyConfig strategyConfig = context.getStrategyConfig();
            EntityTemplateArugments entity = strategyConfig.entity();

            String entityName = entity.getNameConvert().entityNameConvert(tg);
            tg.setEntityName(entity.getConverterFileName().apply(entityName));
            tg.setMapperName(strategyConfig.mapper().getConverterMapperFileName().apply(entityName));
            tg.setXmlName(strategyConfig.mapper().getConverterXmlFileName().apply(entityName));
            tg.setServiceName(strategyConfig.service().getConverterServiceFileName().apply(entityName));
            tg.setServiceImplName(strategyConfig.service().getConverterServiceImplFileName().apply(entityName));
            tg.setControllerName(strategyConfig.controller().getConverterFileName().apply(entityName));

            if (strategyConfig.startsWithTablePrefix(tg.getName()) || entity.isTableFieldAnnotationEnable()) {
                tg.setConvert(true);
            } else {
                tg.setConvert(!entityName.equalsIgnoreCase(tg.getName()));
            }

            importPackage(tg);

            // 启用 schema 处理逻辑
            String schemaName = "";
            if (strategyConfig.isEnableSchema()) {
                // 存在 schemaName 设置拼接 . 组合表名
                schemaName = context.getDataSourceConfig().getSchemaName();
                if (StringUtils.hasText(schemaName)) {
                    schemaName += ".";
                    tg.setConvert(true);
                }
            }
            tg.setSchemaName(schemaName);

            initializeColumns(tg);
        }
    }

    /**
     * 导包处理
     *
     * @since 3.5.0
     */
    public void importPackage(TableGeneration tableGeneration) {
        List<String> importPackages = new ArrayList<>();
        StrategyConfig strategyConfig = context.getStrategyConfig();
        EntityTemplateArugments entity = strategyConfig.entity();
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
                // TODO 好像default的不用处理也行,这个做优化项目.
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

    public String getFieldFill(ColumnGeneration column, EntityTemplateArugments entity) {
        if (StringUtils.isBlank(column.getFill())) {
            for (FieldFillStrategy tf : entity.getTableFillList()) {
                // 忽略大写字段问题
                if (tf instanceof ColumnFill && tf.getName()
                    .equalsIgnoreCase(column.getName()) || tf instanceof PropertyFill && tf.getName().equals(column.getPropertyName())) {
                    column.setFill(tf.getFieldFill().name());
                    break;
                }
            }
        }
        return column.getFill();
    }

    private void initializeColumns(TableGeneration tableGeneration) {
        EntityTemplateArugments entity = context.getStrategyConfig().entity();

        DataSourceConfig dataSourceConfig = context.getDataSourceConfig();
        GlobalConfig globalConfig = context.getGlobalConfig();

        Iterator<ColumnGeneration> iterator = tableGeneration.getColumns().iterator();
        while (iterator.hasNext()) {
            ColumnGeneration column = iterator.next();

            String propertyName = column.getPropertyName();
            String columnName = column.getColumnName();
            String name = column.getName();

            // 版本控制字段
            String versionPName = entity.getVersionPropertyName();
            String versionColumnName = entity.getVersionColumnName();
            column.setVersionField(StringUtils.hasText(versionPName)
                && column.getPropertyName().equals(versionPName) || StringUtils.hasText(versionColumnName) && column.getName().equalsIgnoreCase(versionColumnName));

            // 逻辑删除
            String logicDeletePName = entity.getLogicDeletePropertyName();
            String logicDeleteColumnName = entity.getLogicDeleteColumnName();
            column.setLogicDeleteField(StringUtils.hasText(logicDeletePName) && propertyName.equals(logicDeletePName) || StringUtils.hasText(logicDeleteColumnName) && name.equalsIgnoreCase(logicDeleteColumnName));

            // 关键字字段
            IKeyWordsHandler keyWordsHandler = dataSourceConfig.getKeyWordsHandler();
            if (keyWordsHandler != null && keyWordsHandler.isKeyWords(columnName)) {
                column.setKeyWords(true);
                column.setColumnName(keyWordsHandler.formatColumn(columnName));
            }

            // 字段注释信息
            if (globalConfig.isSwagger() && StringUtils.hasText(column.getComment())) {
                column.setComment(column.getComment().replace("\"", "\\\""));
            }

            // 字段处理
            if (entity.matchIgnoreColumns(column.getColumnName())) {
                // 忽略字段不在处理
                iterator.remove();
            } else if (entity.matchSuperEntityColumns(column.getColumnName())) {
                tableGeneration.getCommonColumns().add(column);
            }
        }
        initColumns(tableGeneration);
    }

    void initColumns(TableGeneration introspectedTable) {
        EntityTemplateArugments entity = context.getStrategyConfig().entity();

        GlobalConfig globalConfig = context.getGlobalConfig();
        DateType dateType = globalConfig.getDateType();

        // 初始化字段属性名
        for (ColumnGeneration column : introspectedTable.getColumns()) {
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
    }

    @Override
    public boolean shouldGenerate(GenerationUnit unit) {
        return unit instanceof TableGeneration;
    }

    @Override
    public int getPriority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public List<GeneratedFile> generateFiles(GenerationUnit unit, List<GeneratedFile> files) {
        if (!(unit instanceof TableGeneration tg)) {
            return Collections.emptyList();
        }
        StrategyConfig strategyConfig = context.getStrategyConfig();

        // Controller
        Map<String, Object> objectMap = strategyConfig.controller().renderData(tg);
        TemplateGeneratedFile controllerJavaFile = new TemplateGeneratedFile();
        files.add(controllerJavaFile);

        // Mapper
        TemplateGeneratedFile mapperJavaFile = new TemplateGeneratedFile();
        Map<String, Object> mapperData = strategyConfig.mapper().renderData(tg);
        objectMap.putAll(mapperData);
        files.add(mapperJavaFile);

        // Service
        TemplateGeneratedFile serviceJavaFile = new TemplateGeneratedFile();
        Map<String, Object> serviceData = strategyConfig.service().renderData(tg);
        objectMap.putAll(serviceData);
        files.add(serviceJavaFile);

        // Entity
        TemplateGeneratedFile entityJavaFile = new TemplateGeneratedFile();
        Map<String, Object> entityData = strategyConfig.entity().renderData(tg);
        objectMap.putAll(entityData);
        files.add(entityJavaFile);

        objectMap.put("context", context);
        objectMap.put("package", context.getPackageConfig().getPackageInfo());

        // 全局配置信息
        GlobalConfig globalConfig = context.getGlobalConfig();
        objectMap.put("author", globalConfig.getAuthor());
        objectMap.put("kotlin", globalConfig.isKotlin());
        objectMap.put("swagger", globalConfig.isSwagger());
        objectMap.put("springdoc", globalConfig.isSpringdoc());
        objectMap.put("date", globalConfig.getCommentDate());

        objectMap.put("schemaName", tg.getSchemaName());
        objectMap.put("table", tg);
        objectMap.put("entity", tg.getEntityName());

        return files;
    }

    public GeneratedFile createGeneratedFile(TableGeneration tg) {
        String entityName = tg.getEntityName();
        String entityPath = context.getPathInfo(OutputFile.ENTITY);
        if (StringUtils.hasText(entityName) && StringUtils.hasText(entityPath)) {
            getTemplateFilePath(template -> template.getEntityTemplatePath(context.useKotlin())).ifPresent((entity) -> {
                // 保存位置
                String entityFile = String.format((entityPath + File.separator + "%s" + suffixJavaOrKt()), entityName);

            });
        }

        return new TemplateGeneratedFile();
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
            return Optional.of(getPathOfTemplateFile(filePath, ".vm"));
        }
        return Optional.empty();
    }

    public String getPathOfTemplateFile(String filePath, String templateFileExtension) {
        return filePath.endsWith(templateFileExtension) ? filePath : filePath + templateFileExtension;
    }

    /**
     * 文件后缀
     */
    protected String suffixJavaOrKt() {
        return context.useKotlin() ? ConstVal.KT_SUFFIX : ConstVal.JAVA_SUFFIX;
    }
}
