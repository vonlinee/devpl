package io.devpl.generator.utils;

import io.devpl.sdk.lang.Interpolations;

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
}
