package io.devpl.backend.utils.expression;

import java.util.Map;

/**
 * 表达式引擎
 */
public interface ExpressionEngine {

    <T> T eval(String expression, Map<String, Object> context, Class<T> resultType);
}
