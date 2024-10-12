package io.devpl.backend.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * 把HttpServletRequest包装成RepeatableHttpServletRequest，然后丢给后面的调用链
 */
public class RepeatableFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String contentType = servletRequest.getContentType();
//        if (contentType != null) {
//            MediaType mediaType = MediaType.parseMediaType(contentType);
//            if (mediaType.isCompatibleWith(MediaType.MULTIPART_FORM_DATA)) {
//                // 文件上传场景
//                filterChain.doFilter(servletRequest, servletResponse);
//            }
//        } else {
        servletRequest = new RepeatableHttpServletRequest((HttpServletRequest) servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
//        }
    }
}
