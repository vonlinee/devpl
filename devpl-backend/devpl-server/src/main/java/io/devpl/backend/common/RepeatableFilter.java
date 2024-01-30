package io.devpl.backend.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 把HttpServletRequest包装成RepeatableHttpServletRequest，然后丢给后面的调用链
 */
public class RepeatableFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = new RepeatableHttpServletRequest((HttpServletRequest) servletRequest);
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
