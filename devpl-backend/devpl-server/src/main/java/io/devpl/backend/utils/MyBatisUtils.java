package io.devpl.backend.utils;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import io.devpl.sdk.lang.Interpolations;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * MyBatis工具类
 */
public class MyBatisUtils {

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
