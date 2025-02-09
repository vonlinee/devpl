package io.devpl.codegen.generator.plugins;

import io.devpl.codegen.db.IdType;
import io.devpl.codegen.generator.ColumnGeneration;
import io.devpl.codegen.generator.GenerationTarget;
import io.devpl.codegen.generator.TableGeneration;
import io.devpl.codegen.generator.config.*;
import io.devpl.codegen.strategy.FieldFillStrategy;
import io.devpl.codegen.template.model.EntityTemplateArguments;
import io.devpl.codegen.type.JavaType;
import io.devpl.sdk.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
}
