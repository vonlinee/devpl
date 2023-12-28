package io.devpl.codegen.template;

import java.util.HashMap;
import java.util.Map;

/**
 * 模板参数Map, 等同于Map<String, Object>
 */
public final class TemplateArgumentsMap implements TemplateArguments {

    private final Map<String, Object> argumentsMap;

    public TemplateArgumentsMap() {
        this.argumentsMap = new HashMap<>();
    }

    public TemplateArgumentsMap(Map<? extends String, ?> m) {
        this.argumentsMap = new HashMap<>(m);
    }

    @Override
    public Map<String, Object> asMap() {
        return argumentsMap;
    }

    @Override
    public void setValue(String name, Object value) {
        argumentsMap.put(name, value);
    }

    @Override
    public Object getValue(String name) {
        return argumentsMap.get(name);
    }

    @Override
    public boolean isMap() {
        return true;
    }
}
