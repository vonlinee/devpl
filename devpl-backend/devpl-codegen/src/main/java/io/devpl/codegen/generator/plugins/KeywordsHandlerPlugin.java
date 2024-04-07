package io.devpl.codegen.generator.plugins;

import io.devpl.codegen.db.ColumnKeyWordsHandler;
import io.devpl.codegen.db.DBType;
import io.devpl.codegen.db.keywords.MySqlKeyWordsHandler;
import io.devpl.codegen.db.keywords.PostgreSqlKeyWordsHandler;
import io.devpl.codegen.generator.ColumnGeneration;
import io.devpl.codegen.generator.TableGeneration;
import io.devpl.codegen.generator.config.JdbcConfiguration;

/**
 * sql列名称关键字处理
 */
public class KeywordsHandlerPlugin extends TableGenerationPlugin {

    @Override
    public void initialize(TableGeneration tableGeneration) {
        JdbcConfiguration jdbcConfiguration = context.getObject(JdbcConfiguration.class);
        ColumnKeyWordsHandler keyWordsHandler = jdbcConfiguration.getKeyWordsHandler();
        if (keyWordsHandler == null) {
            DBType dbType = jdbcConfiguration.getDbType();
            if (dbType == DBType.MYSQL) {
                keyWordsHandler = new MySqlKeyWordsHandler();
            } else if (dbType == DBType.POSTGRE_SQL) {
                keyWordsHandler = new PostgreSqlKeyWordsHandler();
            } else {
                return;
            }
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
