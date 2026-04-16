package io.github.poojithairosha.query_guard_spring_boot_starter.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class QueryGuardEvent {
    private final String endpoint;
    private final String method;
    private final int queryCount;
    private final long executionTimeMs;
    private final String severity;
    private final boolean nPlusOne;
    private final int nPlusOneCount;
}
