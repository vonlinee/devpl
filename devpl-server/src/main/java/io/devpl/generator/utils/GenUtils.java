package io.devpl.generator.utils;

import cn.hutool.core.text.NamingCase;
import io.devpl.sdk.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码生成器 工具类
 */
@Slf4j
public class GenUtils {

    /**
     * 获取模块名
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        return StringUtils.subAfter(packageName, ".", true);
    }

    /**
     * 获取功能名
     * @param tableName 表名
     * @return 功能名
     */
    public static String getFunctionName(String tableName) {
        String functionName = StringUtils.subAfter(tableName, "_", true);
        if (StringUtils.isBlank(functionName)) {
            functionName = tableName;
        }
        return functionName;
    }

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
