package io.devpl.generator.config;

import io.devpl.generator.common.query.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 返回值统一处理
 */
// @Component
public class ResultHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    public ResultHandlerMethodReturnValueHandler(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(@NotNull MethodParameter returnType) {
        return delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, @NotNull ModelAndViewContainer mavContainer, @NotNull NativeWebRequest webRequest) throws Exception {
        // 如果类或者方法含有不包装注解则忽略包装
        if (returnType.getDeclaringClass() == Result.class) {
            delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            return;
        }
        // 自定义返回格式
        Result<Object> result = new Result<>();
        result.setMsg("none");
        result.setCode(200);
        result.setData(returnValue);
        delegate.handleReturnValue(result, returnType, mavContainer, webRequest);
    }
}
