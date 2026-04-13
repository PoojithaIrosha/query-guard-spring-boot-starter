package io.poojithairosha.query_guard_spring_boot_starter.service;

import io.poojithairosha.query_guard_spring_boot_starter.analysis.AnalysisResult;
import io.poojithairosha.query_guard_spring_boot_starter.analysis.QueryAnalysisEngine;
import io.poojithairosha.query_guard_spring_boot_starter.analysis.QueryResultProcessor;
import io.poojithairosha.query_guard_spring_boot_starter.config.QueryGuardProperties;
import io.poojithairosha.query_guard_spring_boot_starter.context.QueryContext;
import io.poojithairosha.query_guard_spring_boot_starter.context.QueryContextHolder;
import io.poojithairosha.query_guard_spring_boot_starter.logging.QueryGuardLogger;
import io.poojithairosha.query_guard_spring_boot_starter.report.QueryGuardReport;
import io.poojithairosha.query_guard_spring_boot_starter.report.QueryGuardReportBuilder;
import io.poojithairosha.query_guard_spring_boot_starter.trace.TraceContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;

import java.util.List;

@RequiredArgsConstructor
public class QueryGuardExecutor {
    private final QueryAnalysisEngine engine;
    private final QueryGuardReportBuilder reportBuilder;
    private final QueryGuardLogger logger;
    private final QueryGuardProperties properties;


    public void execute(HttpServletRequest request, ServletResponse servletResponse, FilterChain chain) throws Exception {
        boolean traceCreated = false;
        String traceId = MDC.get("traceId");

        if (traceId == null) {
            traceId = TraceContext.getOrCreateTraceId();
            traceCreated = true;
        }

        long start = System.currentTimeMillis();

        try {
            QueryContextHolder.init();
            chain.doFilter(request, servletResponse);
        } finally {

            long time = System.currentTimeMillis() - start;
            QueryContext context = QueryContextHolder.get();

            if (context != null && properties.getLogging().isEnabled()) {

                List<AnalysisResult> analyzed = engine.analyze(context);
                List<AnalysisResult> processed = QueryResultProcessor.process(analyzed);

                QueryGuardReport report = reportBuilder.build(
                        request,
                        context.getQueryCount(),
                        time,
                        processed,
                        traceId
                );

                logger.log(report);
            }

            TraceContext.clear(traceCreated);
            QueryContextHolder.clear();
        }
    }
}
