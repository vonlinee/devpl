package io.devpl.generator.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class WebMvcConfiguration implements WebMvcConfigurer {

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
            .allowCredentials(false).maxAge(3600);
    }

    @Override
    public void addReturnValueHandlers(@NotNull List<HandlerMethodReturnValueHandler> returnValueHandlers) {

    }

    @Override
    public void configureMessageConverters(@NotNull List<HttpMessageConverter<?>> converters) {

    }

}
