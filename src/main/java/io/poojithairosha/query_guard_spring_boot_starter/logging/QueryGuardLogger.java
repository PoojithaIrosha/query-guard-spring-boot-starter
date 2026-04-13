package io.poojithairosha.query_guard_spring_boot_starter.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.poojithairosha.query_guard_spring_boot_starter.report.QueryGuardReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryGuardLogger {
    private static final Logger log = LoggerFactory.getLogger("QUERY_GUARD");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void log(QueryGuardReport report) {
        try {
            String json = objectMapper.writeValueAsString(report);
            log.info(json);
        } catch (Exception e) {
            log.error("Failed to serialize QueryGuardReport", e);
        }
    }
}
