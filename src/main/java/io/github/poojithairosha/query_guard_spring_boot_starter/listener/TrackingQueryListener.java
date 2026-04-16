package io.github.poojithairosha.query_guard_spring_boot_starter.listener;

import io.github.poojithairosha.query_guard_spring_boot_starter.context.QueryContext;
import io.github.poojithairosha.query_guard_spring_boot_starter.context.QueryContextHolder;
import io.github.poojithairosha.query_guard_spring_boot_starter.model.QueryExecution;

public class TrackingQueryListener implements QueryExecutionListener {

    @Override
    public void beforeQuery(String sql) {
        // implementation
    }

    @Override
    public void afterQuery(QueryExecution execution) {
        QueryContext context = QueryContextHolder.get();
        if(context != null) {
            context.addQuery(execution);
        }
    }
}
