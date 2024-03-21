package io.devpl.backend.config;

import io.devpl.backend.common.RepeatableFilter;
import io.devpl.backend.common.RequestTracer;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.common.query.StatusCode;
import jakarta.servlet.Filter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * WEB Mvc 全部的配置
 */
@Configuration(proxyBeanMethods = false)
public class WebMvcConfiguration implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebMvcConfiguration.class);

    @Value(value = "${server.servlet.context-path:}")
    private String contextPath;

    @Value(value = "${spring.profiles.active:}")
    private String activeProfile;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }

    /**
     * 利用过滤器配置，把RepeatableFilter设置成第一个调用的过滤器。注意FilterRegistrationBean的setOrder()方法，数值越小越先执行。
     *
     * @return 过滤器配置Bean
     */
    @Bean
    public FilterRegistrationBean<Filter> repeatableFilterBean() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RepeatableFilter());
        registration.addUrlPatterns("/*");
        registration.setName("repeatableFilter");
        registration.setOrder(1);
        return registration;
    }

    /**
     * 注册请求拦截器
     *
     * @param registry 拦截器注册中心
     */
    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        if ("dev".equalsIgnoreCase(activeProfile)) {
            RequestTracer requestTracer = new RequestTracer();
            requestTracer.setServletContextPath(this.contextPath);
            registry.addInterceptor(requestTracer);
        }
    }

    /**
     * 跨域过滤器配置
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    /**
     * 解决Application跨域
     *
     * @param registry CORS
     * @see WebMvcConfiguration#corsFilter()
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("PUT", "DELETE", "GET", "POST")
            .allowedHeaders("*")
            .exposedHeaders("access-control-allow-headers",
                "access-control-allow-methods",
                "access-control-allow-origin",
                "access-control-max-age",
                "X-Frame-Options")
            .allowCredentials(false)
            .maxAge(3600);
    }

    /**
     * 自定义的参数解析器的优先级是低于Spring内置的
     */
    @Configuration(proxyBeanMethods = false)
    public static class RequestMethodProcessorConfiguration implements InitializingBean {

        private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

        public RequestMethodProcessorConfiguration(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
            this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            log.info("自定义HandlerMethodReturnValueHandler");
            List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
            if (returnValueHandlers == null) {
                returnValueHandlers = new ArrayList<>(0);
            } else {
                /**
                 * @see HandlerMethodReturnValueHandlerComposite#getHandlers()
                 */
                returnValueHandlers = new ArrayList<>(returnValueHandlers);
            }

            ControllerRequestResponseProcessor _processor = null;

            for (int i = 0; i < returnValueHandlers.size(); i++) {
                if (returnValueHandlers.get(i) instanceof RequestResponseBodyMethodProcessor processor) {
                    returnValueHandlers.set(i, _processor = new ControllerRequestResponseProcessor(processor));
                    break;
                }
            }
            requestMappingHandlerAdapter.setReturnValueHandlers(Collections.unmodifiableList(returnValueHandlers));

            // 参数解析器
            log.info("自定义参数解析器HandlerMethodArgumentResolver");
            List<HandlerMethodArgumentResolver> argumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
            if (argumentResolvers == null) {
                argumentResolvers = new ArrayList<>(0);
            } else {
                argumentResolvers = new ArrayList<>(argumentResolvers);
            }
            argumentResolvers.add(_processor);
            requestMappingHandlerAdapter.setArgumentResolvers(argumentResolvers);
        }
    }

    @Bean
    public ConfigurableServletWebServerFactory servletWebServerFactory() {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        HashSet<ErrorPage> errorPages = new HashSet<>();
        errorPages.add(new ErrorPage(HttpStatus.FORBIDDEN, "/403"));
        errorPages.add(new ErrorPage(HttpStatus.FORBIDDEN, "/404"));
        errorPages.add(new ErrorPage(HttpStatus.FORBIDDEN, "/500"));
        tomcatServletWebServerFactory.setErrorPages(errorPages);
        return tomcatServletWebServerFactory;
    }

    /**
     * 函数式web接口
     * 给容器中放一个RouterFunction<ServerResponse>的bean，统一路由管理
     * 核心四大对象
     * 1.RouterFunction:定义路由信息。发什么请求，由谁来处理
     * 2.RequestPredicate：定义请求规则：请求方式、请求参数
     * 3.ServerRequest：封装请求完整数据
     * 4.ServerResponse：封装响应完整数据
     *
     * @return 路由配置
     */
    @Bean
    public RouterFunction<ServerResponse> globalRouteMapping() {
        return RouterFunctions
            .route()
            .GET("/403", RequestPredicates.accept(MediaType.ALL), (request) -> ServerResponse.ok().body(Result.error(StatusCode.FORBIDDEN)))
            .GET("/404", RequestPredicates.accept(MediaType.ALL), (request) -> ServerResponse.ok().body(Result.error(StatusCode.NOT_FOUND)))
            .GET("/500", RequestPredicates.accept(MediaType.ALL), (request) -> ServerResponse.ok().body(Result.error(StatusCode.INTERNAL_SERVER_ERROR)))
            .GET("/error", RequestPredicates.accept(MediaType.ALL), (request) -> ServerResponse.ok().body(Result.error(StatusCode.NOT_FOUND)))
            .build();
    }
}
