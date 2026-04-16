package io.github.poojithairosha.query_guard_spring_boot_starter.listener;

import io.github.poojithairosha.query_guard_spring_boot_starter.event.QueryGuardEvent;
import io.github.poojithairosha.query_guard_spring_boot_starter.metrics.QueryGuardMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@Slf4j
public class QueryGuardMetricsListener {

    private final QueryGuardMetrics metrics;

    @EventListener
    public void handle(QueryGuardEvent event) {
        log.info("METRIC EVENT: {}", event.getEndpoint());
        metrics.recordMetric(event);
    }

}
