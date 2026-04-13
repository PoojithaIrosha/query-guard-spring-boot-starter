package io.poojithairosha.query_guard_spring_boot_starter.report;

import io.poojithairosha.query_guard_spring_boot_starter.analysis.AnalysisResult;
import io.poojithairosha.query_guard_spring_boot_starter.analysis.Severity;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

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
                .toList();

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
