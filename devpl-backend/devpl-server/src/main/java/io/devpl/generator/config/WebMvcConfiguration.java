package io.devpl.generator.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * WEB Mvc 全部的配置
 */
@Configuration(proxyBeanMethods = false)
public class WebMvcConfiguration implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebMvcConfiguration.class);

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestTracer());
    }

    /**
     * 解决Application跨域
     *
     * @param registry CORS
     * @see CorsFilterConfiguration
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
    // @Configuration(proxyBeanMethods = false)
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
}
