package io.poojithairosha.query_guard_spring_boot_starter.analysis;

import io.poojithairosha.query_guard_spring_boot_starter.context.QueryContext;
import io.poojithairosha.query_guard_spring_boot_starter.model.QueryExecution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DuplicateQueryAnalyzer implements QueryAnalyzer {

    private static final int DUPLICATE_THRESHOLD = 1;

    @Override
    public List<AnalysisResult> analyze(QueryContext context) {
        log.debug("[DuplicateQueryAnalyzer] start analyzing");
        if (context == null || context.getQueries().isEmpty()) {
            log.debug("[DuplicateQueryAnalyzer] nothing to analyze. skipping");
            return Collections.emptyList();
        }

        Map<String, Long> queryCountMap = context.getQueries()
                .stream()
                .collect(Collectors.groupingBy(
                        this::normalize,
                        Collectors.counting()
                ));

        log.debug("[DuplicateQueryAnalyzer] query count map {}", queryCountMap);

        ArrayList<AnalysisResult> results = new ArrayList<>();

        queryCountMap.forEach((query, count) -> {
            if (count > DUPLICATE_THRESHOLD) {
                results.add(AnalysisResult.of(
                        "DUPLICATE_QUERY",
                        buildMessage(query, count)
                ));
            }
        });

        log.debug("[DuplicateQueryAnalyzer] final results - {}", results);

        return results;
    }

    private String normalize(QueryExecution execution) {
        return execution.getSql()
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String buildMessage(String query, Long count) {
        return String.format("Query executed %d times: %s", count, query);
    }
}
