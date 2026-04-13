package io.poojithairosha.query_guard_spring_boot_starter.web;

import io.poojithairosha.query_guard_spring_boot_starter.context.QueryContext;
import io.poojithairosha.query_guard_spring_boot_starter.context.QueryContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class QueryGuardFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            QueryContextHolder.init();

            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            QueryContext context = QueryContextHolder.get();

            if (context != null) {
                logSummary((HttpServletRequest) servletRequest, context);
            }

            QueryContextHolder.clear();
        }
    }

    private void logSummary(HttpServletRequest request, QueryContext context) {
        System.out.println("==== QueryGuard Summary ====");
        System.out.println("Endpoint: " + request.getRequestURI());
        System.out.println("Query Count: " + context.getQueryCount());
        System.out.println("Total Query Time: " + context.getTotalExecutionTime() + " ms");
        System.out.println("============================");
    }
}
