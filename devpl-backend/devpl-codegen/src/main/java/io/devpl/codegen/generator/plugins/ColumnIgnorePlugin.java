package io.devpl.codegen.generator.plugins;

import io.devpl.codegen.generator.ColumnGeneration;
import io.devpl.codegen.generator.TableGeneration;
import io.devpl.codegen.generator.config.StrategyConfiguration;
import io.devpl.codegen.template.model.EntityTemplateArguments;

import java.util.Iterator;

public class ColumnIgnorePlugin extends TableGenerationPlugin {

    @Override
    public void initialize(TableGeneration tableGeneration) {
        EntityTemplateArguments entity = context.getObject(StrategyConfiguration.class).entity();
        Iterator<ColumnGeneration> iterator = tableGeneration.getColumns().iterator();
        while (iterator.hasNext()) {
            ColumnGeneration column = iterator.next();
            // 字段处理
            if (entity.matchIgnoreColumns(column.getColumnName())) {
                // 忽略字段不在处理
                iterator.remove();
            } else if (entity.matchSuperEntityColumns(column.getColumnName())) {
                tableGeneration.getCommonColumns().add(column);
            }
        }
    }
}
