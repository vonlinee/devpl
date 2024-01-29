package io.devpl.backend.common;

import io.devpl.sdk.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Slf4j
public class RequestTracer implements HandlerInterceptor {

    @Setter
    private String servletContextPath;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder sb = new StringBuilder(request.getRequestURI().replace(servletContextPath, ""));

        if (!parameterMap.isEmpty()) {
            sb.append("?");
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                sb.append(entry.getKey()).append("=").append(StringUtils.join(entry.getValue())).append("&");
            }
        }

        log.info("url => {}", sb.substring(0, sb.length() - 1));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
