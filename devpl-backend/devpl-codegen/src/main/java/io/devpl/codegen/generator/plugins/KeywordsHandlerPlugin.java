package io.devpl.codegen.generator.plugins;

import io.devpl.codegen.db.IKeyWordsHandler;
import io.devpl.codegen.generator.ColumnGeneration;
import io.devpl.codegen.generator.TableGeneration;
import io.devpl.codegen.generator.config.JdbcConfiguration;

/**
 * sql列名称关键字处理
 */
public class KeywordsHandlerPlugin extends TableGenerationPlugin {

    @Override
    public void initialize(TableGeneration tableGeneration) {
        JdbcConfiguration dataSourceConfig = context.getObject(JdbcConfiguration.class);
        IKeyWordsHandler keyWordsHandler = dataSourceConfig.getKeyWordsHandler();
        if (keyWordsHandler == null) {
            return;
        }
        for (ColumnGeneration column : tableGeneration.getColumns()) {
            String columnName = column.getColumnName();
            // 关键字字段
            if (keyWordsHandler.isKeyWords(columnName)) {
                column.setKeyWords(true);
                column.setColumnName(keyWordsHandler.formatColumn(columnName));
            }
        }
    }
}
