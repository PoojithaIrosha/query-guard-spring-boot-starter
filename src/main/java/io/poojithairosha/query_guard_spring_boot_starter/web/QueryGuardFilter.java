package io.poojithairosha.query_guard_spring_boot_starter.web;

import io.poojithairosha.query_guard_spring_boot_starter.service.QueryGuardExecutor;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class QueryGuardFilter implements Filter {

    private final QueryGuardExecutor executor;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            executor.execute((HttpServletRequest) servletRequest, servletResponse, filterChain);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
