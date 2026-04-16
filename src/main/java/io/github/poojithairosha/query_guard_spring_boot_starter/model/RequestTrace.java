package io.github.poojithairosha.query_guard_spring_boot_starter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RequestTrace {
    private String traceId;
    private String endpoint;
    private String method;
    private long startTime;
    private long totalExecutionTimeMs;
    private boolean nPlusOneDetected;

    @Builder.Default
    private List<QueryExecution> queries = new ArrayList<>();

    @Builder.Default
    private List<String> issues = new ArrayList<>();

    public void addQuery(QueryExecution query) {
        this.queries.add(query);
    }
}
