package io.poojithairosha.query_guard_spring_boot_starter.report;

import io.poojithairosha.query_guard_spring_boot_starter.analysis.Severity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class QueryGuardReport {
    private final String type = "QUERY_GUARD";

    private final String endpoint;
    private final String method;
    private final int queryCount;
    private final long totalExecutionTimeMs;
    private final Severity overallSeverity;

    private final boolean nPlusOneDetected;
    private final List<QueryGuardIssue> issues;

    private final String traceId;
    private final long timestamp;

}
