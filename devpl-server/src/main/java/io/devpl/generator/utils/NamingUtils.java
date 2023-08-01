package io.devpl.generator.utils;

import cn.hutool.core.text.NamingCase;
import io.devpl.sdk.util.StringUtils;

/**
 * 命名工具类
 */
public class NamingUtils {

    /**
     * 表名转驼峰并移除前后缀
     * @param upperFirst   首字母大写
     * @param tableName    表名
     * @param removePrefix 删除前缀
     * @param removeSuffix 删除后缀
     * @return java.lang.String
     */
    public static String camelCase(boolean upperFirst, String tableName, String removePrefix, String removeSuffix) {
        String className = tableName;
        // 移除前缀
        if (StringUtils.isNotBlank(removePrefix)) {
            className = StringUtils.removePrefix(tableName, removePrefix);
        }
        // 移除后缀
        if (StringUtils.isNotBlank(removeSuffix)) {
            className = StringUtils.removeSuffix(className, removeSuffix);
        }
        // 是否首字母大写
        if (upperFirst) {
            return NamingCase.toPascalCase(className);
        } else {
            return NamingCase.toCamelCase(className);
        }
    }
}
