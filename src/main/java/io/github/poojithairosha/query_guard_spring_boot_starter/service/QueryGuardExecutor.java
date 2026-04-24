package io.github.poojithairosha.query_guard_spring_boot_starter.service;

import io.github.poojithairosha.query_guard_spring_boot_starter.analysis.AnalysisResult;
import io.github.poojithairosha.query_guard_spring_boot_starter.analysis.QueryAnalysisEngine;
import io.github.poojithairosha.query_guard_spring_boot_starter.analysis.QueryResultProcessor;
import io.github.poojithairosha.query_guard_spring_boot_starter.config.QueryGuardProperties;
import io.github.poojithairosha.query_guard_spring_boot_starter.context.QueryContext;
import io.github.poojithairosha.query_guard_spring_boot_starter.context.QueryContextHolder;
import io.github.poojithairosha.query_guard_spring_boot_starter.logging.QueryGuardLogger;
import io.github.poojithairosha.query_guard_spring_boot_starter.model.QueryExecution;
import io.github.poojithairosha.query_guard_spring_boot_starter.model.RequestTrace;
import io.github.poojithairosha.query_guard_spring_boot_starter.report.QueryGuardReport;
import io.github.poojithairosha.query_guard_spring_boot_starter.report.QueryGuardReportBuilder;
import io.github.poojithairosha.query_guard_spring_boot_starter.storage.TraceStorage;
import io.github.poojithairosha.query_guard_spring_boot_starter.trace.TraceContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class QueryGuardExecutor {
    private final QueryAnalysisEngine engine;
    private final QueryGuardReportBuilder reportBuilder;
    private final QueryGuardLogger logger;
    private final QueryGuardProperties properties;
    private final TraceStorage traceStorage;

    private boolean shouldSkip(HttpServletRequest request) {
        String path = request.getRequestURI();

        List<String> excludePaths = properties.getTracing().getExcludePaths();
        excludePaths.add("/queryguard/**");
        excludePaths.add("/queryguard-ui/**");

        return properties.getTracing().getExcludePaths().stream()
                .anyMatch(pattern -> path.startsWith(pattern.replace("/**", "")));
    }

    public void execute(HttpServletRequest request, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        if (shouldSkip(request)) {
            chain.doFilter(request, servletResponse);
            return;
        }

        boolean traceCreated = false;
        String traceId = MDC.get("traceId");

        if (traceId == null) {
            traceId = TraceContext.getOrCreateTraceId();
            traceCreated = true;
        }

        long start = System.currentTimeMillis();

        try {
            QueryContextHolder.init();

            QueryContext context = QueryContextHolder.get();
            context.setStartTime(System.currentTimeMillis());
            context.setEndpoint(request.getRequestURI());
            context.setMethod(request.getMethod());

            chain.doFilter(request, servletResponse);
        } finally {
            long time = System.currentTimeMillis() - start;
            QueryContext context = QueryContextHolder.get();

            if (context != null) {
                RequestTrace trace = RequestTrace.builder()
                        .traceId(traceId)
                        .endpoint(context.getEndpoint())
                        .method(context.getMethod())
                        .startTime(context.getStartTime())
                        .build();

                for (QueryExecution q : context.getQueries()) {
                    trace.addQuery(q);
                }

                List<AnalysisResult> analyzed = engine.analyze(context);
                List<AnalysisResult> processed = QueryResultProcessor.process(analyzed);

                trace.setTotalExecutionTimeMs(System.currentTimeMillis() - context.getStartTime());
                trace.setNPlusOneDetected(!analyzed.isEmpty());
                trace.setIssues(
                        analyzed.stream()
                                .map(AnalysisResult::getMessage)
                                .collect(Collectors.toList())
                );

                QueryGuardReport report = reportBuilder.build(
                        request,
                        context.getQueryCount(),
                        time,
                        processed,
                        traceId
                );

                traceStorage.save(trace);
                if(properties.getLogging().isEnabled()) {
                    logger.log(report);
                }
            }

            TraceContext.clear(traceCreated);
            QueryContextHolder.clear();
        }
    }
}
