package io.github.poojithairosha.query_guard_spring_boot_starter.logging;

import io.github.poojithairosha.query_guard_spring_boot_starter.event.QueryGuardEvent;
import io.github.poojithairosha.query_guard_spring_boot_starter.report.QueryGuardIssue;
import io.github.poojithairosha.query_guard_spring_boot_starter.report.QueryGuardReport;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import tools.jackson.databind.json.JsonMapper;

@RequiredArgsConstructor
public class QueryGuardLogger {
    private static final Logger log = LoggerFactory.getLogger("QUERY_GUARD");

    private final ApplicationEventPublisher applicationEventPublisher;
    private final JsonMapper jsonMapper;

    public void log(QueryGuardReport report) {
        try {
            String json = jsonMapper.writeValueAsString(report);
            log.info(json);

            int totalNPlusOne = report.getIssues().stream()
                    .filter(issue -> "N_PLUS_ONE".equals(issue.getType()))
                    .mapToInt(QueryGuardIssue::getOccurrenceCount)
                    .sum();

            log.info("from queryguardlogger event emitted!");
            applicationEventPublisher.publishEvent(
                    new QueryGuardEvent(
                            report.getEndpoint(),
                            report.getMethod(),
                            report.getQueryCount(),
                            report.getTotalExecutionTimeMs(),
                            report.getOverallSeverity().toString(),
                            totalNPlusOne > 0,
                            totalNPlusOne
                    )
            );
        } catch (Exception e) {
            log.error("Failed to serialize QueryGuardReport", e);
        }
    }
}
