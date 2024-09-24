package io.devpl.backend.tools.ddl;

import io.devpl.backend.tools.ddl.enums.SqlTypeAndJavaTypeEnum;
import io.devpl.sdk.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * 生成DDL SQL语句
 */
public class DdlUtils {

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
            if (maxFieldSqlTypeStringLength <= getSqlType(field).length()) {
                maxFieldSqlTypeStringLength = getSqlType(field).length();
            }
        }
        maxFieldStringLength++;
        maxFieldSqlTypeStringLength++;

        for (Field field : fieldList) {
            String tableColumn = field.getTableColumn();
            builder = builder.space(4)
                .addColumn(String.format("%-" + maxFieldStringLength + "s", tableColumn))
                .addType(String.format("%-" + maxFieldSqlTypeStringLength + "s", getSqlType(field)))
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
            // TODO 翻译
            HashMap<String, String> map = new HashMap<>();
            String tableNameCommend = map.getOrDefault(tableName.replace("_", " "), tableName);
            builder.space().addComment(tableNameCommend);
        }
        return builder.end();
    }

    /**
     * 获取mysql类型
     */
    public static String getSqlTypeForMapping(Field field) {
        return SqlTypeAndJavaTypeEnum.findByJavaType(field.getType()).getSqlType();
    }

    public static String getSqlTypeSize(Field field) {
        return SqlTypeAndJavaTypeEnum.findByJavaType(field.getType()).getDefaultLength();
    }

    @NotNull
    public static String getSqlType(Field field) {
        SqlTypeInfo sqlTypeInfo = SqlTypeMapUtil.getInstance().typeConvert(field.getType());
        if (null != sqlTypeInfo) {
            return sqlTypeInfo.getSqlType() + sqlTypeInfo.getSqlTypeLength();
        }
        /*兜底配置*/
        return getSqlTypeForMapping(field) + getSqlTypeSize(field);
    }
}
