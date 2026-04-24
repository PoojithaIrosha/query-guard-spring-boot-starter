package io.github.poojithairosha.query_guard_spring_boot_starter.report;

import io.github.poojithairosha.query_guard_spring_boot_starter.analysis.AnalysisResult;
import io.github.poojithairosha.query_guard_spring_boot_starter.analysis.Severity;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

public class QueryGuardReportBuilder {

    public QueryGuardReport build(
            HttpServletRequest request,
            int queryCount,
            long totalTime,
            List<AnalysisResult> results,
            String traceId
    ) {

        List<QueryGuardIssue> issues = results.stream()
                .map(r -> new QueryGuardIssue(
                        r.getType(),
                        r.getSeverity(),
                        r.getMessage(),
                        r.getPattern(),
                        r.getSuggestions(),
                        r.getOccurrenceCount()
                ))
                .collect(Collectors.toList());

        boolean hasNPlusOne = results.stream()
                .anyMatch(r -> "N_PLUS_ONE".equals(r.getType()));

        boolean hasDuplicates = results.stream()
                .anyMatch(r -> "N_PLUS_ONE".equals(r.getType()));

        Severity overallSeverity = Severity.INFO;
        if(hasDuplicates) overallSeverity = Severity.WARNING;
        if(hasNPlusOne) overallSeverity = Severity.CRITICAL;

        return new QueryGuardReport(
                request.getRequestURI(),
                request.getMethod(),
                queryCount,
                totalTime,
                overallSeverity,
                hasNPlusOne,
                issues,
                traceId,
                System.currentTimeMillis()
        );
    }

}
