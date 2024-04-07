package io.devpl.codegen.generator.config;

import java.util.Set;

import io.devpl.codegen.generator.TableGeneration;
import io.devpl.codegen.util.StringUtils;

/**
 * 默认名称转换接口类
 */
public class DefaultNameConvert implements NameConverter {

    private final StrategyConfiguration strategyConfiguration;

    public DefaultNameConvert(StrategyConfiguration strategyConfiguration) {
        this.strategyConfiguration = strategyConfiguration;
    }

    @Override
    public String entityNameConvert(TableGeneration tableInfo) {
        return NamingStrategy.capitalFirst(processName(tableInfo.getName(), strategyConfiguration.entity()
            .getNamingStrategy(), strategyConfiguration.getTablePrefix(), strategyConfiguration.getTableSuffix()));
    }

    @Override
    public String propertyNameConvert(String propertyName) {
        return processName(propertyName, strategyConfiguration.entity()
            .getColumnNamingStrategy(), strategyConfiguration.getFieldPrefix(), strategyConfiguration.getFieldSuffix());
    }

    private String processName(String name, NamingStrategy strategy, Set<String> prefix, Set<String> suffix) {
        String propertyName = name;
        // 删除前缀
        if (!prefix.isEmpty()) {
            propertyName = NamingStrategy.removePrefix(propertyName, prefix);
        }
        // 删除后缀
        if (!suffix.isEmpty()) {
            propertyName = NamingStrategy.removeSuffix(propertyName, suffix);
        }
        if (!StringUtils.hasText(propertyName)) {
            throw new RuntimeException(String.format("%s 的名称转换结果为空，请检查是否配置问题", name));
        }
        // 下划线转驼峰
        if (NamingStrategy.UNDERLINE_TO_CAMEL.equals(strategy)) {
            return NamingStrategy.underlineToCamel(propertyName);
        }
        return propertyName;
    }
}
