package io.devpl.generator.config;

import io.devpl.generator.common.query.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 返回值统一处理
 * 如果Controller没有返回值时，这个接口的代码就不会执行。因为存在另类的写法，Controller的返回值不一定是 return 回去的，可以用类似于 ModelAndView 之类的对象传递
 * SpringBoot下默认的ResponseBody处理类是HandlerMethodReturnValueHandler，此处理器的优先级比此实现类高，
 * 在WebMvcConfigurerAdapter 中配置HandlerMethodReturnValueHandler，其实并不生效
 */
//@Component
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
        if (returnType.getDeclaringClass() == Result.class || Result.class.isAssignableFrom(returnType.getDeclaringClass())) {
            delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            return;
        }
        // 表示此函数可以处理请求，不必交给别的代码处理
        // mavContainer.setRequestHandled(true);

        // 自定义返回格式
        delegate.handleReturnValue(Result.ok(returnValue), returnType, mavContainer, webRequest);
    }
}
