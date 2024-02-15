package io.devpl.backend.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * 把HttpServletRequest包装成RepeatableHttpServletRequest，然后丢给后面的调用链
 */
public class RepeatableFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest = new RepeatableHttpServletRequest((HttpServletRequest) servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
