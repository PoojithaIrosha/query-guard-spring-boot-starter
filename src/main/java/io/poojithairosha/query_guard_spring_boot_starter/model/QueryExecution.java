package io.poojithairosha.query_guard_spring_boot_starter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class QueryExecution {
    private final String sql;
    private final long executionTimeMs;
}
