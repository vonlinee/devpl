package io.devpl.codegen.generator.plugins;

import io.devpl.codegen.generator.ColumnGeneration;
import io.devpl.codegen.generator.TableGeneration;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.util.StringUtils;

public class ColumnCommentPlugin extends TableGenerationPlugin {
    @Override
    void initialize(TableGeneration tableGeneration) {
        GlobalConfiguration globalConfiguration = context.getObject(GlobalConfiguration.class);
        for (ColumnGeneration column : tableGeneration.getColumns()) {
            // 字段注释信息
            if (globalConfiguration.isSwagger() && StringUtils.hasText(column.getComment())) {
                column.setComment(column.getComment().replace("\"", "\\\""));
            }
        }
    }
}
