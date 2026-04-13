package io.poojithairosha.query_guard_spring_boot_starter.web;

import io.poojithairosha.query_guard_spring_boot_starter.analysis.*;
import io.poojithairosha.query_guard_spring_boot_starter.context.QueryContext;
import io.poojithairosha.query_guard_spring_boot_starter.context.QueryContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class QueryGuardFilter implements Filter {

    private final QueryAnalysisEngine queryAnalysisEngine;

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
        log.info("==== QueryGuard Summary ====");

        log.info("Endpoint: " + request.getRequestURI());
        log.info("Query Count: " + context.getQueryCount());
        log.info("Total Query Time: " + context.getTotalExecutionTime() + " ms");

        queryAnalysisEngine.analyze(context).forEach(result -> {
            log.warn("⚠\uFE0F [{}] {}", result.getType(), result.getMessage());
        });

        log.info("============================");
    }
}
