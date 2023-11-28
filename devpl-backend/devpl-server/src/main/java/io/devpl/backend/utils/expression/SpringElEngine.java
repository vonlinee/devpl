package io.devpl.backend.utils.expression;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * <a href="https://www.w3schools.cn/spring_expression_language/spring_expression_language_expression_templating.html">...</a>
 */
public class SpringElEngine implements ExpressionEngine {

    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 表达式允许将文字文本与评估块混合。 每个评估块都应正确添加前缀和后缀。 标准选择是使用 #{}。
     * 板表达式允许文字和表达式混合使用, 一般选择使用#{}作为一个定界符
     *
     * @param expression 表达式
     * @param context    参数
     * @param resultType 结果类型
     * @param <T>        结果类型
     * @return 表达式执行结果
     */
    @Override
    public <T> T eval(String expression, Map<String, Object> context, Class<T> resultType) {
        EvaluationContext elContext = new StandardEvaluationContext(context);

        Expression parsedExpression = parser.parseExpression(expression, new TemplateParserContext());

        return parsedExpression.getValue(elContext, resultType);
    }
}
