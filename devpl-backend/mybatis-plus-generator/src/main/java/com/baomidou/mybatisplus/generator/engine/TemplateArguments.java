package com.baomidou.mybatisplus.generator.engine;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * 模板参数集合
 */
public interface TemplateArguments {

    /**
     * 转为Map结构
     * @return map
     */
    @NotNull Map<String, Object> asMap();
}
