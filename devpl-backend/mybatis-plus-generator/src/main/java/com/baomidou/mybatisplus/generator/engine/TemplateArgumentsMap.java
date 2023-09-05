package com.baomidou.mybatisplus.generator.engine;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 模板参数Map
 */
public final class TemplateArgumentsMap extends LinkedHashMap<String, Object> implements TemplateArguments {

    @Override
    public @NotNull Map<String, Object> asMap() {
        return this;
    }
}
