package io.devpl.backend.common;

import io.devpl.sdk.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Setter
@Slf4j
public class RequestTracer implements HandlerInterceptor {

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

        String requestBodyJsonAsString = null;
        if (Objects.equals(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            requestBodyJsonAsString = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        }

        if (requestBodyJsonAsString != null) {
            log.info("url => {} \nbody: {}", sb.substring(0, sb.length()), requestBodyJsonAsString);
        } else {
            log.info("url => {}", sb.substring(0, sb.length()));
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
