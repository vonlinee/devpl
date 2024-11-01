package io.devpl.backend.config;

import io.devpl.backend.common.query.RestfulResult;
import io.devpl.backend.common.query.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * 对控制器层的请求入参和响应返回值统一处理
 * 如果Controller没有返回值时，这个接口的代码就不会执行。因为存在另类的写法，Controller的返回值不一定是 return 回去的，可以用类似于 ModelAndView 之类的对象传递
 * SpringBoot下默认的ResponseBody处理类是HandlerMethodReturnValueHandler，此处理器的优先级比此实现类高，
 * 在WebMvcConfigurerAdapter 中配置HandlerMethodReturnValueHandler，其实并不生效
 * RequestResponseBodyMethodProcessor是ResponseBody的默认处理实现，为避免完全替换此实现类
 * 替代RequestResponseBodyMethodProcessor的功能
 *
 * @see RequestResponseBodyMethodProcessor
 */
public class ControllerRequestResponseProcessor implements HandlerMethodReturnValueHandler, HandlerMethodArgumentResolver {

    private final RequestResponseBodyMethodProcessor delegate;

    public ControllerRequestResponseProcessor(RequestResponseBodyMethodProcessor delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(@NotNull MethodParameter returnType) {
        return delegate.supportsReturnType(returnType);
    }

    /**
     * Controller的返回值不一定是 return 回去的，可以用类似于 ModelAndView 之类的对象传递
     * TODO 针对使用ModelAndView的场景进行数据包装
     *
     * @param returnValue
     * @param returnType
     * @param mavContainer
     * @param webRequest
     * @throws Exception 例外
     */
    @Override
    public void handleReturnValue(Object returnValue, @NotNull MethodParameter returnType, @NotNull ModelAndViewContainer mavContainer, @NotNull NativeWebRequest webRequest) throws Exception {
        if (returnValue == null) {
            // 如果Controller方法无返回值，不进行处理的话 后台会进行重定向，处理方式: mavContainer.setRequestHandled(true);
            // 比如请求: /api/user/list
            // 那么第二次重定向请求的路径为: /api/user/api/user/list(会报错，Resource Not Found)，即拼接了路径的上级子路径
            mavContainer.setRequestHandled(true);
            delegate.handleReturnValue(Result.ok(null), returnType, mavContainer, webRequest);
            return;
        }

        // 已被包装的无需再进行包装
        if (RestfulResult.class.isAssignableFrom(returnValue.getClass())) {
            delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            return;
        }

        // 表示此函数可以处理请求，不必交给别的代码处理
        mavContainer.setRequestHandled(true);
        // 自定义返回格式
        delegate.handleReturnValue(Result.ok(returnValue), returnType, mavContainer, webRequest);
    }

    @Override
    public boolean supportsParameter(@NotNull MethodParameter parameter) {
        return delegate.supportsParameter(parameter);
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return delegate.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
    }
}
