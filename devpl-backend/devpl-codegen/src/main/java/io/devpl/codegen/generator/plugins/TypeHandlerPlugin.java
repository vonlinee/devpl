package io.devpl.codegen.generator.plugins;

import io.devpl.codegen.db.DbFieldDataType;
import io.devpl.codegen.generator.ColumnGeneration;
import io.devpl.codegen.generator.TableGeneration;
import io.devpl.codegen.generator.config.DateType;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.type.JavaType;
import io.devpl.codegen.type.TypeRegistry;
import org.apache.ddlutils.jdbc.meta.ColumnMetadata;

/**
 * 处理列的数据类型
 */
public class TypeHandlerPlugin extends TableGenerationPlugin {

    @Override
    public void initialize(TableGeneration tableGeneration) {
        GlobalConfiguration globalConfiguration = context.getObject(GlobalConfiguration.class);
        DateType dateType = globalConfiguration.getDateType();
        for (ColumnGeneration column : tableGeneration.getColumns()) {

            ColumnMetadata cmd = column.getColumnMetadata();
            JavaType columnType;
            if (cmd.getDataType() == null || cmd.getColumnSize() == null || cmd.getDecimalDigits() == null) {
                columnType = DbFieldDataType.STRING;
            } else {
                columnType = TypeRegistry.getColumnType(cmd.getDataType(), cmd.getColumnSize(), dateType, cmd.getDecimalDigits());
            }
            column.setColumnType(columnType);
        }
    }
}
