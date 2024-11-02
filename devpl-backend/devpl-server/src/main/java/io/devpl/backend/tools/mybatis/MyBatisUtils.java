package io.devpl.backend.tools.mybatis;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import io.devpl.backend.utils.SimpleSqlFormatter;
import io.devpl.backend.utils.SqlFormatter;
import io.devpl.common.utils.XMLUtils;
import io.devpl.sdk.lang.Interpolations;
import io.devpl.sdk.util.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyBatisUtils {

    public static String COMMA = ",";
    public static String SEMICOLON = ";";
    public static Pattern PREPARING_PATTERN = Pattern.compile("Preparing:(.*?)(?=\n|\r|\r\n)");
    public static Pattern PARAMETER_PATTERN = Pattern.compile("Parameters:(.*?)(?=\n|\r|\r\n)");
    static String L_BRACKET = "(";
    static String R_BRACKET = ")";
    static Pattern END_WITH_PAREN = Pattern.compile("\\((.*?)\\)$");
    private static final SqlFormatter FORMATTER = new SimpleSqlFormatter();

    /**
     * using Hibernate formatter
     *
     * @param sql the executable sql
     * @return formatted executable sql
     */
    public static String format(String sql) {
        if (StringUtils.isBlank(sql)) {
            return StringUtils.EMPTY;
        }
        sql = FORMATTER.format(null, sql);
        return sql.endsWith(SEMICOLON) ? sql : sql + SEMICOLON;
    }

    public static String parseExecutableSqlFromMyBatisLog(String mybatisLog) {
        return parseExecutableSql(mybatisLog, true);
    }

    public static String parseExecutableSql(String mybatisLog, boolean formatSqlEnabled) {
        String executableSql = parseExecutableSql(mybatisLog);
        if (formatSqlEnabled) {
            return format(executableSql);
        }
        return executableSql;
    }

    /**
     * @param mybatisLogs the s mybatis logs
     *                    ==> Preparing: select * from table where id = ?
     *                    ==> Parameters: 123(String)
     * @return an executable sql
     */
    public static String parseExecutableSql(String mybatisLogs) {
        if (mybatisLogs == null || mybatisLogs.isEmpty()) {
            return "";
        }
        mybatisLogs = mybatisLogs.trim();
        String lineSeparator = System.lineSeparator();
        if (!mybatisLogs.endsWith(lineSeparator)) {
            mybatisLogs += lineSeparator;
        }

        String preparedSql = "";
        Matcher preparingSqlMatcher = PREPARING_PATTERN.matcher(mybatisLogs);
        if (preparingSqlMatcher.find()) {
            preparedSql = preparingSqlMatcher.group(1).trim();
        }
        Matcher paramsMatcher = PARAMETER_PATTERN.matcher(mybatisLogs);
        if (paramsMatcher.find()) {
            String params = paramsMatcher.group(1);
            if (StringUtils.isBlank(params)) {
                return format(preparedSql);
            }
            String[] paramItems = params.split(COMMA);
            for (int i = 0; i < paramItems.length; i++) {
                paramItems[i] = paramItems[i].trim();
            }
            for (String param : paramItems) {
                String value = getParamValue(param);
                preparedSql = StringUtils.replaceOnce(preparedSql, "?", value);
            }
        }
        return preparedSql;
    }

    private static String getParamValue(String param) {
        if (!END_WITH_PAREN.matcher(param).find()) {
            return MSParamDataType.STRING.normalize(param.trim());
        }
        String value = param.substring(0, param.indexOf(L_BRACKET)).trim();
        MSParamDataType type = MSParamDataType.NULL;
        try {
            type = MSParamDataType.valueOf(param.substring(param.indexOf(L_BRACKET) + 1, param.indexOf(R_BRACKET)).toUpperCase());
        } catch (IllegalArgumentException ignored) {

        }

        return type.normalize(value);
    }

    /**
     * MyBatis使用XML语法，转义字符如下
     * &lt;<  小于号；
     * &gt;> 大于号；
     * &amp; & 和 ；
     * &apos;  ‘’单引号；
     * &quot; “”  双引号；
     *
     * @param content XML内容
     * @return 未转义的字符
     */
    public static String toRawUnEscapedContent(String content) {
        content = content.replace("<", XMLUtils.wrapWithCDATA("<"));
        return content;
    }

    /**
     * ifTest("name", "param.username") => 结果如下：
     * <if test="param.username != null and param.username != ''">
     * name = #{param.username}
     * </if>
     *
     * @param columnName 数据库列名
     * @param paramName  参数名
     * @return 标签文本 <if test=''></if>
     */
    public static String ifTest(String columnName, String paramName) {
        Map<String, String> map = new HashMap<>();
        map.put("columnName", columnName);
        map.put("paramName", paramName);
        return Interpolations.named("<if test=\"{paramName} != null and {paramName} != ''\">\n\t{columnName} = #{{paramName}}\n</if>", map);
    }

    /**
     * 填充实体值, 仅支持基本类型
     *
     * @param defaultValueMap key为数据库列名, value为值
     */
    public static String fillEntity(Object entity, Map<String, String> defaultValueMap, boolean useDbColumnKey) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
        StringBuilder sb = new StringBuilder();
        for (TableFieldInfo tableFieldInfo : tableInfo.getFieldList()) {
            try {
                Field field = tableFieldInfo.getField();
                if (!field.canAccess(entity)) {
                    field.setAccessible(true);
                }
                field.set(entity, defaultValueMap.get(useDbColumnKey ? tableFieldInfo.getColumn() : tableFieldInfo.getProperty()));
            } catch (IllegalAccessException e) {
                sb.append(tableFieldInfo.getColumn());
            }
        }
        return sb.toString();
    }
}
