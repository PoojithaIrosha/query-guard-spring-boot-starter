package io.poojithairosha.query_guard_spring_boot_starter.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.poojithairosha.query_guard_spring_boot_starter.event.QueryGuardEvent;

public class QueryGuardMetrics {

    private final Counter totalRequests;
    private final Counter nPlusOneRequests;
    private final Counter criticalRequests;

    public QueryGuardMetrics(MeterRegistry registry) {
        this.totalRequests = Counter.builder("queryguard_requests_total")
                .description("Total requests")
                .register(registry);

        this.nPlusOneRequests = Counter.builder("queryguard_nplusone_total")
                .description("N+1 detected requests")
                .register(registry);

        this.criticalRequests = Counter.builder("queryguard_critical_total")
                .description("Critical requests")
                .register(registry);
    }

    public void recordMetric(QueryGuardEvent event) {
        totalRequests.increment();

        if (event.getNPlusOneCount() > 0) {
            nPlusOneRequests.increment(event.getNPlusOneCount());
        }

        if ("CRITICAL".equals(event.getSeverity())) {
            criticalRequests.increment();
        }
    }
}
