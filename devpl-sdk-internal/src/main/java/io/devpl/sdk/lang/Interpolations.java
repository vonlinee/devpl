package io.devpl.sdk.lang;

import java.util.Collections;
import java.util.Map;

import static java.lang.System.out;

/**
 * 插值字符串
 */
public class Interpolations {
    private static final InterpolationEngine INDEXED_ENGINE = new IndexedInterpolationEngine();
    private static final InterpolationEngine NAMED_ENGINE = new NamedInterpolationEngine();

    public static String indexed(String template, Object... bindings) {
        return INDEXED_ENGINE.combine(template, IndexedInterpolationEngine.createBindings(bindings));
    }

    public static String named(String template, Object... bindings) {
        return NAMED_ENGINE.combine(template, NamedInterpolationEngine.createBindings(bindings));
    }

    public static String named(String template, Map<String, ?> bindings) {
        return named(template, bindings, null);
    }

    public static String named(String template, Map<String, ?> bindings, Object defaultValue) {
        return NAMED_ENGINE.combine(template, NamedInterpolationEngine.createBindings(bindings, defaultValue));
    }

    public static void main(String[] args) {
        out.println(indexed("{0} and {1}", "Li Lei", "Han Meimei"));
        out.println(named("{a} and {b}", "a", "Li Lei", "b", "Han Meimei"));
        out.println(named("{a} and { unknown}", "a", "A", "default"));
        out.println(named("{a} and {  unknown}", Collections.singletonMap("a", "A"), "default"));
    }
}