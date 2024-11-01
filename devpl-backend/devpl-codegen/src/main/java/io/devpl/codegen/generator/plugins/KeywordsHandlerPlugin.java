package io.devpl.codegen.generator.plugins;

import io.devpl.codegen.db.ColumnKeyWordsHandler;
import io.devpl.codegen.db.keywords.MySqlKeyWordsHandler;
import io.devpl.codegen.db.keywords.PostgreSqlKeyWordsHandler;
import io.devpl.codegen.generator.ColumnGeneration;
import io.devpl.codegen.generator.TableGeneration;
import io.devpl.codegen.generator.config.JdbcConfiguration;
import org.apache.ddlutils.platform.BuiltinDatabaseType;
import org.apache.ddlutils.platform.DatabaseType;

/**
 * sql列名称关键字处理
 */
public class KeywordsHandlerPlugin extends TableGenerationPlugin {

    @Override
    public void initialize(TableGeneration tableGeneration) {
        JdbcConfiguration jdbcConfiguration = context.getObject(JdbcConfiguration.class);
        ColumnKeyWordsHandler keyWordsHandler = jdbcConfiguration.getKeyWordsHandler();
        if (keyWordsHandler == null) {
            DatabaseType databaseType = jdbcConfiguration.getDbType();
            if (databaseType == BuiltinDatabaseType.MYSQL) {
                keyWordsHandler = new MySqlKeyWordsHandler();
            } else if (databaseType == BuiltinDatabaseType.POSTGRE_SQL) {
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
