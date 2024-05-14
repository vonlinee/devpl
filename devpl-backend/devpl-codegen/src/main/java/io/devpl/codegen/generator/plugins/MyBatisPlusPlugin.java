package io.devpl.codegen.generator.plugins;

import io.devpl.codegen.db.DbFieldDataType;
import io.devpl.codegen.generator.ColumnGeneration;
import io.devpl.codegen.generator.TableGeneration;
import io.devpl.codegen.generator.config.*;
import io.devpl.codegen.template.model.EntityTemplateArguments;
import io.devpl.codegen.type.JavaType;
import io.devpl.codegen.type.TypeRegistry;
import io.devpl.codegen.util.StringUtils;
import io.devpl.codegen.util.Utils;
import org.apache.ddlutils.jdbc.meta.ColumnMetadata;

public class MyBatisPlusPlugin extends TableGenerationPlugin {

    @Override
    public void initialize(TableGeneration table) {
        EntityTemplateArguments entity = context.getObject(StrategyConfiguration.class).entity();
        GlobalConfiguration globalConfiguration = context.getObject(GlobalConfiguration.class);

        StrategyConfiguration strategyConfiguration = context.getObject(StrategyConfiguration.class);

        NameConverter nameConverter = Utils.ifNull(entity.getNameConvert(), new DefaultNameConvert(strategyConfiguration));

        DateType dateType = globalConfiguration.getDateType();
        // 初始化字段属性名
        for (ColumnGeneration column : table.getColumns()) {
            ColumnMetadata cmd = column.getColumnMetadata();
            // 设置字段的元数据信息
            JavaType columnType;
            if (cmd.getDataType() == null || cmd.getColumnSize() == null || cmd.getDecimalDigits() == null) {
                columnType = DbFieldDataType.STRING;
            } else {
                columnType = TypeRegistry.getColumnType(cmd.getDataType(), cmd.getColumnSize(), dateType, cmd.getDecimalDigits());
            }
            column.setColumnType(columnType);
            // 数据库字段名 -> Java属性字段名
            String propertyName = nameConverter.propertyNameConvert(column.getName());

            // 版本控制字段
            String versionPName = entity.getVersionPropertyName();
            String versionColumnName = entity.getVersionColumnName();
            column.setVersionField(StringUtils.hasText(versionPName)
                                   && column.getPropertyName().equals(versionPName) || StringUtils.hasText(versionColumnName) && column.getName().equalsIgnoreCase(versionColumnName));

            // 逻辑删除
            String logicDeletePName = entity.getLogicDeletePropertyName();
            String logicDeleteColumnName = entity.getLogicDeleteColumnName();
            column.setLogicDeleteField(StringUtils.hasText(logicDeletePName)
                                       && propertyName.equals(logicDeletePName)
                                       || StringUtils.hasText(logicDeleteColumnName));
        }
    }
}
