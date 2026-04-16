package io.github.poojithairosha.query_guard_spring_boot_starter.listener;

import io.github.poojithairosha.query_guard_spring_boot_starter.model.QueryExecution;

public interface QueryExecutionListener {

    void beforeQuery(String sql);
    void afterQuery(QueryExecution execution);

}
