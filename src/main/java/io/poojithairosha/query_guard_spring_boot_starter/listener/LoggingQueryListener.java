package io.poojithairosha.query_guard_spring_boot_starter.listener;

import io.poojithairosha.query_guard_spring_boot_starter.model.QueryExecution;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingQueryListener implements QueryExecutionListener {

    @Override
    public void beforeQuery(String sql) {
        log.info("Executing Query - {}", sql);
    }

    @Override
    public void afterQuery(QueryExecution execution) {
        log.info("Query executed in {} ms", execution.getExecutionTimeMs());
    }
}
