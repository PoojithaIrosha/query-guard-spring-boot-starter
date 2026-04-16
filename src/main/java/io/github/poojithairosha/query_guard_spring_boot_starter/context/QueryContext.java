package io.github.poojithairosha.query_guard_spring_boot_starter.context;

import io.github.poojithairosha.query_guard_spring_boot_starter.model.QueryExecution;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QueryContext {

    private final List<QueryExecution> queries = new ArrayList<>();
    private long startTime;
    private String endpoint;
    private String method;
    private boolean nPlusOneDetected;
    private List<String> issues = new ArrayList<>();

    public void addQuery(QueryExecution execution) {
        queries.add(execution);
    }

    public int getQueryCount() {
        return queries.size();
    }

    public double getTotalExecutionTime() {
        return queries.stream()
                .mapToDouble(QueryExecution::getExecutionTimeMs)
                .sum();
    }

}
