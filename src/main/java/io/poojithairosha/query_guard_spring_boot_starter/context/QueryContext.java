package io.poojithairosha.query_guard_spring_boot_starter.context;

import io.poojithairosha.query_guard_spring_boot_starter.model.QueryExecution;

import java.util.ArrayList;
import java.util.List;

public class QueryContext {

    private final List<QueryExecution> queries = new ArrayList<>();

    public void addQuery(QueryExecution execution) {
        queries.add(execution);
    }

    public int getQueryCount() {
        return queries.size();
    }

    public List<QueryExecution> getQueries() {
        return queries;
    }

    public long getTotalExecutionTime() {
        return queries.stream()
                .mapToLong(QueryExecution::getExecutionTimeMs)
                .sum();
    }

}
