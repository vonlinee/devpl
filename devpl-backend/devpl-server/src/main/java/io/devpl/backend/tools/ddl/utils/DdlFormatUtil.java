package io.devpl.backend.tools.ddl.utils;

import io.devpl.backend.tools.ddl.model.Field;
import io.devpl.backend.tools.ddl.service.DdlBuilder;
import io.devpl.backend.tools.ddl.setting.MainSetting;

import java.util.HashMap;
import java.util.List;

/**
 * 生成DDL SQL语句
 */
public class DdlFormatUtil {

    public static String buildDdlScript(String tableName, List<Field> fieldList) {
        Boolean autoTranslation = MainSetting.getInstance().myProperties.getAutoTranslationRadio();
        DdlBuilder builder = new DdlBuilder().create()
            .tableName(tableName)
            .leftParenthesis()
            .wrap();
        int maxFieldStringLength = 0;
        int maxFieldSqlTypeStringLength = 0;
        for (Field field : fieldList) {
            if (maxFieldStringLength <= field.getTableColumn().length()) {
                maxFieldStringLength = field.getTableColumn().length();
            }
            if (maxFieldSqlTypeStringLength <= field.getSqlType().length()) {
                maxFieldSqlTypeStringLength = field.getSqlType().length();
            }
        }
        maxFieldStringLength++;
        maxFieldSqlTypeStringLength++;

        for (Field field : fieldList) {
            String tableColumn = field.getTableColumn();
            builder = builder.space(4)
                .addColumn(String.format("%-" + maxFieldStringLength + "s", tableColumn))
                .addType(String.format("%-" + maxFieldSqlTypeStringLength + "s", field.getSqlType()))
                .isPrimaryKey(field.isPrimaryKey());
            if (null != field.getComment()) {
                builder.space().addComment(field.getComment());
            }
            builder.addComma()
                .wrap();
        }

        builder = builder.remove(2)
            .wrap()
            .rightParenthesis();
        if (autoTranslation) {
            HashMap<String, String> map = new HashMap<>();
            String tableNameCommend = map.getOrDefault(tableName.replace("_", " "), tableName);
            builder.space().addComment(tableNameCommend);
        }
        return builder.end();
    }
}
