package io.github.poojithairosha.query_guard_spring_boot_starter.filter;

import io.github.poojithairosha.query_guard_spring_boot_starter.service.QueryGuardExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
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
