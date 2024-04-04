package io.devpl.codegen.generator.plugins;

import io.devpl.codegen.generator.ColumnGeneration;
import io.devpl.codegen.generator.TableGeneration;
import io.devpl.codegen.generator.config.NamingStrategy;
import io.devpl.codegen.generator.config.StrategyConfiguration;
import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import io.devpl.codegen.template.model.EntityTemplateArguments;

/**
 * 命名处理插件
 */
public class NamingPlugin extends TableGenerationPlugin {

    @Override
    public void initialize(TableGeneration table) {
        EntityTemplateArguments entity = context.getObject(StrategyConfiguration.class).entity();
        // 初始化字段属性名
        for (ColumnGeneration column : table.getColumns()) {
            ColumnMetadata cmd = column.getColumnMetadata();
            // 设置字段的元数据信息

            // 数据库字段名 -> Java属性字段名
            String propertyName = entity.getNameConvert().propertyNameConvert(column.getName());
            // 字段类型
            // boolean类型字段
            if (entity.isBooleanColumnRemoveIsPrefix() && column.isBooleanType() && propertyName.startsWith("is")) {
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

}
